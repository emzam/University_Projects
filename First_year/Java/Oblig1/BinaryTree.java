import java.io.*;
import java.util.*;

public class BinaryTree {
    Node root;
    static int total = 1;

    // Metode for aa lese fil og lage tre ut fra ordene.
    public void readFile(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            // Setter foerste ordet i listen lik rotnoden.
            try {
                String firstWord = scanner.nextLine();
                root = new Node(firstWord);
                root.setDepth(0);
            }
            catch(NoSuchElementException e) {
                System.out.println("*FILEN DU BRUKER INNEHOLDER INGEN ORD*");
            }

            // Leser resten av ordene i listen.
            while(scanner.hasNextLine()) {
                String word = scanner.nextLine();
                Node node = new Node(word);
                root.insert(node);
                total++;

            }
        }
        catch(FileNotFoundException e) {
            System.out.printf("*FANT IKKE FILEN*\n");
        }
    }



    // Boolean metode som bruker rotnoden som utgangspunkt i hvert soek.
    public boolean rootsearch(String s) {
        if(root == null) {
            return false;
        }
        if(root.search(s)) {
            return true;
        }
        else {
            return false;
        }
    }



    // Metode for alle de forskjellige spell-checkene.
    public void spellcheck(String s) {
        String word = s.toLowerCase();
        char[] words = word.toCharArray();

        if(rootsearch(word)) {
            System.out.printf("Det du soekte paa, \"%s\", finnes!\n", word);
        }
        else {
            System.out.printf("\"%s\" finnes ikke!\nUnder er det vist " +
                   "lignende ord (om det er tomt, ligner ingen ord):\n", word);

            String wordString = new String(words);
            switchLetter(wordString);
            replaceLetter(wordString);
            addLetter(wordString);
            removeLetter(wordString);
        }
    }

    // Foerste spellcheck som switcher to bokstaver ved siden av hverandre.
    public void switchLetter(String s) {
        for(int i=0; i<s.length()-1; i++) {
            char[] word = s.toCharArray();
            char temp = word[i];
            word[i] = word[i+1];
            word[i+1] = temp;

            String wordString = new String(word);
            if(rootsearch(wordString)) {
                System.out.println("- " + wordString);
            }
        }
    }

    // Andre spellcheck som erstatter en bokstav med en annen.
    public void replaceLetter(String s) {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        for (int i=0; i<s.length(); i++) {
            for(int j=0; j<alphabet.length; j++) {
                char[] word = s.toCharArray();
                word[i] = alphabet[j];

                String wordString = new String(word);
                if(rootsearch(wordString)) {
                    System.out.println("- " + wordString);
                }
            }
        }
    }

    // Tredje spellcheck som legger til bokstaver i ordet.
    public void addLetter(String s) {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        for (int i=0; i<s.length()+1; i++) {
            for(int j=0; j<alphabet.length; j++) {
                StringBuilder ns = new StringBuilder(s);
                ns.insert(i, alphabet[j]);

                String wordString = new String(ns);
                if(rootsearch(wordString)) {
                    System.out.println("- " + wordString);
                }
            }
        }
    }

    // Fjerde spellcheck som fjerner en bokstav fra ordet.
    public void removeLetter(String s) {
        for(int i=0; i<s.length(); i++) {
            StringBuilder word = new StringBuilder(s);
            word.deleteCharAt(i);

            String wordString = new String(word);
            if(rootsearch(wordString)) {
                System.out.println("- " + wordString);
                // Return slik at det ikke blir duplikat i terminalen.
                return;
            }
        }
    }



    // Printer ut dybden av treet.
    public int maxDepth(Node n) {
        if(n == null) {
            return 0;
        }
        else {
            // Regner ut dybden i hvert subtre.
            int leftDepth = maxDepth(n.getLeft());
            int rightDepth = maxDepth(n.getRight());
            // Bruker den stoerste.
            if(leftDepth > rightDepth) {
                return leftDepth + 1;
            }
            else {
                return rightDepth + 1;
            }
        }
    }

    // Denne metoden lager en array av max dybde i treet.
    int[] depth;
    public int[] nodesPerDepth() {
        depth = new int[maxDepth(root)];
        for(int i = 0; i < depth.length; i++) {
            depth[i] = 0;
        }
        nodesPerDepth(root);
        return depth;
    }
    // Regner ut antall noder i hver dybde.
    private void nodesPerDepth(Node n) {
        depth[n.getDepth()]++;

        if(n.getLeft() != null) {
            nodesPerDepth(n.getLeft());
        }
        if(n.getRight() != null) {
            nodesPerDepth(n.getRight());
        }
    }

    // Gjennomsnittlig dybde av alle nodene.
    public double averageDepth() {
        double sum = 0;

        for(int i=0; i<depth.length; i++) {
            sum += i*depth[i];
        }

        double gj = (sum/total);
        double roundOff = Math.round(gj * 10000.0) / 10000.0;
        return roundOff;
    }

    // Finner foerste ordet i treet (alfabetisk).
    public void firstWord(Node n) {
        if(n.getLeft() != null) {
            firstWord(n.getLeft());
        }
        else {
            System.out.println("\nFørste ordet er: " + n.getValue());
        }
    }
    // Finner siste ordet i treet (alfabetisk).
    public void lastWord(Node n) {
        if(n.getRight() != null) {
            lastWord(n.getRight());
        }
        else {
            System.out.println("Siste ordet er: " + n.getValue());
        }
    }



    // Printer ut all info.
    public void printInfo() {
        // maxDepth(root) - 1 fordi rotnoden ikke telles.
        System.out.println("\nMax dybde i treet: " +(maxDepth(root)-1)+ "\n");

        nodesPerDepth();
        for (int i = 0; i<depth.length; i++) {
            System.out.println("Dybde " + i + ": " + depth[i] + " noder");
        }

        System.out.println("\nGjennomsnittlige dybden av alt i treet " +
                           "(avrundet) er: " + averageDepth());

        firstWord(root);
        lastWord(root);
    }




    // Printer alle ordene i treet sortert (brukt i tester).
    public void printTree(Node n) {
        if(n == null) {
            return;
        }

        printTree(n.getLeft());
        System.out.println(n.getValue());
        printTree(n.getRight());
    }
}
