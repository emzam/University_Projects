public class HvitRute extends Rute {

    public HvitRute(Labyrint labyrint, int rad, int kolonne) {
        super(labyrint, rad, kolonne);
    }

    @Override
    public char tilTegn() {
        return '.';
    }
}
