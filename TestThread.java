public class TestThread extends Thread {
    
    @Override
    public void run(){
        System.out.println("Hello from a thread!");
    }

    public static void main(String[] args) {
        (new Thread(new TestRunnable())).start();
    }
}
