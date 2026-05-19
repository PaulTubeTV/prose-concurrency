import java.util.Random;

public class KundeConcurrent {
    public static void main(String[] args) throws InterruptedException {
        Konto k = new Konto();
        k.einzahlen(100); // Startguthaben
        
        Thread kunde1 = new Thread(() -> {
            Random rand = new Random();
            for (int i = 0; i < 1000; i++) {
                if (rand.nextInt(2) == 0) k.einzahlen(rand.nextInt(20));
                else k.auszahlen(rand.nextInt(20));
            }
        });

        Thread kunde2 = new Thread(() -> {
            Random rand = new Random();
            for (int i = 0; i < 1000; i++) {
                if (rand.nextInt(2) == 0) k.einzahlen(rand.nextInt(20));
                else k.auszahlen(rand.nextInt(20));
            }
        });

        kunde1.start();
        kunde2.start();
        
        kunde1.join();
        kunde2.join();

        System.out.println("Kontostand: " + k.auslesen());
    }
}