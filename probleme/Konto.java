package probleme;

public class Konto {

    // Problem 1
    private int guthaben = 0;

    // Problem 2
    private boolean isGesperrt = false;

    // Problem 1.1
    public void aufladen(int betrag) {
        // Derzeitiges Guthaben einlesen (Read)
        int derzeitigesGuthaben = this.guthaben;

        // Künstliche Verzögerung, damit der Kontextwechsel genau zwischen
        // dem Einlesen des aktuellen Guthabens (Read)
        // und dem Schreiben des neuen Guthabens (Write) stattfindet.
        try {
            Thread.sleep(1);
        } catch (InterruptedException _) {
        }

        // Guthaben erhöhen (Modify)
        int neuesGuthaben = derzeitigesGuthaben + betrag;

        // Guthaben schreiben (Write)
        this.guthaben = neuesGuthaben;
    }

    // Problem 1.2
    public void abheben(int betrag) {
        // Prüfen, ob genug Guthaben da ist (Check)
        if (this.guthaben >= betrag) {
            IO.println(Thread.currentThread().getName() + " sieht: Genug Geld da! Erlaubnis erteilt");

            // Künstliche Verzögerung, damit der Kontextwechsel genau zwischen
            // dem Abfragen des aktuellen Guthabens (Check) und dem Abheben vom Guthaben (Act) stattfindet.
            try {
                Thread.sleep(10);
            } catch (InterruptedException _) {
            }

            // Geld abheben (Act)
            this.guthaben = this.guthaben - betrag;
            IO.println(Thread.currentThread().getName() + " hat " + betrag + " € abgehoben");
        } else {
            IO.println(Thread.currentThread().getName() + " abgelehnt! Zu wenig Guthaben");
        }
    }

    // Problem 1
    public int getGuthaben() {
        return guthaben;
    }

    // Problem 1
    public void setGuthaben(int guthaben) {
        this.guthaben = guthaben;
    }

    // Problem 2
    public boolean isGesperrt() {
        return isGesperrt;
    }

    // Problem 2
    public void setGesperrt(boolean gesperrt) {
        isGesperrt = gesperrt;
    }
}

