import java.util.*;

public class StatiskTabell<T> implements Tabell<T> {

    private int arrayLengde;
    private T[] tabell;
    private int stoerrelse = 0;

    public StatiskTabell(int arrayLengde){
        this.arrayLengde = arrayLengde;
        this.tabell = (T[]) new Object[arrayLengde];
    }

    /**
     * Beregner antall elementer i tabellen
     * @return      antall elementer i tabellen
     */
    public int storrelse() {
        return stoerrelse;
    }

    /**
     * Sjekker om tabellen er tom
     * @return      om tabellen er tom
     */
    public boolean erTom(){
        return (tabell[0] == null);
    }


    /**
     * Setter inn et element i tabellen
     * @param   element             elementet som settes inn
     * @throws  FullTabellUnntak    hvis tabellen allerede er full
     */
    public void settInn(T element) {
        boolean sattInn = false;
        for(int i = 0; i < arrayLengde; i++) {
            if(tabell[i] == null) {
                tabell[i] = element;
                stoerrelse++;
                sattInn = true;
                break;
            }
        }

        if(!sattInn) {
            throw new FullTabellUnntak(arrayLengde);
        }
    }

    /**
     * Henter (uten aa fjerne) et element fra tabellen
     * @param  plass    plassen i tabellen som det hentes fra
     * @return          elementet paa plassen
     * @throws  UgyldigPlassUnntak  hvis plassen ikke er en gyldig
                                    indeks i arrayet eller plassen
                                    ikke inneholder noe element
     */
    public T hentFraPlass(int plass) {
        if(plass >= 0 && plass < arrayLengde && tabell[plass] != null) {
            return tabell[plass];
        }
        else {
            throw new UgyldigPlassUnntak(plass, arrayLengde);
        }
    }


    // Implementerer iterator.
    public Iterator iterator() {
        return new StatiskTabellIterator();
    }

    private class StatiskTabellIterator implements Iterator<T> {
        private int posisjon = 0;

        public boolean hasNext() {
            return (posisjon < storrelse());
        }

        public T next() {
            if(hasNext()) {
                return tabell[posisjon++];
            }
            else {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            System.out.println("Denne metoden finnes ikke i denne iteratoren.");
        }
    }
}
