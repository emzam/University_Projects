public class OrdnetLenkeliste<T extends Comparable<T>> extends SuperForLenkelister<T> {

    public OrdnetLenkeliste() {
        super();
    }

    // Overrider settInn() fra superklassen. I tillegg tilkaller den en ekstra metode jeg har laget.
    @Override
    public void settInn(T element) {
        Node<T> ny = new Node<T>(element);

        finnRiktigPlass(ny);
        storrelse++;
    }

    // Denne metoden finner riktig plass i lenkelisten, slik at naar noden blir satt inn, blir den satt inn i en sortert rekkefoelge.
    public void finnRiktigPlass(Node<T> ny) {
        T t = ny.hentInnhold();
        Node<T> denne = hode;

        while((denne = denne.hentNeste()) != hale) {
            if(t.compareTo(denne.hentInnhold()) <= 0) {
                break;
            }
        }

        ny.settInnMellom(denne.hentForrige(), denne);
    }
}
