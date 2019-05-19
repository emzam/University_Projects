import java.util.*;

// Superklasse for alle beholderne. Stabel, Koe og OrdnetLenkeliste.
public abstract class SuperForLenkelister<T> implements Liste<T> {
    protected int storrelse = 0;
    protected Node<T> hode;
    protected Node<T> hale;

    // Konstruktoer for klassen. Lager en Node for hode, og hale (uten innhold). Disse er bare til aa alltid vite hvor starten og slutten av listen er.
    public SuperForLenkelister() {
        hode = new Node<T>(null);
        hale = new Node<T>(null);
        hode.settNeste(hale);
        hale.settForrige(hode);
    }

    // Returnerer stoerrelsen.
    public int storrelse() {
        return storrelse;
    }

    // Sjekker om stoerrelse er oekt.
    public boolean erTom() {
        return (storrelse == 0);
    }

    // Abstrakt metode som @Overrides i subklassene. Dette fordi den ikke er lik for alle.
    public abstract void settInn(T element);

    // Fjerner en node, og samtidig returnerer innholdet <T>.
    public T fjern() {
        if(erTom()) {
            return null;
        }

        Node<T> forrest = this.hode.hentNeste();
        forrest.kobleUt();
        storrelse--;

        return forrest.hentInnhold();
    }



    // Implementerer iterator.
    public Iterator<T> iterator() {
		return new LenkelisterIterator();
	}

	public class LenkelisterIterator implements Iterator<T>{

		private Node<T> posisjon;

		public LenkelisterIterator(){
			posisjon = hode.hentNeste();
		}

		public boolean hasNext(){
			return (posisjon != hale);
		}

		public T next(){
            if(hasNext()) {
                T denne = posisjon.hentInnhold();
                posisjon = posisjon.hentNeste();
                return denne;
            }
            throw new NoSuchElementException();
		}

		public void remove(){
            System.out.println("Denne metoden finnes ikke i denne iteratoren.");
		}
	}
}
