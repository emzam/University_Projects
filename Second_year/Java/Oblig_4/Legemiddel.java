public class Legemiddel {

    protected String navn;
    protected static int idLegemidler = 0;
    protected int id;
    protected double pris;
    protected double virkestoff;

    public Legemiddel(String navn, double pris, double virkestoff) {
        this.navn = navn;
        this.pris = pris;
        this.virkestoff = virkestoff;
        id = idLegemidler;
        idLegemidler++;
    }

    @Override
    public String toString() {
        return this.hentNavn();
    }

    public int hentId() {
        return id;
    }

    public String hentNavn() {
        return navn;
    }

    public double hentPris() {
        return pris;
    }

    public double hentVirkestoff() {
        return virkestoff;
    }
}
