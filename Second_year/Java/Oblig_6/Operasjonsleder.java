import java.util.*;
import java.io.*;
import java.util.concurrent.locks.*;
import krypto.*;

public class Operasjonsleder implements Runnable {

    private Monitor monitor;
    private Koe<Melding> dekrypterteMeldinger;
    private List<Melding> organisertListe;
    private List<Melding> fraKanalEn;
    private List<Melding> fraKanalTo;
    private List<Melding> fraKanalTre;




    public Operasjonsleder(Monitor monitor, Koe<Melding> dekrypterteMeldinger) {
        this.monitor = monitor;
        this.dekrypterteMeldinger = dekrypterteMeldinger;
        organisertListe = new ArrayList<Melding>();
    }



    @Override
    public void run() {
        monitor.hentOpMonitor().organiser(this);

        System.out.println("\n" + this + " ferdig!");
    }




    public void organiserMeldinger() {
        fraKanalEn = new ArrayList<Melding>();
        fraKanalTo = new ArrayList<Melding>();
        fraKanalTre = new ArrayList<Melding>();

        for(Melding m : dekrypterteMeldinger) {
            organisertListe.add(m);
        }

        // Bruker her en anonym klasse som sorterer meldingene.
        Collections.sort(organisertListe, new Comparator<Melding>() {
            @Override
            public int compare(Melding a, Melding b) {
                if(a.hentFraKanalID() == b.hentFraKanalID()) {
                    if(a.hentSekvensnummer() == b.hentSekvensnummer()) {
                        return 0;
                    }
                    else if(a.hentSekvensnummer() > b.hentSekvensnummer()){
                        return 1;
                    }
                    else {
                        return -1;
                    }
                }
                else if(a.hentFraKanalID() > b.hentFraKanalID()) {
                    return 1;
                }
                else {
                    return -1;
                }
            }
        });

        // Legger de sorterte meldingene i hver sin liste for hver kanal.
        for(Melding m : organisertListe) {
            if(m.hentFraKanalID() == 1) {
                fraKanalEn.add(m);
            }
            else if(m.hentFraKanalID() == 2) {
                fraKanalTo.add(m);
            }
            else if(m.hentFraKanalID() == 3) {
                fraKanalTre.add(m);
            }

            System.out.println(m.hentMelding());
        }

        try {
            skrivUtTilFiler();
        }
        catch(Exception e) {

        }
    }



    // Metdoe for aa skrive ut tekstene i filer.
    public void skrivUtTilFiler() throws Exception {
        File utfil = new File("fraKanalEn.txt");
        File utfil2 = new File("fraKanalTo.txt");
        File utfil3 = new File("fraKanalTre.txt");
        PrintWriter printWriter = new PrintWriter(utfil, "utf-8");
        PrintWriter printWriter2 = new PrintWriter(utfil2, "utf-8");
        PrintWriter printWriter3 = new PrintWriter(utfil3, "utf-8");

        for(Melding m : fraKanalEn) {
            printWriter.println(m.hentMelding() + "\n\n");
        }
        printWriter.close();

        for(Melding m : fraKanalTo) {
            printWriter2.println(m.hentMelding() + "\n\n");
        }
        printWriter2.close();

        for(Melding m : fraKanalTre) {
            printWriter3.println(m.hentMelding() + "\n\n");
        }
        printWriter3.close();
    }
}
