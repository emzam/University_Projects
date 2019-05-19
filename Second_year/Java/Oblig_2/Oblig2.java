import java.util.*;
import java.io.*;

// Testprogram for Oblig 2.
public class Oblig2 {

    public static void main(String[] emirZamwa) throws Exception {

        ArrayList<Bil> liste = new ArrayList<>();
        /* Filen blir lest inn fra terminalen, derfor "emirZamwa[0]". 0 fordi det er nullte plass i arrayen. 0'te plass starter etter java Oblig2 *HER*. */
        File fil = new File(emirZamwa[0]);
        Scanner inn = new Scanner(fil);

        // Whileloekke som skal lese inn fra fil og lage objekter.
        while(inn.hasNextLine()) {
            // Lager array av ordene i en linje i tekstfilen. Skiller med " ".
            String[] info = inn.nextLine().split(" ");

            /* Foerste if-setning er hvis linjen er for Elbil. Vet at Elbil Konstruktoeren bare tar inn 2 parametere. Pluss foerste ord i linjen ("EL") betyr at arrayen for Elbil linjene er paa lengde 3. */
            if(info.length == 3) {
                /* Arrayen info vet jeg da inneholder kjennemerke paa plass 1, og batteriKap paa plass 2. Lager saa et nytt Elbil objekt ut fra dette. Og putter deretter objektet i arraylisten "liste". */
                String kjennemerke = info[1];
                String batteriKap = info[2];
                Elbil nyElbil = new Elbil(batteriKap, kjennemerke);
                liste.add(nyElbil);
            }
            /* Denne if-setningen og neste er da for aa lage Personbil objekter og Lastebil objekter. Hvis info paa plass 1 er personbil, saa vil koden paa samme maate som forrige lage objekter ut fra infoen den faar inn. Det samme skjer for if-setningen for lastebiler. */
            else if(info[0].equalsIgnoreCase("PERSONBIL")) {
                String kjennemerke = info[1];
                String utslipp = info[2];
                String seter = info[3];
                Personbil nyPersonbil = new Personbil(seter, utslipp, kjennemerke);
                liste.add(nyPersonbil);
            }
            else if(info[0].equalsIgnoreCase("LASTEBIL")) {
                String kjennemerke = info[1];
                String utslipp = info[2];
                String vekt = info[3];
                Lastebil nyLastebil = new Lastebil(vekt, utslipp, kjennemerke);
                liste.add(nyLastebil);
            }
        }

        /* En try-catch kode for infoen som skrives ut i terminalen ut ifra hva brukeren taster inn. Try tester om brukeren skriver inn et filter, og skriver ut info ut ifra dette. Hvis brukeren skriver inn noe annet enn el og fossil, vil det bli gitt en tilbakemelding. */
        try {
            if(emirZamwa[1].equalsIgnoreCase("EL")) {
                for(Bil b : liste) {
                    if(b instanceof Elbil) {
                        b.skrivInfo();
                        System.out.println("");
                    }
                }
            }
            else if(emirZamwa[1].equalsIgnoreCase("FOSSIL")) {
                for(Bil b : liste) {
                    if(b instanceof Fossilbil) {
                        b.skrivInfo();
                        System.out.println("");
                    }
                }
            }
            else if(!emirZamwa[1].equalsIgnoreCase("EL") || !emirZamwa[1].equalsIgnoreCase("FOSSIL")) {
                System.out.println("Du maa enten skrive EL eller FOSSIL.");
            }
        }
        /* Hvis brukeren ikke skriver inn et filter, betyr det at det skal skrives ut info om alle Bil objektene i programmet slik oppgaven spoer om. I tillegg vil det komme en kompileringsfeil. Catch vil ta imot dette og heller skrive ut info om alle objektene. */
        catch(ArrayIndexOutOfBoundsException e) {
            for(Bil b : liste) {
                b.skrivInfo();
                System.out.println("");
            }
        }
    }
}
