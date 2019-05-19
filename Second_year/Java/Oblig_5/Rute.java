public abstract class Rute {

    private int rad, kolonne;
    private Labyrint labyrint;
    private Rute nord, soer, oest, vest;


    public Rute(Labyrint labyrint, int rad, int kolonne) {
        this.labyrint = labyrint;
        this.rad = rad;
        this.kolonne = kolonne;
    }


    public void settNord(Rute nord) {
        this.nord = nord;
    }


    public void settSoer(Rute soer) {
        this.soer = soer;
    }


    public void settOest(Rute oest) {
        this.oest = oest;
    }


    public void settVest(Rute vest) {
        this.vest = vest;
    }


    public abstract char tilTegn();


    public Rute hentNord() {
        return nord;
    }


    public Rute hentSoer() {
        return soer;
    }


    public Rute hentOest() {
        return oest;
    }


    public Rute hentVest() {
        return vest;
    }


    @Override
    public String toString() {
        String koordinat = String.format("(%d, %d)", kolonne+1, rad+1);
        return koordinat;
    }


    public void gaa(Rute denneRuta, String vei) {
        if(vei.equals("")) {
            vei += toString();
        }
        else {
            vei += " --> " + toString();
        }

        if(this instanceof Aapning) {
            vei += (" AAPNING!\n");
            labyrint.hentListe().settInn(vei);
            return;
        }

        if(denneRuta instanceof SortRute) {
            System.out.println("Sort rute");
            return;
        }

        if(hentNord() instanceof HvitRute && hentNord() != denneRuta) {
            hentNord().gaa(this, vei);
        }

        if(hentSoer() instanceof HvitRute && hentSoer() != denneRuta) {
            hentSoer().gaa(this, vei);
        }

        if(hentOest() instanceof HvitRute && hentOest() != denneRuta) {
            hentOest().gaa(this, vei);
        }

        if(hentVest() instanceof HvitRute && hentVest() != denneRuta) {
            hentVest().gaa(this, vei);
        }
    }


    public void finnUtvei() {
        String vei = "";
        gaa(this, vei);
    }
}
