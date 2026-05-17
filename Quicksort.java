import java.util.Arrays;
import java.util.Random;

/**
 * Einfache, sequentielle Quicksort-Implementierung (in-place).
 *
 * - Wählt ein zufälliges Pivot, partitioniert das Array und sortiert
 *   die beiden Teilbereiche rekursiv.
 */
public class Quicksort {

	/** Public API: sortiert das gesamte Array in-place. */
	public static void quicksort(int[] a) {
		if (a == null || a.length < 2) return;
		quicksort(a, 0, a.length - 1);
	}

	/** Rekursive Hilfsmethode für Bereich [low..high]. */
	private static void quicksort(int[] a, int low, int high) {
		if (low < high) {
			int p = partition(a, low, high);
			quicksort(a, low, p - 1);
			quicksort(a, p + 1, high);
		}
	}

	/**
	 * Partitioniert den Bereich [low..high] anhand eines zufälligen Pivots.
	 * Gibt den Index zurück, an dem das Pivot nach der Partition steht.
	 */
	private static int partition(int[] a, int low, int high) {
		// zufälliges Pivot wählen und ans Ende tauschen
		int pivotIndex = low + RANDOM.nextInt(high - low + 1);
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

	private static final Random RANDOM = new Random();

	/** Einfacher Array-Swap (in-place). */
	private static void swap(int[] a, int i, int j) {
		int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	/** Demo-`main`: optionaler Parameter `args[0]` legt die Array-Länge fest. */
	public static void main(String[] args) {
		int n = 9; // default length
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
			a[i] = RANDOM.nextInt(1000);
		}

		System.out.println("Array-Länge: " + n);
		if (n <= 200) System.out.println("Vorher: " + Arrays.toString(a));
		else System.out.println("Vorher: (zu lang, zeige nur erste 20) " + Arrays.toString(Arrays.copyOf(a, Math.min(20, n))));

		quicksort(a);

		if (n <= 200) System.out.println("Nachher: " + Arrays.toString(a));
		else System.out.println("Nachher: (zu lang, zeige nur erste 20) " + Arrays.toString(Arrays.copyOf(a, Math.min(20, n))));
	}
}
