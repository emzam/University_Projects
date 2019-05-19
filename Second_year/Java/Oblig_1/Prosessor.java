public class Prosessor {

    // instansvariablenefor klassen.
    private int antallKjerner;
    private double klokkeHastighet;

    // Konstruktoer for prosessor som setter x kjerner og x hastighet.
    public Prosessor(int antallKjerner, double klokkeHastighet) {
        this.antallKjerner = antallKjerner;
        this.klokkeHastighet = klokkeHastighet;
    }

    // Metode som regner ut FLOPS for prosessoren.
    public double flops() {
        double floppser = (antallKjerner*klokkeHastighet*8);
        return floppser;
    }
}
