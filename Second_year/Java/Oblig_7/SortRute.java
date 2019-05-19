public class SortRute extends Rute {

    public SortRute(Labyrint labyrint, int rad, int kolonne) {
        super(labyrint, rad, kolonne);
    }

    @Override
    public char tilTegn() {
        return '#';
    }
}
