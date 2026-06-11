/**
 * ThreadStates.java
 *
 * Demonstriert alle 6 Zustände (Thread.State) eines Java-Threads:
 *
 *   NEW            → Thread erstellt, start() noch nicht aufgerufen
 *   RUNNABLE       → Thread läuft oder wartet auf CPU-Zeit
 *   BLOCKED        → Thread wartet darauf, einen synchronized-Block zu betreten
 *   WAITING        → Thread wartet unbegrenzt (wait(), join() ohne Timeout)
 *   TIMED_WAITING  → Thread wartet begrenzt (sleep(ms), join(ms), wait(ms))
 *   TERMINATED     → run() ist abgeschlossen
 *
 * Zustandsübergänge (vereinfacht):
 *
 *   new Thread()
 *       │
 *       ▼
 *     [NEW]
 *       │  start()
 *       ▼
 *   [RUNNABLE] ◄────────────────────────────────────────────────────┐
 *       │                                                           │
 *       ├── synchronized-Block belegt ──► [BLOCKED]  ── frei ─────►┤
 *       │                                                           │
 *       ├── wait() / join() ────────────► [WAITING]  ── notify() ──►┤
 *       │                                                           │
 *       ├── sleep(ms) / join(ms) / wait(ms) ► [TIMED_WAITING] ─────►┤
 *       │                                  (Timeout oder notify())  │
 *       │                                                           │
 *       └── run() abgeschlossen ─────────► [TERMINATED]
 */
public class ThreadStates {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread-States Demonstration ===\n");

        // ── NEW ──────────────────────────────────────────────────────
        Thread thread = new Thread(() -> {
            try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "States-Demo");

        System.out.println("NEW            → " + thread.getState());

        // ── RUNNABLE ─────────────────────────────────────────────────
        thread.start();
        System.out.println("RUNNABLE       → " + thread.getState());

        // ── TIMED_WAITING ────────────────────────────────────────────
        Thread.sleep(50); // thread schläft noch durch sleep(500) oben
        System.out.println("TIMED_WAITING  → " + thread.getState());

        // ── WAITING ──────────────────────────────────────────────────
        Object lock = new Object();
        Thread wartenThread = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait(); // unbegrenzt warten → WAITING
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Warte-Thread");
        wartenThread.start();
        Thread.sleep(50); // warten, bis wartenThread sicher in wait() ist
        System.out.println("WAITING        → " + wartenThread.getState());

        // ── BLOCKED ──────────────────────────────────────────────────
        Object sharedLock = new Object();

        Thread halterThread = new Thread(() -> {
            synchronized (sharedLock) {
                try { Thread.sleep(600); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }, "Lock-Halter");

        Thread blockedThread = new Thread(() -> {
            synchronized (sharedLock) { /* wird blockiert, während halterThread den Lock hält */ }
        }, "Blocked-Thread");

        halterThread.start();
        Thread.sleep(50); // warten, bis halterThread den Lock sicher hält
        blockedThread.start();
        Thread.sleep(50); // warten, bis blockedThread versucht, den Lock zu betreten
        System.out.println("BLOCKED        → " + blockedThread.getState());

        // ── TERMINATED ───────────────────────────────────────────────
        thread.join();
        System.out.println("TERMINATED     → " + thread.getState());

        // Aufräumen
        synchronized (lock) {
            lock.notifyAll(); // wartenThread aufwecken
        }
        wartenThread.join();
        halterThread.join();
        blockedThread.join();

        System.out.println("\n=== Thread-States abgeschlossen ===");
    }
}
