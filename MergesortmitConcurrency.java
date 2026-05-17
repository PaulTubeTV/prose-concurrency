import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Optimierte, parallelisierte Mergesort-Implementierung mit ForkJoin.
 *
 * Optimierungen:
 * - Gemeinsames Hilfsarray `tmp` (vermeidet viele Allokationen)
 * - ForkJoinRecursiveAction für rekursive Parallelität (skalierbar auf viele Kerne)
 * - Threshold: kleine Bereiche werden mit `Arrays.sort` sequenziell sortiert
 */
public class MergesortmitConcurrency {

    private static final Random RANDOM = new Random();
    // Schwelle, unterhalb der sequenziell sortiert wird (tunebar)
    private static final int THRESHOLD = 1 << 13; // 8192

    private static class MergeTask extends RecursiveAction {
        final int[] a;
        final int[] tmp;
        final int lo, hi;

        MergeTask(int[] a, int[] tmp, int lo, int hi) {
            this.a = a; this.tmp = tmp; this.lo = lo; this.hi = hi;
        }

        @Override
        protected void compute() {
            int len = hi - lo + 1;
            if (len <= THRESHOLD) {
                Arrays.sort(a, lo, hi + 1);
                return;
            }
            int mid = (lo + hi) >>> 1;
            MergeTask left = new MergeTask(a, tmp, lo, mid);
            MergeTask right = new MergeTask(a, tmp, mid + 1, hi);
            invokeAll(left, right);
            merge(a, tmp, lo, mid, hi);
        }
    }

    /** Merge: kopiert Bereich nach tmp und merged zurück in a */
    private static void merge(int[] a, int[] tmp, int lo, int mid, int hi) {
        System.arraycopy(a, lo, tmp, lo, hi - lo + 1);
        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            if (tmp[i] <= tmp[j]) a[k++] = tmp[i++];
            else a[k++] = tmp[j++];
        }
        while (i <= mid) a[k++] = tmp[i++];
        while (j <= hi) a[k++] = tmp[j++];
    }

    public static void mergesortParallel(int[] a) {
        if (a == null || a.length < 2) return;
        int[] tmp = new int[a.length];
        ForkJoinPool.commonPool().invoke(new MergeTask(a, tmp, 0, a.length - 1));
    }

    /** Demo-`main`: optionaler Parameter `args[0]` legt die Array-Länge fest. */
    public static void main(String[] args) {
        int n = 14;
        if (args.length >= 1) {
            try { n = Integer.parseInt(args[0]); if (n < 0) n = 0; }
            catch (NumberFormatException e) { }
        }

        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = RANDOM.nextInt(1000);

        System.out.println("Array-Länge: " + n);
        if (n <= 200) System.out.println("Vorher: " + Arrays.toString(a));
        else System.out.println("Vorher: (zu lang, zeige nur erste 20) " + Arrays.toString(Arrays.copyOf(a, Math.min(20, n))));

        mergesortParallel(a);

        if (n <= 200) System.out.println("Nachher: " + Arrays.toString(a));
        else System.out.println("Nachher: (zu lang, zeige nur erste 20) " + Arrays.toString(Arrays.copyOf(a, Math.min(20, n))));
    }
}
