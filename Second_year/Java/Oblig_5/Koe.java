public class Koe<T> extends SuperForLenkelister<T> {

    // Tilkaller super sin konstruktoer.
    public Koe() {
        super();
    }

    // Overrider settInn() metoden fra superklassen.
    @Override
    public void settInn(T element) {
        Node<T> ny = new Node<T>(element);

        ny.settInnMellom(hale.hentForrige(), hale);
        storrelse++;
    }
}
