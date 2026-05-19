import java.util.ArrayList;

public class Konto{
    ArrayList<Integer> verlauf = new ArrayList<>();

    public int auslesen() {
        int res = 0;
        for (int i : verlauf) {
            res += i;
        }
        return res;
    }

    public void einzahlen(int betrag){
        verlauf.addFirst(betrag);
    }

    public void auszahlen(int betrag){
        if(auslesen() < betrag) System.out.println("Nicht genug Geld auf dem Konto");
        else verlauf.addFirst(betrag * (-1));
    }
}