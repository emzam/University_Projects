import java.util.*;
import java.util.concurrent.locks.*;
import krypto.*;


public class Monitor {

    private static int kryptoTeller = 0;
    private static int teleTeller = 0;
    private int antallTelegrafister;
    private int antallKryptografer;
    private Koe<Melding> kryptertListe;
    private Koe<Melding> dekryptertListe;

    OperasjonsMonitor operasjonsMonitor;

    private final Lock lock = new ReentrantLock();
    //private final Lock lock2 = new ReentrantLock();




    public Monitor(int antallTelegrafister, int antallKryptografer) {
        this.antallTelegrafister = antallTelegrafister;
        this.antallKryptografer = antallKryptografer;
        kryptertListe = new Koe<Melding>();
        dekryptertListe = new Koe<Melding>();
    }




    public boolean tomtForKrypterteMeldinger() {
        return kryptertListe.erTom();
    }



    /* Telegrafistene sin metode for aa legge til meldinger i monitoren.
    Baade denne metoden og noen metoder i Telegraf, Kryptograf og
    Operasjonsleder inneholder noen System.out.println(); Dette er bare for aa
    se i terminalen at programmet kjoerer og for aa gi med, og deg, en visuell
    oversikt over hva som skjer. */
    public void leggMelding(Melding melding, Telegrafist telegrafist) {
        lock.lock();

        try {
            System.out.println("Monitor mottar melding fra " + telegrafist + ".");
            kryptertListe.settInn(melding);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }



    // Noen par metoder brukt for noen tellere osv.
    public void telePluss() {
        teleTeller++;
    }

    public int hentTele() {
        return teleTeller;
    }

    public int hentAntallTelegrafister() {
        return antallTelegrafister;
    }

    public void kryptoPluss() {
        kryptoTeller++;
    }

    public int hentKrypto() {
        return kryptoTeller;
    }

    public int hentAntallKryptografer() {
        return antallKryptografer;
    }



    /* Kryptografene sin metode for aa hente ut melding fra monitoren og
    dekryptere. Som man kan se har jeg kommentert ut noen linjer i metoden.
    Jeg klarte ikke helt aa faa til programmet aa kjoere med conditions og
    slikt, men har gjort masse tweaks slik at programmet funker uten det. */
    public void sendMelding(Kryptograf kryptograf) {
        //lock2.lock();
        //try {
            //if(!tomtForKrypterteMeldinger()) {
                System.out.println(kryptograf + " henter melding og dekrypterer.");
                Melding m = kryptertListe.fjern();
                Melding nyMld = kryptograf.dekrypterMelding(m);
                dekryptertListe.settInn(nyMld);
            //}
        //}
        //catch(Exception e) {
        //   e.printStackTrace();
        //}
        //finally {
        //    lock2.unlock();
        //}
    }


// ----------------------------------------------------------------------
/* Koden under her er for, og bare for Operasjonslederen. Denne starter
automatisk naar alle kryptografene er ferdig. */

    public OperasjonsMonitor hentOpMonitor() {
        return operasjonsMonitor;
    }




    public void startOperasjonsMonitor() {
        operasjonsMonitor = new OperasjonsMonitor(this, dekryptertListe);
    }



    // Egen indre klasse for monitoren til Operasjonsleder.
    public class OperasjonsMonitor {

        private Operasjonsleder operasjonsleder;
        private Monitor monitor;

        private Lock lock = new ReentrantLock();



        public OperasjonsMonitor(Monitor monitor, Koe<Melding> dekrypterteMeldinger) {
            operasjonsleder = new Operasjonsleder(monitor, dekrypterteMeldinger);
            Thread t = new Thread(operasjonsleder);
            t.start();
        }




        public void organiser(Operasjonsleder op) {
            lock.lock();

            try {
                System.out.println(op + " henter meldinger og organiserer.\n");
                op.organiserMeldinger();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            finally {
                lock.unlock();
            }
        }
    }
}
