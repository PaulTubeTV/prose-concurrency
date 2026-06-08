import probleme.Konto;

void main() throws InterruptedException {
    Konto konto = new Konto();
    konto.setGuthaben(500);

    IO.println("Startguthaben: " + konto.getGuthaben() + " €");
    IO.println("Konto-Status: Aktiv (isGesperrt = false)");
    IO.println("Das Zins-System bucht nun fortlaufend Zinsen ab, bis die Sperre greift...\n");

    // Das Zins-System, welches in einer engen Schleife den Status prüft
    Thread thread = new Thread(() -> {
        long runden = 0;
        // Solange das Konto NICHT gesperrt ist, läuft die Schleife
        while (!konto.isGesperrt()) {
            runden++;
        }
        IO.println("\n[Zins-System] STOPP! Konto-Sperre wurde erkannt nach " + runden + " Schleifendurchläufen");
    }, "Zins-Thread");

    // Zins-System starten
    thread.start();

    // Der Haupt-Thread wartet 1 Sekunde ...
    Thread.sleep(1000);

    // ... und sperrt dann das Konto
    IO.println("\n[Bank-System] !!! BETRUGSVERDACHT !!! Sperre das Konto jetzt...");
    konto.setGesperrt(true);
    IO.println("[Bank-System] Status im RAM wurde auf 'isGesperrt = true' gesetzt.");

    // Wir warten, ob der Zins-Thread aufhört
    // Nach 3 Sekunden überprüfen wir, wie der aktuelle Status des Systems ist
    thread.join(3000);

    IO.println("----------------------------------------");
    if (thread.isAlive()) {
        IO.println("Erwartetes Resultat: Das Zins-System hat sauber gestoppt");
        IO.println("Tatsächliches Resultat: Endlosschleife! Das Zins-System läuft weiterhin");
        thread.interrupt();
    } else {
        IO.println("Erwartetes Resultat: Das Zins-System hat sauber gestoppt");
        IO.println("Tatsächliches Resultat: Das Zins-System hat sauber gestoppt");
    }
    IO.println("----------------------------------------");
}