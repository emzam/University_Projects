import java.util.Iterator;

public class Legeliste extends OrdnetLenkeliste<Lege> {

    private String[] arrayNavn;

    /**
    * Soeker gjennom listen etter en lege med samme navn som ‘navn‘
    * og returnerer legen (uten aa fjerne den fra listen).
    * Hvis ingen slik lege finnes, returneres ‘null‘.
    * @param navn navnet til legen
    * @return legen
    */
    public Lege finnLege(String navn) {
        for(Lege l : this) {
            if(l.hentNavn().equalsIgnoreCase(navn)) {
                return l;
            }
        }
        return null;
    }

    /**
    * Returnerer et String[] med navnene til alle legene i listen
    * i samme rekkefoelge som de staar i listen.
    * @return array med navn til alle legene
    */
    public String[] stringArrayMedNavn() {
        arrayNavn = new String[storrelse];
        int teller = 0;

        for(Lege l : this) {
            arrayNavn[teller] = l.hentNavn();
            teller++;
        }

        return arrayNavn;
    }
}
