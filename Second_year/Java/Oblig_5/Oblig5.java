import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Oblig5 {
    public static void main(String[] args) {
        String filnavn = null;

        if (args.length > 0) {
            filnavn = args[0];
        } else {
            System.out.println("FEIL! Riktig bruk: "
                               +"java Oblig5 <labyrintfil>");
            return;
        }
        File fil = new File(filnavn);
        Labyrint l = null;
        try {
            l = Labyrint.lesFraFil(fil);
            //Litt tekst. Syns det ser bedre ut.
            System.out.print(l.toString());
            System.out.println("Skriv inn koordinater: x y");
        } catch (FileNotFoundException e) {
            System.out.printf("FEIL: Kunne ikke lese fra '%s'\n", filnavn);
            System.exit(1);
        }
        l.settMinimalUtskrift();

        // les start-koordinater fra standard input
        Scanner inn = new Scanner(System.in);
        while (inn.hasNextLine()) {
            String[] ord = inn.nextLine().split(" ");
            int startKol = Integer.parseInt(ord[0]);
            int startRad = Integer.parseInt(ord[1]);
            Liste<String> utveier = l.finnUtveiFra(startKol, startRad);
            if (!utveier.erTom()) {
                for (String s : utveier) {
                    System.out.println(s);
                }
            } else {
                System.out.println("Ingen utveier.");
            }
            System.out.println();
        }
    }
}
