public class Main {
    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("* SKRIV MED EN FIL! *");
        }
        else {
            String file = args[0];
            Graph g = new Graph();
            g.readFile(file);
        }
    }
}
