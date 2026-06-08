import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class fixedDeadlock
{
    static class Konto
    {
        private Lock lock = new ReentrantLock();
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
        
        public boolean trySend(Konto empfaenger, double betrag)
        {
            Boolean sendLock = false;
            Boolean receiveLock = false;
            try
            {
                sendLock = lock.tryLock();
                receiveLock = empfaenger.lock.tryLock();
            }
            finally
            {
                if(!(sendLock && receiveLock))
                {
                    if(sendLock)
                    {
                        lock.unlock();
                    }
                    if(receiveLock) 
                    {
                        empfaenger.lock.unlock();
                    }
                }  
            }
            return sendLock && receiveLock;

        }

        public void send(Konto empfaenger, double betrag)
        {
            if(trySend(empfaenger, betrag))
            {
                try
                {
                    System.out.println("Sende " + betrag + " von " + this.getName() + " an " + empfaenger.getName());
                    this.Kontostand -= betrag;
                    empfaenger.receive(betrag);
                }
                finally
                {
                    lock.unlock();
                    empfaenger.lock.unlock();
                } 
            }
            else
            {
                System.out.println(this.getName() + " sendet an " + empfaenger.getName() + " aber " + empfaenger.getName() + " sendet gerade an " + this.getName());
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e) {}
                finally
                {
                    send(empfaenger, betrag);
                }
            }  
        }

        public void receive(double betrag)
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