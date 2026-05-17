import java.util.Arrays;
import java.util.Random;

public class Mergesort {

    private static final Random RANDOM = new Random();

    public static int[] mergesort(int[] a) {
        if (a == null) return null;
        if (a.length < 2) return a;
        return mergesort(a, 0, a.length - 1);
    }

    private static int[] mergesort(int[] a, int left, int right) {
        if (left == right) return new int[] { a[left] };
        int mid = (left + right) >>> 1;
        int[] leftArr = mergesort(a, left, mid);
        int[] rightArr = mergesort(a, mid + 1, right);
        return merge(leftArr, rightArr);
    }

    private static int[] merge(int[] leftArr, int[] rightArr) {
        int n = leftArr.length + rightArr.length;
        int[] res = new int[n];
        int i = 0, j = 0, k = 0;
        while (i < leftArr.length && j < rightArr.length) {
            if (leftArr[i] <= rightArr[j]) res[k++] = leftArr[i++];
            else res[k++] = rightArr[j++];
        }
        while (i < leftArr.length) res[k++] = leftArr[i++];
        while (j < rightArr.length) res[k++] = rightArr[j++];
        return res;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);

        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = RANDOM.nextInt(1000);

        System.out.println("Array-Länge: " + n);
        if (n <= 200) System.out.println("Vorher: " + Arrays.toString(a));
        else System.out.println("Vorher: (zu lang, zeige nur erste 20) " + Arrays.toString(Arrays.copyOf(a, Math.min(20, n))));

        int[] sorted = mergesort(a);

        if (n <= 200) System.out.println("Nachher: " + Arrays.toString(sorted));
        else System.out.println("Nachher: (zu lang, zeige nur erste 20) " + Arrays.toString(Arrays.copyOf(sorted, Math.min(20, n))));
    }
}
