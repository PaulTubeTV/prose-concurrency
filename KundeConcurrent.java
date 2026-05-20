import java.util.Random;

public class KundeConcurrent {

    Random rand = new Random();

    public void zufällig(Konto k, int n){
        for(int i = 0; i < n; i++){
            int a = rand.nextInt(2);
            if(a == 0) k.einzahlen(rand.nextInt(20));
            if(a == 1) k.auszahlen(rand.nextInt(20));
        }
    }
    
    public void main(String[] args) throws InterruptedException {

        int n = 1000;

        Konto k = new Konto();
        k.einzahlen(n); // Startguthaben
        
        Thread kunde1 = new Thread(() -> {
            zufällig(k, n);
        });

        Thread kunde2 = new Thread(() -> {
            zufällig(k, n);
        });

        kunde1.start();
        kunde2.start();

        kunde1.join();
        kunde2.join();

        System.out.println("Kontostand: " + k.auslesen());
    }
}