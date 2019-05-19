public class Pasient {

    private static int antallPasienter = 0;
    protected int id;
    private String navn;
    private long fodselsnummer;
    private String gateadresse;
    private int postnummer;
    private Stabel<Resept> stabel = new Stabel<Resept>();


    Pasient(String navn, long fodselsnummer, String gateadresse,
    int postnummer) {
        this.navn = navn;
        this.fodselsnummer = fodselsnummer;
        this.gateadresse = gateadresse;
        this.postnummer = postnummer;
        id = antallPasienter;
        antallPasienter++;
    }

    @Override
    public String toString() {
        return this.hentNavn() + " (" + this.fodselsnummer + ")";
    }

    public int hentId() {
        return id;
    }

    public String hentNavn() {
        return navn;
    }

    public long hentFodselsnummer() {
        return fodselsnummer;
    }

    public String hentGateadresse() {
        return gateadresse;
    }

    public int hentPostnummer() {
        return postnummer;
    }

    public void leggTilResept(Resept resept) {
        stabel.settInn(resept);
    }

    public Stabel<Resept> hentReseptliste() {
        return stabel;
    }

}
