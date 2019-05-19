public class Personbil extends Fossilbil {

    // Forklaring for String ligger i Fossilbil klassen.
    protected String antallSeter;

    // Konstruktoer for klassen.
    public Personbil(String antallSeter, String utslipp, String kjennemerke) {
        super(utslipp, kjennemerke);
        this.antallSeter = antallSeter;
    }

    @Override
    public void skrivInfo() {
        System.out.println("Type motorvogn: Personbil");
        super.skrivInfo();
        System.out.println("Antall seter: " + antallSeter);
    }
}
