import probleme.Konto;

void main() {
    Konto konto = new Konto();

    IO.println("Startguthaben: " + konto.getGuthaben() + " €");
    IO.println("Führe 100 Transaktionen mit jeweils 10 € aus...");

    // Es werden 10 € nacheinander auf das Konto geladen
    for (int i = 0; i < 100; i++) {
        konto.aufladen(10);
        IO.println("[" + Thread.currentThread().getName() + "] 10 € aufgeladen");
    }

    // Das Ergebnis
    IO.println("----------------------------------------");
    IO.println("Erwartetes Guthaben: 1000 €");
    IO.println("Tatsächliches Guthaben: " + konto.getGuthaben() + " €");
    IO.println("----------------------------------------");
}
