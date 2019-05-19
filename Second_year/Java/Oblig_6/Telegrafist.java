import java.util.*;
import java.util.concurrent.locks.*;
import krypto.*;

public class Telegrafist implements Runnable {

    private int teller = 0;
    private Monitor monitor;
    private Kanal kanal;




    public Telegrafist(Monitor monitor, Kanal kanal) {
        this.monitor = monitor;
        this.kanal = kanal;
    }


    /* Run-metoden til telegrafist. Denne kjoerer til alle telegrafistene er
    ferdige, saa vil den starte traadene til kryptografene. */
    @Override
    public void run() {
        String s = kanal.lytt();

        while(s != null) {
            monitor.leggMelding(lyttTilMelding(s), this);
            s = kanal.lytt();
        }

        monitor.telePluss();
        System.out.println(this + " ferdig!\n");

        if(monitor.hentTele() == monitor.hentAntallTelegrafister()) {
            for(int i = 0; i < monitor.hentAntallKryptografer(); i++) {
                Kryptograf k = new Kryptograf(monitor);
                Thread v = new Thread(k);
                v.start();
            }
        }
    }




    public Melding lyttTilMelding(String s) {
        Melding melding = new Melding(s, kanal.hentId(), teller);
        teller++;
        return melding;
    }
}
