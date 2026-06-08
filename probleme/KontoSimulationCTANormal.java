import probleme.Konto;

void main() {
    Konto konto = new Konto();

    konto.setGuthaben(10);

    IO.println("Startguthaben auf dem gemeinsamen Konto: " + konto.getGuthaben() + " €");
    IO.println("Zwei Personen versuchen GLEICHZEITIG jeweils 10 € abzuheben...\n");

    // Person A am Geldautomat 1
    konto.abheben(10);

    // Person B am Geldautomat 2
    konto.abheben(10);

    // Das Ergebnis
    IO.println("----------------------------------------");
    IO.println("Erwartetes Mindestguthaben: 0 € (Zweite Buchung müsste abgelehnt werden!)");
    IO.println("Tatsächliches Guthaben: " + konto.getGuthaben() + " €");
    IO.println("----------------------------------------");
}
