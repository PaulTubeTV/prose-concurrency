public class TestRunnable implements Runnable{

    @Override
    public void run(){
        System.out.println("Hello from a Thread!");
    }

    public static void main(String[] args) {
        (new Thread(new TestRunnable())).start();
    }
}