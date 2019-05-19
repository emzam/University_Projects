public class Melding {

    private String melding;
    private int sekvensnummer;
    private int fraKanalID;


    public Melding(String melding, int fraKanalID, int sekvensnummer) {
        this.melding = melding;
        this.fraKanalID = fraKanalID;
        this.sekvensnummer = sekvensnummer;
    }


    public void erstattMelding(String melding) {
        this.melding = melding;
    }


    public String hentMelding() {
        return melding;
    }


    public int hentSekvensnummer() {
        return sekvensnummer;
    }


    public int hentFraKanalID() {
        return fraKanalID;
    }
}
