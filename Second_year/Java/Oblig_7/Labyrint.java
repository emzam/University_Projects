import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class Labyrint {

    public Rute[][] ruteArray;
    private int antallRader, antallKolonner;
    private Koe<String> liste;


    public int hentAntallRader() {
        return antallRader;
    }

    public int hentAntallKolonner() {
        return antallKolonner;
    }


    private Labyrint(int antallRader, int antallKolonner) {
        this.antallRader = antallRader;
        this.antallKolonner = antallKolonner;
        ruteArray = new Rute[antallRader][antallKolonner];
    }


    @Override
    public String toString() {
        String arrayString = "";

        for(int r = 0; r < antallRader; r++) {
            for(int k = 0; k < antallKolonner; k++) {
                arrayString += " " + ruteArray[r][k].tilTegn();
            }
            arrayString += "\n\r";
        }
        return arrayString;
    }


    public static Labyrint lesFraFil(File fil) throws FileNotFoundException {
        Scanner in = new Scanner(fil);

        int rader = Integer.parseInt(in.next());
        int kolonner = Integer.parseInt(in.next());
        Labyrint labyrint = new Labyrint(rader, kolonner);
        int rad = 0;

        /* Her brukte jeg foerst in.hasNextLine() i while-loekken og
        in.nextLine(). Dette funket ikke, saa jeg byttet til in.hasNext() og
        in.next(). Da funket det. Skjoenner ikke helt hvorfor det ikke funket
        med det foerstnevnte, men med det sistnevnte. */
        while(in.hasNext()) {
            char linje[] = in.next().toCharArray();

            for(int k = 0; k < linje.length; k++) {
                if(linje[k] == '#') {
                    SortRute nySort = new SortRute(labyrint, rad, k);
                    labyrint.ruteArray[rad][k] = nySort;
                }

                if(linje[k] == '.') {
                    if(rad == 0 || k == 0 || rad == rader-1 || k == kolonner-1) {
                        Aapning nyAapning = new Aapning(labyrint, rad, k);
                        labyrint.ruteArray[rad][k] = nyAapning;
                    }
                    else {
                        HvitRute nyHvit = new HvitRute(labyrint, rad, k);
                        labyrint.ruteArray[rad][k] = nyHvit;
                    }
                }
            }
            rad++;
        }

        labyrint.settNaboer();
        return labyrint;
    }


    public void settNaboer() {
        for(int r = 0; r < antallRader-1; r++) {
            for(int k = 0; k < antallKolonner-1; k++) {
                if(r != 0) {
                    ruteArray[r][k].settNord(ruteArray[r-1][k]);
                }

                if(r != antallRader) {
                    ruteArray[r][k].settSoer(ruteArray[r+1][k]);
                }

                if(k != 0) {
                    ruteArray[r][k].settVest(ruteArray[r][k-1]);
                }

                if(k != antallKolonner) {
                    ruteArray[r][k].settOest(ruteArray[r][k+1]);
                }
            }
        }
    }


    public Koe<String> finnUtveiFra(int kol, int rad) {
        liste = new Koe<String>();

        if(kol > antallKolonner || rad > antallRader) {
            System.out.println("Labyrinten har ikke saa mange rader/kolonner.");
            System.exit(0);
        }

        if(kol < 1 || rad < 1) {
            System.out.println("Koordinatene maa vaere minst (1, 1).");
            System.exit(0);
        }

        ruteArray[rad-1][kol-1].finnUtvei();
        return liste;
    }

    //Skjoenner ikke denne metoden. Programmet funker helt fint uten.
    public void settMinimalUtskrift() {}


    public Koe<String> hentListe() {
        return liste;
    }
}
