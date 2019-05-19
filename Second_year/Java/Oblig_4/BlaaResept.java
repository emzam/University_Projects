public class BlaaResept extends Resept {

    private final double rabattPris = 0.25;

    public BlaaResept(Legemiddel legemiddel, Lege utskrivendeLege, int pasientId, int reit) {
        super(legemiddel, utskrivendeLege, pasientId, reit);
    }

    @Override
    public String farge() {
        return "blaa";
    }

    @Override
    public double prisAaBetale() {
        return (legemiddel.hentPris() * rabattPris);
    }

    @Override
    public String toString() {
        return "Blå resept";
    }
}
