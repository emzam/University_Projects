public class Stabel<T> extends SuperForLenkelister<T> {

    // Tilkaller super sin konstruktoer.
    public Stabel() {
        super();
    }

    // Overrider settInn() metoden fra konstruktoer.
    @Override
    public void settInn(T element) {
        Node<T> ny = new Node<T>(element);

        ny.settInnMellom(hode, hode.hentNeste());
        storrelse++;
    }
}
