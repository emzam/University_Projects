public class Node {

    // instansvariablene for klassen.
    private int minneStr;
    private Prosessor prosessor1;
    private Prosessor prosessor2;

    // Konstruktoer 1 for noden. Denne med en prosessor.
    public Node(int minneStr, int antallKjerner, double klokkeHastighet) {
        this.minneStr = minneStr;
        prosessor1 = new Prosessor(antallKjerner, klokkeHastighet);
    }

    // Konstruktoer 2 for noden, dette tilfellet med to prosessorer.
    public Node(int minneStr, int antallKjerner1, double klokkeHastighet1, int antallKjerner2, double klokkeHastighet2) {
        this.minneStr = minneStr;
        prosessor1 = new Prosessor(antallKjerner1, klokkeHastighet1);
        prosessor2 = new Prosessor(antallKjerner2, klokkeHastighet2);
    }

    // Regner ut FLOPS for begge prosessorene.
    public double flops() {
        double floppser;
        floppser = (prosessor1.flops() + prosessor2.flops());
        return floppser;
    }

    // Boolean metode som skjekker om minnestoerrelsen er stoerre enn paakrevd minne. Returnerer true om minneStr er stoerre.
    public boolean nokMinne(int paakrevdMinne) {
        return (minneStr >= paakrevdMinne);
    }
}
