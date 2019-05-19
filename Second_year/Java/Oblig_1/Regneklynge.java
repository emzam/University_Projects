import java.util.*;
public class Regneklynge {

    // Lager instansvariablene brukt i klassen.
    private int noderInniRack;
    private ArrayList<Rack> rackListe = new ArrayList<Rack>();

    // Konstruktoer for klassen. Lager en ny regneklynge med plass til x antall noder.
    public Regneklynge(int noderInniRack) {
        this.noderInniRack = noderInniRack;
    }

    // Legger til en ny rack.
    public void nyRack() {
        Rack addRack = new Rack(noderInniRack);
        rackListe.add(addRack);
    }

    // Setter inn node i racken. Denne metoden sjekker om det er ledige plasser i racket. Hvis det ikke er ledige plasser i racket, lages det et nytt rack med noden.
    public void settInnNode(Node node) {
        boolean ikkePlass = true;
        for(Rack r : rackListe) {
            if(r.sjekkLedig()) {
                r.addNode(node);
                ikkePlass = false;
                break;
            }
        }
        if(ikkePlass) {
            nyRack();
            settInnNode(node);
        }
    }

    // Regner ut FLOPS for hver prosessor i hver node for alle racker tilsammen.
    public double flops() {
        double floppser = 0;
        for(Rack r : rackListe) {
            floppser = (floppser + r.flops());
        }
        // Multipliserer med 10^9 fordi svaret ikke skal vaere oppgitt i GHz (som det staar i oppgaven).
        return (floppser * 1000000000);
    }

    // Returnerer antall noder med nok paakrevd minne.
    public int noderMedNokMinne(int paakrevdMinne) {
        int antallNoder = 0;
        for(Rack r : rackListe) {
            antallNoder = (antallNoder + r.noderMedNokMinne(paakrevdMinne));
        }
        return antallNoder;
    }

    // Returnerer antall racks det finnes i listen.
    public int antallRacks() {
        int teller = rackListe.size();
        return teller;
    }
}
