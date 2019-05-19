import java.util.*;
import java.util.concurrent.locks.*;
import krypto.*;

public class Kryptograf implements Runnable {

    private Monitor monitor;




    public Kryptograf(Monitor monitor) {
        this.monitor = monitor;
    }


    /* Run-metoden til kryptografene. Denne kjoerer helt til alle kryptografene
    er ferdige, saa vil den starte operasjonslederen. */
    @Override
    public void run() {
        while(!monitor.tomtForKrypterteMeldinger()) {
            monitor.sendMelding(this);
        }

        monitor.kryptoPluss();
        System.out.println(this + " ferdig!\n");

        if(monitor.tomtForKrypterteMeldinger() && monitor.hentKrypto() == monitor.hentAntallKryptografer()) {
            monitor.startOperasjonsMonitor();
        }
    }




    public Melding dekrypterMelding(Melding melding) {
            Melding meldingen = melding;
            String m = meldingen.hentMelding();
            String dekrypterMld = Kryptografi.dekrypter(m);
            meldingen.erstattMelding(dekrypterMld);
            return meldingen;
    }
}
