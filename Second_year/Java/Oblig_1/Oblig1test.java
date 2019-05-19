public class Oblig1test {

    public static void main(String[] emirZamwa) {

        // Lager regneklyngen "abel" med plass til 12 noder.
        Regneklynge abel = new Regneklynge(12);

        System.out.println("\nRegneklyngen \"abel\" er laget med plass til 12 noder i hvert rack.\n");

        // Lager de to typene av noder som skal inn i rackene.
        Node type1 = new Node(64, 8, 2.6, 8, 2.6);
        Node type2 = new Node(1024, 8, 2.3, 8, 2.3);

        // Legger til 650 stk av node type1.
        for(int i = 0; i < 650; i++) {
            abel.settInnNode(type1);
        }

        // Legger til 16 stk av node type2.
        for(int i = 0; i < 16; i++) {
            abel.settInnNode(type2);
        }

        // Lager variabler for det oppgaven ber om, og tilkaller metodene.
        double antallFlops = abel.flops();
        int antall32 = abel.noderMedNokMinne(32);
        int antall64 = abel.noderMedNokMinne(64);
        int antall128 = abel.noderMedNokMinne(128);
        int antallRacks = abel.antallRacks();

        // Skriver ut i terminal.
        System.out.println("Samlet FLOPS: " + antallFlops);
        System.out.println("Noder med minst 32GB: " + antall32);
        System.out.println("Noder med minst 64GB: " + antall64);
        System.out.println("Noder med minst 128GB: " + antall128);
        System.out.println("Antall racks: " + antallRacks + "\n");
    }
}
