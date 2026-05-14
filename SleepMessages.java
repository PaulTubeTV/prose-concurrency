public class SleepMessages{
    public static void main(String[] args) throws  InterruptedException{
        String importantInfo[] = {
            "Text1",
            "Text2",
            "Text3",
            "Text4"
        };

        for (int i = 0; i < importantInfo.length; i++) {
            // Pause for 4 seconds
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                // We've been interrupted: no more messages.
                return;
            }
            System.out.println(importantInfo[i]);
        }
    }
}
