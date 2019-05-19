public class Bil {

    private String kjennemerke;

    // Konstruktoer for klassen.
    public Bil(String kjennemerke) {
        this.kjennemerke = kjennemerke;
    }

    public void skrivInfo() {
        System.out.println("Reg. nummer: " + kjennemerke);
    }
}
