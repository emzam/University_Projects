import java.util.*;
import java.io.*;

public class Oblig5test {
    public static void main(String[] args) throws Exception {
        File fil = new File("3.txt");
        Labyrint test = Labyrint.lesFraFil(fil);

        System.out.print(test.toString());

        int k, r;
        k = 6;
        r = 4;

        System.out.printf("Valgt koordinat (kolonne, rad) er: (%d, %d)\n", k, r);

        test.finnUtveiFra(k, r);
    }
}
