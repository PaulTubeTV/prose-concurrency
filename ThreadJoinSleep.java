/**
 * ThreadJoinSleep.java
 *
 * Demonstriert:
 *   - thread.join()         → Warten bis Thread fertig (blockiert den aufrufenden Thread)
 *   - thread.join(millis)   → Warten mit Timeout (max. X Millisekunden)
 *   - Thread.sleep(millis)  → Aktuellen Thread pausieren (Zustand: TIMED_WAITING)
 *   - thread.isAlive()      → Prüfen ob Thread noch nicht TERMINATED ist
 */
public class ThreadJoinSleep {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== ThreadJoinSleep Demonstration ===\n");

        // ─────────────────────────────────────────────────────────────
        // 1. thread.join()
        //    main blockiert, bis t1 vollständig fertig ist (TERMINATED).
        //    Garantiert, dass nachfolgende Berechnungen Ergebnisse von t1
        //    sicher lesen können.
        // ─────────────────────────────────────────────────────────────
        Thread t1 = new Thread(() -> {
            System.out.println("  [t1] Starte Arbeit ...");
            try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            System.out.println("  [t1] Fertig.");
        }, "Worker-1");

        System.out.println("1) thread.join()  →  main wartet auf t1 ...");
        t1.start();
        t1.join();
        System.out.println("   t1 fertig. Zustand: " + t1.getState()); // TERMINATED

        // ─────────────────────────────────────────────────────────────
        // 2. thread.join(millis) – Variante mit Timeout
        //    main wartet maximal 500 ms, dann läuft main weiter –
        //    egal ob t2 fertig ist oder nicht.
        // ─────────────────────────────────────────────────────────────
        Thread t2 = new Thread(() -> {
            try { Thread.sleep(5000); } catch (InterruptedException e) {
                System.out.println("  [t2] Wurde durch interrupt() geweckt!");
            }
        }, "SleeperThread");

        System.out.println("\n2) thread.join(500)  →  warte max. 500 ms auf t2 ...");
        t2.start();
        t2.join(500); // Timeout nach 500 ms

        // ─────────────────────────────────────────────────────────────
        // 3. thread.isAlive()
        //    true  → Thread läuft noch (RUNNABLE / BLOCKED / WAITING / TIMED_WAITING)
        //    false → Thread ist TERMINATED
        // ─────────────────────────────────────────────────────────────
        System.out.println("\n3) thread.isAlive():");
        System.out.println("   t2 noch aktiv nach 500 ms? " + t2.isAlive()); // true

        // ─────────────────────────────────────────────────────────────
        // 4. Thread.sleep(millis) – statische Methode
        //    Pausiert den AKTUELL laufenden Thread.
        //    Zustand während sleep: TIMED_WAITING
        // ─────────────────────────────────────────────────────────────
        Thread t3 = new Thread(() -> {
            try {
                System.out.println("  [t3] Schlafe 400 ms ...");
                Thread.sleep(400);
                System.out.println("  [t3] Aufgewacht!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "SleepDemo");

        System.out.println("\n4) Thread.sleep(millis):");
        t3.start();
        Thread.sleep(100); // main kurz schlafen, damit t3 sicher in sleep() ist
        System.out.println("   Zustand von t3 während sleep: " + t3.getState()); // TIMED_WAITING
        t3.join();
        System.out.println("   Zustand von t3 nach join():   " + t3.getState()); // TERMINATED

        // Aufräumen: t2 unterbrechen, damit die JVM sauber beendet
        t2.interrupt();
        t2.join();

        System.out.println("\n=== ThreadJoinSleep abgeschlossen ===");
    }
}
