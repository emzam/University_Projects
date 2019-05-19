public class Fossilbil extends Bil {

    /* Jeg har valgt aa bruke String istedenfor int eller double for forskjellige variabler for de forskjellige klassene. Dette fordi ingen av disse verdiene skulle bli regnet paa eller brukt som tall, men mer som bare info for bilene. Dermed ble det lettere for aa kode ogsaa. */
    protected String utslipp;

    // Konstruktoer for klassen.
    public Fossilbil(String utslipp, String kjennemerke) {
        super(kjennemerke);
        this.utslipp = utslipp;
    }

    @Override
    public void skrivInfo() {
        super.skrivInfo();
        System.out.println("CO2-utslipp: " + utslipp + " g/km");
    }
}
