import java.util.Random;

public class Mergesort {

    public static void sort(int[] arr, int left, int right) {
        if (left >= right) return;

        int mid = (left + right) / 2;
        sort(arr, left, mid);
        sort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    public static void merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right)
            temp[k++] = arr[i] <= arr[j] ? arr[i++] : arr[j++];
        while (i <= mid)  temp[k++] = arr[i++];
        while (j <= right) temp[k++] = arr[j++];

        for (int l = 0; l < temp.length; l++)
            arr[left + l] = temp[l];
    }

    public static void sortConcurrent(int[] arr, int left, int right) throws InterruptedException {
        if (left >= right) return;

        int mid = (left + right) / 2;

        Thread leftThread = new Thread(() -> {
            try {
                sortConcurrent(arr, left, mid);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        });
        Thread rightThread = new Thread(() -> {
            try {
                sortConcurrent(arr, mid + 1, right);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        });

        leftThread.start();
        rightThread.start();

        leftThread.join();
        rightThread.join();

        merge(arr, left, mid, right);
    }

    private static String preview(int[] arr, int max) {
        int n = Math.min(arr.length, max);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < n; i++) {
            if (i > 0) sb.append(", ");
            sb.append(arr[i]);
        }
        if (arr.length > max) sb.append(", ...");
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) throws InterruptedException {
        int size = 20;
        if (args.length > 0) {
            try {
                size = Integer.parseInt(args[0]);
                if (size < 0) {
                    System.err.println("Negative Größe nicht erlaubt, verwende 20.");
                    size = 20;
                }
            } catch (NumberFormatException e) {
                System.err.println("Ungültiges Argument, nutze Standardgröße 20.");
            }
        }

        int[] arr = new int[Math.max(0, size)];
        Random rnd = new Random();
        for (int i = 0; i < arr.length; i++)
            arr[i] = rnd.nextInt(100); // Werte 0-99

        System.out.println("Unsortiert: " + preview(arr, 20)
                + (arr.length > 20 ? " (zeige 20 von " + arr.length + ")" : ""));

        if (arr.length > 0) {
            sortConcurrent(arr, 0, arr.length - 1);
        }

        System.out.println("Sortiert:   " + preview(arr, 20)
                + (arr.length > 20 ? " (zeige 20 von " + arr.length + ")" : ""));
    }

    
}