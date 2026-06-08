import probleme.Konto;

void main() throws InterruptedException {
    Konto konto = new Konto();

    konto.setGuthaben(10);

    IO.println("Startguthaben auf dem gemeinsamen Konto: " + konto.getGuthaben() + " €");
    IO.println("Zwei Personen versuchen GLEICHZEITIG jeweils 10 € abzuheben...\n");

    // Person A am Geldautomat 1
    Thread personA = new Thread(() -> konto.abheben(10), "Ehepartner A");

    // Person B am Geldautomat 2
    Thread personB = new Thread(() -> konto.abheben(10), "Ehepartner B");

    // Beide drücken exakt im selben Moment auf "Bestätigen"
    personA.start();
    personB.start();

    personA.join();
    personB.join();

    // Das Ergebnis
    IO.println("----------------------------------------");
    IO.println("Erwartetes Mindestguthaben: 0 € (Zweite Buchung müsste abgelehnt werden!)");
    IO.println("Tatsächliches Guthaben: " + konto.getGuthaben() + " €");
    IO.println("----------------------------------------");
}