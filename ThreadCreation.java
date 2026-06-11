public class ThreadCreation {

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            System.out.println("  [" + Thread.currentThread().getName() + "] läuft!");
        }, "Demo-Thread-1");

        System.out.println("1) Zustand nach new Thread: " + t1.getState()); // NEW

        t1.start();
        System.out.println("2) Zustand nach start():        " + t1.getState()); // RUNNABLE (oder TERMINATED)

        t1.join(); // Warten bis t1 fertig ist
        System.out.println("3) Zustand nach join():         " + t1.getState()); // TERMINATED


        Thread benannt   = new Thread(() -> {}, "Mein-Thread");
        Thread unbenannt = new Thread(() -> {});

        System.out.println("\n4) Thread.getName():");
        System.out.println("   Benannter Thread:   " + benannt.getName());
        System.out.println("   Unbenannter Thread: " + unbenannt.getName()); // Thread-0, Thread-1, ...

        System.out.println("\n5) Thread.currentThread():");
        System.out.println("   Aktuell laufender Thread: " + Thread.currentThread().getName()); // main

    }
}
