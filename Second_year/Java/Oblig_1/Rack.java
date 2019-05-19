public class Rack {

    // instansvariablene for klassen.
    private int maxNoder;
    private int antallNoder;
    private Node[] nodeArray;

    // Konstruktoer for klassen. Legger til node.
    public Rack(int maxNoder) {
        this.maxNoder = maxNoder;
        nodeArray = new Node[maxNoder];
    }

    // Sjekker om racken har plass til flere noder.
    public boolean sjekkLedig() {
        for(int i = 0; i < maxNoder; i++) {
            if(nodeArray[i] == null) {
                return true;
            }
        }
        return false;
    }

    // Legger til ny node.
    public void addNode(Node nyNode) {
        nodeArray[antallNoder] = nyNode;
        antallNoder++;
    }

    // Regner ut FLOPS for hver prosessor i hver node tilsammen.
    public double flops() {
        double floppser = 0;
        for(int i = 0; i < maxNoder; i++) {
            if(nodeArray[i] != null) {
                floppser = (floppser + (nodeArray[i].flops()));
            }
        }
        return floppser;
    }

    // Teller hvor mange noder det finnes med nok paakrevd minne.
    public int noderMedNokMinne(int paakrevdMinne) {
        int teller = 0;
        for(int i = 0; i < maxNoder; i++) {
            if(nodeArray[i] != null) {
                if(nodeArray[i].nokMinne(paakrevdMinne)) {
                    teller++;
                }
            }
        }
        return teller;
    }
}
