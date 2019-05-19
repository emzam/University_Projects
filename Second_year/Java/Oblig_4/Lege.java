public class Lege implements Comparable<Lege> {

    private String navn;
    private Koe<Resept> resept = new Koe<Resept>();

    public Lege(String navn){
        this.navn = navn;
    }

    public String hentNavn() {
        return navn;
    }

    public int compareTo(Lege annenLege) {
        return navn.compareTo(annenLege.hentNavn());
    }

    public void leggTilResept(Resept resept) {
        this.resept.settInn(resept);
    }

    public Koe<Resept> hentReseptliste() {
        return resept;
    }
}
