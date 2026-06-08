import probleme.Konto;

void main() throws InterruptedException {
    Konto konto = new Konto();
    List<Thread> threads = new ArrayList<>();

    IO.println("Startguthaben: " + konto.getGuthaben() + " €");
    IO.println("Führe 100 Transaktionen mit jeweils 10 € aus...");

    // Wir erstellen 100 Threads, welche alle gleichzeitig 10 € auf das Konto laden
    for (int i = 0; i < 100; i++) {
        Thread t = new Thread(() -> {
            konto.aufladen(10);
            IO.println("[" + Thread.currentThread().getName() + "] 10 € aufgeladen");
        });
        threads.add(t);
        t.start();
    }

    // Warten, bis alle Threads fertig sind
    for (Thread t : threads) {
        t.join();
    }

    // Das Ergebnis
    IO.println("----------------------------------------");
    IO.println("Erwartetes Guthaben: 1000 €");
    IO.println("Tatsächliches Guthaben: " + konto.getGuthaben() + " €");
    IO.println("----------------------------------------");
}
