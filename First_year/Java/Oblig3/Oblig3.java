import java.util.Arrays;

public class Oblig3 {

    /** N.B. Sorterer a[] stigende – antar at: 0 ≤ a[i]) < 232 , returnerer
        tiden i millisek. */
    final static int NUM_BIT = 10; /* eller: 6-13 er kanskje best..
                                   finn selv ut hvilken verdi som er raskest */
    final static int MIN_NUM = 31; // mellom 16 og 60, kvikksort bruker 47


    public double VRadixMulti(int [] a) {
        long tt = System.nanoTime();
        int [] b = new int [a.length];
        // a) finn ‘max’ verdi i a[]
        int maxValue = a[0];
        for(int i = 1; i < a.length; i++) {
            if(a[i] > maxValue) {
                maxValue = a[i];
            }
        }
        // b) bestem numBit = høyeste (mest venstre) bit i ‘max’ som == 1
        int numBit = 0;
        for(int i = MIN_NUM; i >= 0; i--) {
            if((maxValue & (int)Math.pow(2, i)) == (int)Math.pow(2, i)) {
                numBit = i + 1;
                break;
            }
        }
        /* c) Første kall (rot-kallet) på VenstreRadix med a[], b[], numBit,
           og lengden av første siffer */
        VenstreRadix(a, b, 0, (a.length - 1), numBit, NUM_BIT);

        double tid = (System.nanoTime() - tt)/1000000.0;
        testSort(a);
        return tid; /* returnerer tiden i ms. det tok å sortere a, som nå er
                       sortert og testet */
    } // end VRadixMulti

// ****************************************************************************
    /* Sorter a[left..right] på siffer med start i bit: leftSortBit, og med
       lengde: maskLen bit, */
    public void VenstreRadix(int[] a, int[] b, int left, int right,
                             int leftSortBit, int maskLen) {
        int mask = (1 << maskLen) - 1;
        int shift = leftSortBit - maskLen;
        int[] count;

        if(leftSortBit <= 0) {
            return;
        }

        if(leftSortBit <= maskLen) {
            mask = mask >>> maskLen - leftSortBit;
            shift = 0;
        }

        count = new int[mask + 1];
        //System.out.println(mask);

        // ……………. Andre deklarasjoner ……………
        /* d) count[] = hvor mange det er med de ulike verdiene
           av dette sifret I a [left..right] */
        for(int i = left; i <= right; i++) {
            count[(a[i] >>> shift) & mask]++;
        }
        // TESTER UNDER
        // for (int j = 0; j < count.length; j++) {
        //     System.out.printf("Index %d: %d\n", j, count[j]);
        // }

        /* e) Tell opp verdiene I count[] slik at count[i] sier hvor i b[] vi
              skal flytte første element med verdien ‘i’ vi sorterer. */
        int[] count2 = new int[count.length];
        int[] count2cpy = new int[count2.length];
        for (int i = 1; i < count2.length; i++) {
            count2[i] = count2[i-1] + count[i-1];
            count2cpy[i] = count2[i];
        }

        /* f) Flytt tallene fra a[] til b[] sorter på dette sifferet I
           a[left..right] for
           alle de ulike verdiene for dette sifferet */
        for(int i = left; i <= right; i++) {
            int index = count2[(a[i] >>> shift) & mask]++;
            b[index + left] = a[i];
        }

        // for(int i = left; i <= right; i++) {
        //     System.out.println(b[i]);
        // }
        // System.out.println("");

        // g) Kall enten innstikkSort eller rekursivt VenstreRadix
        /* på neste siffer (hvis vi ikke er ferdige) for alle verdiene vi har
           av nåværende siffer */
        // Vurder når vi. skal kopiere tilbake b[] til a[] ??
        for(int i = 0; i < count.length; i++) {
            if(count[i] > 1) {
                int newLeft = count2cpy[i] + left;
                int newRight = newLeft + count[i] - 1;

                if(count[i] >= MIN_NUM) {
                    int newleftSortBit = leftSortBit - maskLen;
                    VenstreRadix(b, a, newLeft, newRight, newleftSortBit,
                                 maskLen);
                }
                else {
                    insertSort(b, newLeft, newRight);
                }
            }
        }

        for(int i = left; i <= right; i++) {
            a[i] = b[i];
        }
    }// end VenstreRadix

// ****************************************************************************

    public void insertSort(int[] a, int left, int right) {
        int i, t = left;

        for(int k = left; k < right; k++) {
            // Invariant: a[0..k] er sortert, skal
            // nå sortere a[k+1] inn på riktig plass
            if(a[k] > a[k+1]) {
                t = a[k+1];
                i = k;

                do { // gå bakover, skyv de andre
                    // og finn riktig plass for ’t’
                    a[i+1] = a[i];
                    i--;
                }
                while (i >= left && a[i] > t);
                a[i+1] = t;
            }
        }
    } // end insertSort

// ****************************************************************************

    public double quickSort(int[] a) {
        long tt = System.nanoTime();
        Arrays.sort(a);
        double tid = (System.nanoTime() - tt)/1000000.0;
        return tid;
    }

// ****************************************************************************

    void testSort(int [] a) {
        for (int i = 0; i< a.length-1; i++) {
            if (a[i] > a[i+1]) {
                System.out.println("SorteringsFEIL på: "+
                i +" a["+i+"]:"+a[i]+" > a["+(i+1)+"]:"+a[i+1]);
                return;
            }
        }
    }// end testSort

        /* Tiden tas selv av VenstreRadix. Du må imidlertid omslutte kallet
           på Arrays.sort(a) med tidtaking, Disse tidene for en bestemt verdi
           av n oppbevarer du i to double – arrayer som du så tar
           innstikkSortering på for å finne medianverdien av hver av dem .
           Midtelementet i en sortert array er medianen. */
}
