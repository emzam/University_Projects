public class Main {
    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.print("* SKRIV MED FILER! *");
        }
        else {
            Oblig4 test = new Oblig4();
            test.read(args[0], args[1]);
        }
    }
}
