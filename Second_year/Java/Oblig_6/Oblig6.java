import krypto.*;

public class Oblig6 {

    // Her velges det antall telegrafister og kryptografer.
    private static final int antallTelegrafister = 3;
    private static final int ANTALL_KRYPTOGRAFER = 20;

    public static void main(String[] args) {

        Monitor monitor = new Monitor(antallTelegrafister, ANTALL_KRYPTOGRAFER);
        Operasjonssentral ops = new Operasjonssentral(antallTelegrafister);
        Kanal[] kanaler = ops.hentKanalArray();

        /* Mitt program trenger bare aa starte traadene til telegrafistene,
        saa vil resten av traadene starte automatisk. */
        for(int i = 0; i < antallTelegrafister; i++) {
            Telegrafist t = new Telegrafist(monitor, kanaler[i]);
            Thread u = new Thread(t);
            u.start();
        }

        // for(int i = 0; i < ANTALL_KRYPTOGRAFER; i++) {
        //     Kryptograf k = new Kryptograf(monitor);
        //     Thread v = new Thread(k);
        //     v.start();
        // }
    }
}
