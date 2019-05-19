public abstract class Resept {

    private static int antallResepter = 0;
    protected int id;
    protected int pasientId;
    protected Legemiddel legemiddel;
    protected Lege lege;
    protected int reit;

    public Resept(Legemiddel legemiddel, Lege utskrivendeLege, int pasientId, int reit) {
        this.legemiddel = legemiddel;
        this.lege = utskrivendeLege;
        this.pasientId = pasientId;
        this.reit = reit;
        id = antallResepter;
        antallResepter++;
    }

    public String toString() {
        return legemiddel.hentNavn();
    }

    public int hentId() {
        return id;
    }

    public Legemiddel hentLegemiddel() {
        return legemiddel;
    }

    public Lege hentLege() {
        return lege;
    }

    public int hentPasientId() {
        return pasientId;
    }

    public int hentReit() {
        return reit;
    }

    /**
    * Bruker resepten én gang. Returner false om resepten er
    * oppbrukt, ellers returnerer den true.
    * @return om resepten kunne brukes
    */
    public boolean bruk() {
        if(reit > 0) {
            reit--;
            return true;
        }
        return false;
    }

    /**
    * Returnerer reseptens farge. Enten "blaa" eller "hvit".
    * @return reseptens farge
    */
    abstract public String farge();

    /**
    * Returnerer prisen pasienten maa betale.
    * @return prisen pasienten maa betale
    */
    abstract public double prisAaBetale();
}
