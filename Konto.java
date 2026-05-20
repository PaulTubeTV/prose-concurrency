
public class Konto{
    int kontostand = 0;

    public int auslesen() {
        return kontostand;
    }

    public void einzahlen(int betrag){
        kontostand += betrag;
    }

    public void auszahlen(int betrag){
        if(auslesen() < betrag) System.out.println("Nicht genug Geld auf dem Konto");
        else kontostand -= betrag;
    }
}