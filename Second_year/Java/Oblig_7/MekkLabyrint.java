import javafx.application.Application;
import javafx.scene.input.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.util.Optional;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/**
 * Dette programmet bruker oblig 5 (labyrint), og lager et GUI-program ut fra
 * det. Jeg har valgt aa ha all koden i én klasse (MekkLabyrint.java).
 */
public class MekkLabyrint extends Application {

    protected Stage stage;
    protected BorderPane rot;
    protected GUIRute[][] ruter;
    protected GridPane rutenett;
    protected Labyrint labyrint;



    @Override
    public void start(Stage stage) {
        this.stage = stage;

        // De fire linjene under er bare for aa legge til musikk i programmet.
        final URL resource = getClass().getResource("Black Skinhead.mp3");
        final Media media = new Media(resource.toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();

        // Dette lager vinduet.
        HBox toppBoks = lagToppBoks();
        rot = new BorderPane();
        rot.setTop(toppBoks);
        Scene scene = new Scene(rot);
        stage.setScene(scene);
        stage.setTitle("Labyrint");
        stage.show();
    }

    // Metoden setter bare instansvariabelen av Labyrint.
    public void settLabyrint(Labyrint labyrint) {
        this.labyrint = labyrint;
    }

    /* Lager HBox. Denne inneholder to knapper og et tekstfelt hvor adressen til
    filen du velger blir skrevet ned. */
    private HBox lagToppBoks() {
        Button velgFil = new Button("Velg fil...");
        TextField filFelt = new TextField();
        Button lag = new Button("Lag labyrint");


        velgFil.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Velg labyrintfil");
                File valgtFil = fileChooser.showOpenDialog(null);

                try {
                    labyrint = Labyrint.lesFraFil(valgtFil);
                    settLabyrint(labyrint);
                }
                catch(Exception e) {
                    System.out.println("Fil ikke lastet inn.");
                }

                if(valgtFil != null) {
                    filFelt.setText(valgtFil.getPath());
                }
            }
        });

        lag.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    int rader = labyrint.hentAntallRader();
                    int kolonner = labyrint.hentAntallKolonner();

                    rutenett = new GridPane();
                    ruter = new GUIRute[rader][kolonner];

                    for (int rad = 0; rad < rader; rad++) {
                        for (int kol = 0; kol < kolonner; kol++) {
                            ruter[rad][kol] = new GUIRute(kol, rad);
                            rutenett.add(ruter[rad][kol], kol, rad);
                        }
                    }

                    rot.setCenter(rutenett);
                    stage.sizeToScene();
                }
                catch(NullPointerException e) {
                    System.out.println("Du maa velge en fil!");
                }
            }
        });

        return new HBox(velgFil, filFelt, lag);
    }





// ****************************************************************************
    // Istedenfor aa lage egen klasse for GUIRute, lager jeg en indre klasse.
    private class GUIRute extends Pane {

        int kol, rad;
        Rute rute;
        boolean[][] utveiBool;

        public GUIRute(int kol, int rad) {
            this.kol = kol;
            this.rad = rad;
            rute = labyrint.ruteArray[rad][kol];

            setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    // Resetter fargen (utveisfargen)
                    resetRuter();
                    String korteste = null;

                    if(rute.tilTegn() == '.') {
                        try {
                            Liste<String> listeMedVeier = labyrint.finnUtveiFra(
                                                                  kol+1, rad+1);
                            // Finner korteste utvei.
                            for(String s : listeMedVeier) {
                                if(korteste == null) {
                                    korteste = s;
                                }
                                else if(korteste.length() > s.length()) {
                                    korteste = s;
                                }
                            }

                            utveiBool = losningStringTilTabell(korteste,
                                        labyrint.hentAntallKolonner(),
                                        labyrint.hentAntallRader());
                            fargUtvei();
                            System.out.println("Antall loesninger totalt: " +
                                              labyrint.hentListe().storrelse());
                        }
                        catch(NullPointerException e) {
                            System.out.println("Finnes ikke uveier fra denne" +
                                               " ruten.");
                        }
                    }
                    else if(rute.tilTegn() == '#') {
                        System.out.println("Svart rute.");
                    }
                }
            });

            oppdaterFarge();
            setMinHeight(720/labyrint.hentAntallRader());
            setMinWidth(720/labyrint.hentAntallRader());
            setBorder(new Border(new BorderStroke(Color.BLACK,
                                                  BorderStrokeStyle.SOLID,
                                                  CornerRadii.EMPTY,
                                                  new BorderWidths(1))));
        }

        /* Metode som resetter fargen til de hvite rutene for naar brukeren
        skal trykke paa en nyy rute. */
        public void resetRuter() {
            for(int r = 0; r < labyrint.hentAntallRader(); r++) {
                for(int k = 0; k < labyrint.hentAntallKolonner(); k++) {
                    if (labyrint.ruteArray[r][k].tilTegn() == '.') {
                        ruter[r][k].setBackground(new Background(
                        new BackgroundFill(
                        Color.WHITE, null, null)));
                    }
                }
            }
        }


        public void fargUtvei() {
            for (int r = 0; r < labyrint.hentAntallRader(); r++) {
                for (int k = 0; k < labyrint.hentAntallKolonner(); k++) {
                    if(utveiBool[r][k] == true) {
                        ruter[r][k].setBackground(new Background(
                                                  new BackgroundFill(
                                                  Color.RED, null, null)));
                    }
                }
            }
        }


        public void oppdaterFarge() {
            if(rute.tilTegn() == '.') {
                setBackground(new Background(new BackgroundFill(Color.WHITE,
                                                                null, null)));
            }
            else {
                setBackground(new Background(new BackgroundFill(Color.BLACK,
                                                                null, null)));
            }
        }
    }




    /**
     * Konverterer losning-String fra oblig 5 til en boolean[][]-representasjon
     * av losningstien.
     * @param losningString String-representasjon av utveien
     * @param bredde        bredde til labyrinten
     * @param hoyde         hoyde til labyrinten
     * @return              2D-representasjon av rutene der true indikerer at
     *                      ruten er en del av utveien.
     */
    public static boolean[][] losningStringTilTabell(String losningString,
                                                     int bredde, int hoyde) {
        boolean[][] losning = new boolean[hoyde][bredde];
        java.util.regex.Pattern p =
        java.util.regex.Pattern.compile("\\(([0-9]+),([0-9]+)\\)");
        java.util.regex.Matcher m =
        p.matcher(losningString.replaceAll("\\s",""));

        while(m.find()) {
            int x = Integer.parseInt(m.group(1))-1;
            int y = Integer.parseInt(m.group(2))-1;
            losning[y][x] = true;
        }

        return losning;
    }
}
