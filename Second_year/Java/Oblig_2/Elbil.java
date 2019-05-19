public class Elbil extends Bil {

    // Forklaring paa String ligger i Fossilbil klassen.
    protected String batteriKapasitet;

    // Konstruktoer for klassen.
    public Elbil(String batteriKapasitet, String kjennemerke) {
        super(kjennemerke);
        this.batteriKapasitet = batteriKapasitet;
    }

    /* Overrider metoden hvis den blir tilkalt fra denne klassen. Skriver ut info om elbilen. */
    @Override
    public void skrivInfo() {
        System.out.println("Type motorvogn: Elbil");
        super.skrivInfo();
        System.out.println("BatteriKapasitet: " + batteriKapasitet + " kWh");
    }
}
