// Ting som maa importeres
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

// ****************************************************************************
// Start
public class Oblig4 {
    static int CHAR_MAX = 256;
    ArrayList<Integer> solutions = new ArrayList<Integer>();
    char[] needle, haystack;

// ****************************************************************************
    // Read metode som tar inn filnavnene og setter needle og haystack char[]
    public void read(String needle, String haystack) {
        try {
            // Leser de forskjellige filene.
            File fileN = new File(needle);
            File fileH = new File(haystack);
            Scanner scannerN = new Scanner(fileN);
            Scanner scannerH = new Scanner(fileH);

            String n = "", h = "";
            while(scannerN.hasNextLine()) {
                n += scannerN.nextLine();
            }
            while(scannerH.hasNextLine()) {
                h += scannerH.nextLine();
            }

            // Sjekker om filene inneholder ord eller ikke.
            if(n == "" || h == "") {
                System.out.println("* EN AV FILENE INNEHOLDER INGEN ORD! *");
                return;
            }

            this.needle = n.toCharArray();
            this.haystack = h.toCharArray();

            System.out.println("Needle: " + n);
            System.out.println("Haystack: " + h);
            System.out.println("");
            // Kaller paa metoden.
            solutions = boyer_moore_horspool(this.needle, this.haystack);

            if(solutions.isEmpty()) {
                System.out.println("Ingen loesninger.");
            } else {
                for(int i : solutions) {
                    System.out.printf("Loesning paa index: %s\n", i);
                    System.out.printf("Text: %s\n\n",
                                       h.substring(i, (i + n.length())));
                }
            }
        }
        catch(FileNotFoundException e) {
            System.out.println("* FANT IKKE FILEN! *");
            System.exit(1);
        }
    }

// ****************************************************************************
    // boyer_moore_horspool metoden som returnerer en ArrayList med loesninger.
    public ArrayList<Integer> boyer_moore_horspool(char[] needle,
                                                   char[] haystack){
        /* Denne metoden returnerer en liste med alle loesninger. Dette
           gjorde det enklere aa lagre de forskjellige funnene, som til slutt
           blir printet ut i read() metoden. */
        ArrayList<Integer> list = new ArrayList<Integer>();
        char wildcard = '_';
        int lastWCshift = -1, lastWCindex = -1;

        if(needle.length > haystack.length) {
            return null;
        }

        /* Her velges den siste indexen til der en evt wildcard ligger. Samt
           ogsaa en skiftlengde hvis det finnes en wildcard. Denne skiftlengden
           er paa samme lengde som der _ forekommer til slutten av char[]. */
        for(int i = needle.length - 2; i >= 0; i--) {
            if(needle[i] == wildcard) {
                lastWCindex = i;
                lastWCshift = (needle.length - 1) - i;
                break;
            }
        }


        int[] bad_shift = new int[CHAR_MAX]; // 256

        /* En sjekk; hvis lastWCshift == -1, betyr det at det ikke er noen
           wildcards i needle filen. Dermed vil bad_shift lengden vaere det
           samme som needle lengden. Hvis det derimot finnes en wildcard, vil
           bad_shift vaere paa samme lengde som lastWCshift. */
        if(lastWCshift == -1) {
            for(int i = 0; i < CHAR_MAX; i++) {
                bad_shift[i] = needle.length;
            }
        }
        else {
            for(int i = 0; i < CHAR_MAX; i++) {
                bad_shift[i] = lastWCshift;
            }
        }

        int offset = 0, scan = 0;
        int last = needle.length - 1;
        int maxoffset = haystack.length - needle.length;

        /* Samme prinsipp gjelder i if-else koden under, som beskrevet over.
           Bare at her saa sjekkes noe annet. Hvis en wildcard ikke finnes, vil
           alle bad_shift'ene fra i = 0 og oppover bli satt slik som vist.
           Hvis en wildcard finnes derimot, maa en annen forloekke starte paa
           indexen til der wildcard forekommer. */
        if(lastWCshift == -1) {
            for(int i = 0; i < last; i++) {
                bad_shift[needle[i]] = last - i;
            }
        }
        else {
            for(int i = lastWCindex; i < last; i++) {
                bad_shift[needle[i]] = last - i;
            }
        }

        // Her legges loesningene i arraylisten.
        while(offset <= maxoffset) {
            for(scan = last; needle[scan] == haystack[scan+offset] ||
                             needle[scan] == wildcard; scan--) {
                if(scan == 0) { // match found!
                    list.add(offset);
                    break;
                }
            }
            offset += bad_shift[haystack[offset + last]];
        }

        return list;
    }

// ****************************************************************************
// End
}
