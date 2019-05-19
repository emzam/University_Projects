public class Lastebil extends Fossilbil {

    // Forklaring for String ligger i Fossilbil klassen.
    protected String nytteVekt;

    // Konstruktoer for klassen.
    public Lastebil(String nytteVekt, String utslipp, String kjennemerke) {
        super(utslipp, kjennemerke);
        this.nytteVekt = nytteVekt;
    }

    @Override
    public void skrivInfo() {
        System.out.println("Type motorvogn: Lastebil");
        super.skrivInfo();
        System.out.println("Nyttevekt: " + nytteVekt + " kg");
    }
}
