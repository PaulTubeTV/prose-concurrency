public class ThreadStates {

    public static void main(String[] args) throws InterruptedException {

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
    }
}
