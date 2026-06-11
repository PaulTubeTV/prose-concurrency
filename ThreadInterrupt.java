/**
 * ThreadInterrupt.java
 *
 * Demonstriert:
 *   - thread.interrupt()     → Interrupt-Flag setzen; weckt schlafende / wartende Threads
 *   - InterruptedException   → Wird geworfen, wenn sleep()/wait()/join() unterbrochen wird
 *   - Thread.interrupted()   → Liest & löscht das Interrupt-Flag des aktuellen Threads
 *
 * Zwei typische Szenarien:
 *   a) Thread schläft → interrupt() weckt ihn sofort mit InterruptedException
 *   b) Thread läuft aktiv → interrupt() setzt nur das Flag; Thread prüft es selbst
 */
public class ThreadInterrupt {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== ThreadInterrupt Demonstration ===\n");

        // ─────────────────────────────────────────────────────────────
        // Szenario A: Schlafender Thread wird durch interrupt() geweckt
        // ─────────────────────────────────────────────────────────────
        Thread schlafer = new Thread(() -> {
            try {
                System.out.println("  [Schläfer] Schlafe 10 Sekunden ...");
                Thread.sleep(10_000);
                System.out.println("  [Schläfer] Ausgeschlafen."); // Wird nicht erreicht
            } catch (InterruptedException e) {
                System.out.println("  [Schläfer] Durch interrupt() geweckt! Thread beendet sich.");
                // Gute Praxis: Interrupt-Flag nach dem Catch wieder setzen
                Thread.currentThread().interrupt();
            }
        }, "Schläfer");

        System.out.println("Szenario A: Schlafender Thread");
        schlafer.start();
        Thread.sleep(200); // warten, bis schlafer sicher schläft
        System.out.println("  → interrupt() wird aufgerufen ...");
        schlafer.interrupt();
        schlafer.join();
        System.out.println("  Zustand nach interrupt() + join(): " + schlafer.getState()); // TERMINATED

        // ─────────────────────────────────────────────────────────────
        // Szenario B: Aktiv laufender Thread prüft das Interrupt-Flag selbst
        // ─────────────────────────────────────────────────────────────
        Thread aktiver = new Thread(() -> {
            System.out.println("  [Aktiver] Starte Schleife ...");
            int i = 0;
            // Thread.currentThread().isInterrupted() liest das Flag OHNE es zu löschen
            while (!Thread.currentThread().isInterrupted()) {
                i++;
            }
            System.out.println("  [Aktiver] Interrupt-Flag erkannt. Schleife nach " + i + " Iterationen beendet.");
        }, "Aktiver");

        System.out.println("\nSzenario B: Aktiv laufender Thread");
        aktiver.start();
        Thread.sleep(50);
        System.out.println("  → interrupt() wird aufgerufen ...");
        aktiver.interrupt();
        aktiver.join();
        System.out.println("  Zustand: " + aktiver.getState()); // TERMINATED

        System.out.println("\n=== ThreadInterrupt abgeschlossen ===");
    }
}
