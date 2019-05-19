import java.util.*;
import java.io.*;

// Main programmet som tar for seg skriving i terminal.
public class Main {
    public static void main(String[] args) throws Exception {

        BinaryTree bt = new BinaryTree();
        bt.readFile("dictionary.txt");

        Scanner scan = new Scanner(System.in);

        while(true) {
            System.out.print("Søk (q for å avslutte): ");
            String linje = scan.nextLine();
            if(linje.equalsIgnoreCase("q")) {
                bt.printInfo();
                System.out.println("Programmet avsluttes.");
                break;
            }
            else {
                bt.spellcheck(linje);
                System.out.println("");
            }
        }
    }
}
