public class KontoConcurrentUse {
    public static void main(String[] args) throws InterruptedException {
        Konto k = new Konto();

        Thread firsThread = new Thread(() -> k.einzahlen(5));
        Thread secondThread = new Thread(() -> System.out.println(k.auslesen()));

        firsThread.start();
        secondThread.start();

        firsThread.join();
        secondThread.join();
    }
}
