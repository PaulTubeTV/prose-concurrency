public class Deadlock
{
    static class Konto
    {
        private double Kontostand = 100;
        private String name;
        
        public Konto(int startKontostand, String name)
        {
            this.Kontostand = startKontostand;
            this.name = name;
        }

        public double getKontostand()
        {
            return Kontostand;
        }

        public String getName()
        {
            return name;
        }
        
        public synchronized void send(Konto empfaenger, double betrag)
        {
            System.out.println("Sende " + betrag + " von " + this.getName() + " an " + empfaenger.getName());
            this.Kontostand -= betrag;
            try { Thread.sleep(100); } catch (InterruptedException e) {} //Provozieren eines Deadlocks
        }

        public synchronized void receive(double betrag)
        {
            System.out.println("Empfange " + betrag + " bei " + this.getName());
            this.Kontostand += betrag;
        }
           
    }
    
    public static void main(String[] args)
    {
        Konto Henrik = new Konto(1000, "Henrik");
        Konto Paul = new Konto(1000, "Paul");
        new Thread(new Runnable() 
            {
                public void run() { Henrik.send(Paul, 100); }
            }).start();
        new Thread(new Runnable() 
            {
                public void run() { Paul.send(Henrik, 50); }
            }).start();
   
    }
}
