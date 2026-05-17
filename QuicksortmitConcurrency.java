import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Concurrent Quicksort-Implementierung mit ForkJoin.
 *
 * - Teilt das Array rekursiv in Partitionen auf.
 * - Forkt kleinere Subaufgaben und bearbeitet größere Seiten iterativ,
 *   um Stapelüberläufe zu vermeiden (Tail-Recursion-Elimination).
 */
public class QuicksortmitConcurrency {

	/*
	 * Grenze, ab der eine Partition sequenziell mit Arrays.sort() bearbeitet wird.
	 * Ein zu kleiner Wert führt zu viel Fork/Join-Overhead, ein zu großer
	 * Wert reduziert Parallelität. 16 ist ein typischer Startwert.
	 */
	static final int THRESHOLD = 16;

	/**
	 * ForkJoin-Task für einen Bereich des Arrays [low..high].
	 * Die Implementierung vermeidet tiefe Rekursion, indem sie kleinere
	 * Partitionen forkt und die größere Seite iterativ weiterverarbeitet.
	 */
	private static class QuickSortTask extends RecursiveAction {
		private final int[] a;
		private final int low, high;

		QuickSortTask(int[] a, int low, int high) {
			this.a = a;
			this.low = low;
			this.high = high;
		}

		@Override
		protected void compute() {
			// Keine Arbeit für leere oder ein-elementige Bereiche
			if (low >= high) return;
			// Stapel zum Sammeln geforkter kleiner Tasks
			Deque<QuickSortTask> forks = new ArrayDeque<>();
			int l = low, h = high;
			// Iterative Bearbeitung: solange l < h weiter aufteilen
			while (l < h) {
				int size = h - l + 1;
				// Für kleine Bereiche sequenziell sortieren (Effizienz)
				if (size <= THRESHOLD) {
					Arrays.sort(a, l, h + 1);
					break;
				}
				int p = partition(a, l, h);
				int leftSize = p - l;
				int rightSize = h - p;
				// Forke die kleinere Seite, bearbeite die größere weiter (Tail-Call-Optimierung)
				if (leftSize < rightSize) {
					QuickSortTask left = new QuickSortTask(a, l, p - 1);
					left.fork();
					forks.push(left);
					l = p + 1; // weiter mit rechter Seite
				} else {
					QuickSortTask right = new QuickSortTask(a, p + 1, h);
					right.fork();
					forks.push(right);
					h = p - 1; // weiter mit linker Seite
				}
			}
			// Auf alle geforkten Tasks warten (join)
			while (!forks.isEmpty()) {
				forks.pop().join();
			}
		}
	}

	private static int partition(int[] a, int low, int high) {
		int pivotIndex = ThreadLocalRandom.current().nextInt(low, high + 1);
		int pivot = a[pivotIndex];
		swap(a, pivotIndex, high);

		int store = low;
		for (int i = low; i < high; i++) {
			if (a[i] < pivot) {
				swap(a, i, store);
				store++;
			}
		}
		swap(a, store, high);
		return store;
	}

	private static void swap(int[] a, int i, int j) {
		int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	public static void quicksortConcurrent(int[] a) {
		if (a == null || a.length < 2) return;
		ForkJoinPool.commonPool().invoke(new QuickSortTask(a, 0, a.length - 1));
	}

	public static void main(String[] args) {
		int n = 14; // default length
		if (args.length >= 1) {
			try {
				n = Integer.parseInt(args[0]);
				if (n < 0) n = 0;
			} catch (NumberFormatException e) {
				// ignore and use default
			}
		}

		int[] a = new int[n];
		for (int i = 0; i < n; i++) {
			a[i] = ThreadLocalRandom.current().nextInt(1000);
		}

		System.out.println("Array-Länge: " + n);
		if (n <= 200) System.out.println("Vorher: " + Arrays.toString(a));
		else System.out.println("Vorher: (zu lang, zeige nur erste 20) " + Arrays.toString(Arrays.copyOf(a, Math.min(20, n))));

		quicksortConcurrent(a);

		if (n <= 200) System.out.println("Nachher: " + Arrays.toString(a));
		else System.out.println("Nachher: (zu lang, zeige nur erste 20) " + Arrays.toString(Arrays.copyOf(a, Math.min(20, n))));
	}
}
