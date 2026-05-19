import java.util.Random;

public class Kunde {
    public static void main(String[] args) {

        Random rand = new Random();
        Konto k = new Konto();

        int n = 1;
        if(args[0] != null) n = Integer.parseInt(args[0]);
        k.einzahlen(n);

        for(int i = 0; i < n; i++){
            int a = rand.nextInt(2);
            if(a == 0) k.einzahlen(rand.nextInt(20));
            if(a == 1) k.auszahlen(rand.nextInt(20));
        }
        System.out.println(k.auslesen());
    }
}