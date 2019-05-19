import java.util.Random;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        int n = 100;
        int[] a1 = new int[n];
        int[] a2 = new int[n];
        int[] cpy = new int[n];

        for(int i = 0; i < a1.length; i++) {
            a1[i] = random.nextInt(n);
            a2[i] = a1[i];
            cpy[i] = a2[i];
        }

        run(a1, a2, cpy, n);

        random = new Random();
        n = 1000;
        a1 = new int[n];
        a2 = new int[n];
        cpy = new int[n];

        for(int i = 0; i < a1.length; i++) {
            a1[i] = random.nextInt(n);
            a2[i] = a1[i];
            cpy[i] = a2[i];
        }

        run(a1, a2, cpy, n);

        random = new Random();
        n = 10000;
        a1 = new int[n];
        a2 = new int[n];
        cpy = new int[n];

        for(int i = 0; i < a1.length; i++) {
            a1[i] = random.nextInt(n);
            a2[i] = a1[i];
            cpy[i] = a2[i];
        }

        run(a1, a2, cpy, n);

        random = new Random();
        n = 100000;
        a1 = new int[n];
        a2 = new int[n];
        cpy = new int[n];

        for(int i = 0; i < a1.length; i++) {
            a1[i] = random.nextInt(n);
            a2[i] = a1[i];
            cpy[i] = a2[i];
        }

        run(a1, a2, cpy, n);

        random = new Random();
        n = 1000000;
        a1 = new int[n];
        a2 = new int[n];
        cpy = new int[n];

        for(int i = 0; i < a1.length; i++) {
            a1[i] = random.nextInt(n);
            a2[i] = a1[i];
            cpy[i] = a2[i];
        }

        run(a1, a2, cpy, n);

        random = new Random();
        n = 10000000;
        a1 = new int[n];
        a2 = new int[n];
        cpy = new int[n];

        for(int i = 0; i < a1.length; i++) {
            a1[i] = random.nextInt(n);
            a2[i] = a1[i];
            cpy[i] = a2[i];
        }

        run(a1, a2, cpy, n);

//         Oblig3 test = new Oblig3();
//         Random random = new Random();
//         double[] median = new double[5];
//         double[] median2 = new double[5];
//
//         int n = 100;
//         for(int j = 0; j < 5; j++) {
//             if(n == 100000000) {
//                 return;
//             }
//
//             System.out.println("n = " + n + "\n");
//             int[] a1 = new int[n];
//             int[] a2 = new int[n];
//
//             for(int i = 0; i < a1.length; i++) {
//                 a1[i] = random.nextInt(n);
//                 a2[i] = a1[i];
//             }
//
//             double tid1 = test.VRadixMulti(a1);
//             double tid2 = test.quickSort(a2);
//             double forhold1 = tid2/tid1;
//             double forhold2 = tid1/tid2;
//
//             System.out.println("VRadix: " + tid1 + " ms");
//             System.out.println("QuickSort: " + tid2 + " ms");
//             System.out.println("Forhold1 (Quick/VRadix): " + forhold1 +
//             "\nForhold2 (VRadix/Quick): " + forhold2);
//             System.out.println("");
//
//             double tallet = forhold1;
//             double tallet2 = forhold2;
//             median[j] = tallet;
//             median2[j] = tallet2;
//
//             if(j == 4) {
//                 j = -1;
//                 n = n * 10;
//                 Arrays.sort(median);
//                 Arrays.sort(median2);
//                 System.out.println("Median VRadix: " + median[2]);
//                 System.out.println("Median Quick: " + median2[2]);
// System.out.println("******************************************************\n");
//             }
//         }
    }


    public static void run(int[] a1, int[] a2, int[] cpy, int n) {
        Oblig3 test = new Oblig3();
        double[] median = new double[5];
        double[] median2 = new double[5];
        double[] median3 = new double[5];
        double[] median4 = new double[5];


        for(int j = 0; j < 5; j++) {
            for(int k = 0; k < cpy.length; k++) {
                a1[k] = cpy[k];
                a2[k] = cpy[k];
            }

            System.out.println("n = " + n + "\n");

            double tid1 = test.VRadixMulti(a1);
            double tid2 = test.quickSort(a2);
            double forhold1 = tid2/tid1;
            double forhold2 = tid1/tid2;

            System.out.println("VRadix: " + tid1 + " ms");
            System.out.println("QuickSort: " + tid2 + " ms");
            System.out.println("Forhold1 (Quick/VRadix): " + forhold1 +
            "\nForhold2 (VRadix/Quick): " + forhold2);
            System.out.println("");

            double tallet = forhold1;
            double tallet2 = forhold2;
            double tallet3 = tid1;
            double tallet4 = tid2;
            median[j] = tallet;
            median2[j] = tallet2;
            median3[j] = tid1;
            median4[j] = tid2;

            if(j == 4) {
                Arrays.sort(median);
                Arrays.sort(median2);
                Arrays.sort(median3);
                Arrays.sort(median4);
                System.out.println("Median Speed VRadix: " + median[2]);
                System.out.println("Median Speed Quick: " + median2[2]);
                System.out.println("Median tid Vradix: " + median3[2] + " ms");
                System.out.println("Median tid Quick: " + median4[2] + " ms");
System.out.println("******************************************************\n");
            }
        }
    }
}
