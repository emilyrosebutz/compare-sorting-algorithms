import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ArraySorting {
    // Insertion sort
    static void insertionSort(int[] a) {
        insertionSort(a, 0, a.length - 1);
    }

    static void insertionSort(int[] a, int left, int right) {
        for (int p = left + 1; p <= right; p++) {
            int tmp = a[p];
            int j = p;
            while (j > left && tmp < a[j - 1]) {
                a[j] = a[j - 1];
                j--;
            }
            a[j] = tmp;
        }
    }

    // Heap sort
    private static void swapReferences(int[] a, int index1, int index2) {
        int tmp = a[index1];
        a[index1] = a[index2];
        a[index2] = tmp;
    }

    private static int leftChild(int i) {
        return 2 * i + 1;
    }

    static void percDown(int[] a, int i, int n) {
        int child;
        int tmp;

        for (tmp = a[i]; leftChild(i) < n; i = child) {
            child = leftChild(i);
            if (child != n - 1 && a[child] < a[child + 1])
                child++;
            if (tmp < a[child])
                a[i] = a[child];
            else
                break;
        }
        a[i] = tmp;
    }

    static void heapsort(int[] a) {
        for (int i = a.length / 2 - 1; i >= 0; i--) /* buildHeap */
            percDown(a, i, a.length);
        for (int i = a.length - 1; i > 0; i--) {
            swapReferences(a, 0, i); /* deleteMax */
            percDown(a, 0, i);
        }
    }

    // Merge sort
    static void merge(int[] a, int[] tmpArray,
            int leftPos, int rightPos, int rightEnd) {
        int leftEnd = rightPos - 1;
        int tmpPos = leftPos;
        int numElements = rightEnd - leftPos + 1;

        // Main loop
        while (leftPos <= leftEnd && rightPos <= rightEnd)
            if (a[leftPos] <= a[rightPos])
                tmpArray[tmpPos++] = a[leftPos++];
            else
                tmpArray[tmpPos++] = a[rightPos++];

        while (leftPos <= leftEnd) // Copy rest of first half
            tmpArray[tmpPos++] = a[leftPos++];

        while (rightPos <= rightEnd) // Copy rest of right half
            tmpArray[tmpPos++] = a[rightPos++];

        // Copy tmpArray back
        for (int i = 0; i < numElements; i++, rightEnd--)
            a[rightEnd] = tmpArray[rightEnd];
    }

    static void mergeSort(int[] a, int[] tmpArray, int left, int right) {
        if (left < right) {
            int center = (left + right) / 2;
            mergeSort(a, tmpArray, left, center);
            mergeSort(a, tmpArray, center + 1, right);
            merge(a, tmpArray, left, center + 1, right);
        }
    }

    static void mergeSort(int[] a) {
        int[] tmpArray = new int[a.length];

        mergeSort(a, tmpArray, 0, a.length - 1);
    }

    // Quick sort (no cutoff)
    static void quicksortNoCutoff(int[] a) {
        quicksortNoCutoff(a, 0, a.length - 1);
    }

    static void quicksortNoCutoff(int[] a, int left, int right) {
        if (left >= right)
            return;
        int size = right - left + 1;
        if (size <= 2) { // handle tiny partitions safely
            if (a[right] < a[left])
                swapReferences(a, left, right);
            return;
        }
        int pivot = median3(a, left, right); // pivot stored at right-1

        int i = left + 1;
        int j = right - 2;
        while (true) {
            while (i <= j && a[i] < pivot)
                i++;
            while (i <= j && a[j] > pivot)
                j--;
            if (i < j) {
                swapReferences(a, i, j);
                i++;
                j--; // shrink inward after swap
            } else {
                break;
            }
        }
        swapReferences(a, i, right - 1); // place pivot in final position

        quicksortNoCutoff(a, left, i - 1);
        quicksortNoCutoff(a, i + 1, right);
    }

    // Quick sort (with cutoff)
    // cutoff values of 10, 50, 200 (where small subarrays are sorted using
    // insertion sort)
    static int median3(int[] a, int left, int right) {
        int center = (left + right) / 2;
        if (a[center] < a[left])
            swapReferences(a, left, center);
        if (a[right] < a[left])
            swapReferences(a, left, right);
        if (a[right] < a[center])
            swapReferences(a, center, right);

        // Place pivot at position right - 1
        swapReferences(a, center, right - 1);
        return a[right - 1];
    }

    static void quicksortWithCutoff(int[] a, int cutoff) {
        quicksortWithCutoff(a, 0, a.length - 1, cutoff);
    }

    static void quicksortWithCutoff(int[] a, int left, int right, int cutoff) {
        if (left >= right)
            return;
        int size = right - left + 1;
        if (size <= cutoff) { // small partition: insertion sort
            insertionSort(a, left, right);
            return;
        }
        if (size <= 2) { // ensure size 2 handled without median3 complications
            if (a[right] < a[left])
                swapReferences(a, left, right);
            return;
        }
        int pivot = median3(a, left, right); // pivot now at right-1
        int i = left + 1;
        int j = right - 2;
        while (true) {
            while (i <= j && a[i] < pivot)
                i++;
            while (i <= j && a[j] > pivot)
                j--;
            if (i < j) {
                swapReferences(a, i, j);
                i++;
                j--; // move inward
            } else {
                break;
            }
        }
        swapReferences(a, i, right - 1); // pivot to final position
        quicksortWithCutoff(a, left, i - 1, cutoff);
        quicksortWithCutoff(a, i + 1, right, cutoff);
    }

    // generate random integer arrays and test different number ranges 
    // to observe algorithm behavior under various conditions
    private static final Random RAND = new Random();
    static int[] generateRandomArray(int size, int range) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = RAND.nextInt(range);
        }
        return array;
    }

    public static void main(String[] args) throws IOException {
        int[] SIZES = { 50, 500, 1000, 2000, 5000, 10000 };
        File csvFile = new File("results.csv");

        try (FileWriter fw = new FileWriter(csvFile)) {
            fw.write("Time (ns)\n");
            fw.write("Size, 50, 500, 1000, 2000, 5000, 10000\n");

            // Insertion sort
            fw.write("Insertion Sort");
            System.out.println("\nInsertion Sort");
            System.out.println("------------------");
            System.out.println("Size  Time (ns)");
            // Track memory usage per size
            long[] memoryUsageInsertion = new long[SIZES.length];
            for (int i = 0; i < 6; i++) {
                int[] insertionSortArr1 = generateRandomArray(SIZES[i], 9);
                long startTime = System.nanoTime();
                insertionSort(insertionSortArr1);
                long endTime = System.nanoTime();
                long duration1 = endTime - startTime;

                int[] insertionSortArr2 = generateRandomArray(SIZES[i], SIZES[i] / 10);
                startTime = System.nanoTime();
                insertionSort(insertionSortArr2);
                endTime = System.nanoTime();
                long duration2 = endTime - startTime;

                int[] insertionSortArr3 = generateRandomArray(SIZES[i], SIZES[i]);
                startTime = System.nanoTime();
                insertionSort(insertionSortArr3);
                endTime = System.nanoTime();
                long duration3 = endTime - startTime;

                int[] insertionSortArr4 = generateRandomArray(SIZES[i], SIZES[i] * 10);
                startTime = System.nanoTime();
                insertionSort(insertionSortArr4);
                endTime = System.nanoTime();
                long duration4 = endTime - startTime;

                int[] insertionSortArr5 = generateRandomArray(SIZES[i], 1000000);
                startTime = System.nanoTime();
                insertionSort(insertionSortArr5);
                endTime = System.nanoTime();
                long duration5 = endTime - startTime;

                long averageDuration = (duration1 + duration2 + duration3 + duration4 + duration5) / 5;
                System.out.printf("%-5d %d%n", SIZES[i], averageDuration);

                fw.write("," + averageDuration);
                long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                memoryUsageInsertion[i] = usedMem;
                if (i == 5) {
                    fw.write("\n");
                }
            }

            // Heap sort
            fw.write("Heap Sort");
            System.out.println("\nHeap Sort");
            System.out.println("------------------");
            System.out.println("Size  Time (ns)");
            // Track memory usage per size
            long[] memoryUsageHeap = new long[SIZES.length];
            for (int i = 0; i < 6; i++) {
                int[] heapSortArr1 = generateRandomArray(SIZES[i], 9);
                long startTime = System.nanoTime();
                heapsort(heapSortArr1);
                long endTime = System.nanoTime();
                long duration1 = endTime - startTime;

                int[] heapSortArr2 = generateRandomArray(SIZES[i], SIZES[i] / 10);
                startTime = System.nanoTime();
                heapsort(heapSortArr2);
                endTime = System.nanoTime();
                long duration2 = endTime - startTime;

                int[] heapSortArr3 = generateRandomArray(SIZES[i], SIZES[i]);
                startTime = System.nanoTime();
                heapsort(heapSortArr3);
                endTime = System.nanoTime();
                long duration3 = endTime - startTime;

                int[] heapSortArr4 = generateRandomArray(SIZES[i], SIZES[i] * 10);
                startTime = System.nanoTime();
                heapsort(heapSortArr4);
                endTime = System.nanoTime();
                long duration4 = endTime - startTime;

                int[] heapSortArr5 = generateRandomArray(SIZES[i], 1000000);
                startTime = System.nanoTime();
                heapsort(heapSortArr5);
                endTime = System.nanoTime();
                long duration5 = endTime - startTime;

                long averageDuration = (duration1 + duration2 + duration3 + duration4 + duration5) / 5;
                System.out.printf("%-5d %d%n", SIZES[i], averageDuration);

                fw.write("," + averageDuration);
                // Capture memory usage (bytes) after timing for this size
                long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                memoryUsageHeap[i] = usedMem;
                if (i == 5) {
                    fw.write("\n");
                }
            }

            // Merge sort
            fw.write("Merge Sort");
            System.out.println("\nMerge Sort");
            System.out.println("------------------");
            System.out.println("Size  Time (ns)");
            // Track memory usage per size
            long[] memoryUsageMerge = new long[SIZES.length];
            for (int i = 0; i < 6; i++) {
                int[] mergeSortArr1 = generateRandomArray(SIZES[i], 9);
                long startTime = System.nanoTime();
                mergeSort(mergeSortArr1);
                long endTime = System.nanoTime();
                long duration1 = endTime - startTime;

                int[] mergeSortArr2 = generateRandomArray(SIZES[i], SIZES[i] / 10);
                startTime = System.nanoTime();
                mergeSort(mergeSortArr2);
                endTime = System.nanoTime();
                long duration2 = endTime - startTime;

                int[] mergeSortArr3 = generateRandomArray(SIZES[i], SIZES[i]);
                startTime = System.nanoTime();
                mergeSort(mergeSortArr3);
                endTime = System.nanoTime();
                long duration3 = endTime - startTime;

                int[] mergeSortArr4 = generateRandomArray(SIZES[i], SIZES[i] * 10);
                startTime = System.nanoTime();
                mergeSort(mergeSortArr4);
                endTime = System.nanoTime();
                long duration4 = endTime - startTime;

                int[] mergeSortArr5 = generateRandomArray(SIZES[i], 1000000);
                startTime = System.nanoTime();
                mergeSort(mergeSortArr5);
                endTime = System.nanoTime();
                long duration5 = endTime - startTime;

                long averageDuration = (duration1 + duration2 + duration3 + duration4 + duration5) / 5;
                System.out.printf("%-5d %d%n", SIZES[i], averageDuration);

                fw.write("," + averageDuration);
                // Capture memory usage (bytes) after timing for this size
                long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                memoryUsageMerge[i] = usedMem;
                if (i == 5) {
                    fw.write("\n");
                }
            }

            // Quick sort (No Cutoff)
            fw.write("Quick Sort (No Cutoff)");
            System.out.println("\nQuick Sort (No Cutoff)");
            System.out.println("------------------");
            System.out.println("Size  Time (ns)");
            // Track memory usage per size
            long[] memoryUsageQuickNoCutoff = new long[SIZES.length];
            for (int i = 0; i < 6; i++) {
                int[] quickSortArr1 = generateRandomArray(SIZES[i], 9);
                long startTime = System.nanoTime();
                quicksortNoCutoff(quickSortArr1);
                long endTime = System.nanoTime();
                long duration1 = endTime - startTime;

                int[] quickSortArr2 = generateRandomArray(SIZES[i], SIZES[i] / 10);
                startTime = System.nanoTime();
                quicksortNoCutoff(quickSortArr2);
                endTime = System.nanoTime();
                long duration2 = endTime - startTime;

                int[] quickSortArr3 = generateRandomArray(SIZES[i], SIZES[i]);
                startTime = System.nanoTime();
                quicksortNoCutoff(quickSortArr3);
                endTime = System.nanoTime();
                long duration3 = endTime - startTime;

                int[] quickSortArr4 = generateRandomArray(SIZES[i], SIZES[i] * 10);
                startTime = System.nanoTime();
                quicksortNoCutoff(quickSortArr4);
                endTime = System.nanoTime();
                long duration4 = endTime - startTime;

                int[] quickSortArr5 = generateRandomArray(SIZES[i], 1000000);
                startTime = System.nanoTime();
                quicksortNoCutoff(quickSortArr5);
                endTime = System.nanoTime();
                long duration5 = endTime - startTime;

                long averageDuration = (duration1 + duration2 + duration3 + duration4 + duration5) / 5;
                System.out.printf("%-5d %d%n", SIZES[i], averageDuration);

                fw.write("," + averageDuration);
                // Capture memory usage (bytes) after timing for this size
                long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                memoryUsageQuickNoCutoff[i] = usedMem;
                if (i == 5) {
                    fw.write("\n");
                }
            }

            // Quick sort (With Cutoff = 10)
            fw.write("Quick Sort (With Cutoff = 10)");
            System.out.println("\nQuick Sort (With Cutoff = 10)");
            System.out.println("------------------");
            System.out.println("Size  Time (ns)");
            // Track memory usage per size
            long[] memoryUsageQuickWithCutoff10 = new long[SIZES.length];
            for (int i = 0; i < 6; i++) {
                int[] quickSortArr1 = generateRandomArray(SIZES[i], 9);
                long startTime = System.nanoTime();
                quicksortWithCutoff(quickSortArr1, 10);
                long endTime = System.nanoTime();
                long duration1 = endTime - startTime;

                int[] quickSortArr2 = generateRandomArray(SIZES[i], SIZES[i] / 10);
                startTime = System.nanoTime();
                quicksortWithCutoff(quickSortArr2, 10);
                endTime = System.nanoTime();
                long duration2 = endTime - startTime;

                int[] quickSortArr3 = generateRandomArray(SIZES[i], SIZES[i]);
                startTime = System.nanoTime();
                quicksortWithCutoff(quickSortArr3, 10);
                endTime = System.nanoTime();
                long duration3 = endTime - startTime;

                int[] quickSortArr4 = generateRandomArray(SIZES[i], SIZES[i] * 10);
                startTime = System.nanoTime();
                quicksortWithCutoff(quickSortArr4, 10);
                endTime = System.nanoTime();
                long duration4 = endTime - startTime;

                int[] quickSortArr5 = generateRandomArray(SIZES[i], 1000000);
                startTime = System.nanoTime();
                quicksortWithCutoff(quickSortArr5, 10);
                endTime = System.nanoTime();
                long duration5 = endTime - startTime;

                long averageDuration = (duration1 + duration2 + duration3 + duration4 + duration5) / 5;
                System.out.printf("%-5d %d%n", SIZES[i], averageDuration);

                fw.write("," + averageDuration);
                // Capture memory usage (bytes) after timing for this size
                long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                memoryUsageQuickWithCutoff10[i] = usedMem;
                if (i == 5) {
                    fw.write("\n");
                }
            }

            // Quick sort (With Cutoff = 50)
            fw.write("Quick Sort (With Cutoff = 50)");
            System.out.println("\nQuick Sort (With Cutoff = 50)");
            System.out.println("------------------");
            System.out.println("Size  Time (ns)");
            // Track memory usage per size
            long[] memoryUsageQuickWithCutoff50 = new long[SIZES.length];
            for (int i = 0; i < 6; i++) {
                int[] quickSortArr1 = generateRandomArray(SIZES[i], 9);
                long startTime = System.nanoTime();
                quicksortWithCutoff(quickSortArr1, 50);
                long endTime = System.nanoTime();
                long duration1 = endTime - startTime;

                int[] quickSortArr2 = generateRandomArray(SIZES[i], SIZES[i] / 10);
                startTime = System.nanoTime();
                quicksortWithCutoff(quickSortArr2, 50);
                endTime = System.nanoTime();
                long duration2 = endTime - startTime;

                int[] quickSortArr3 = generateRandomArray(SIZES[i], SIZES[i]);
                startTime = System.nanoTime();
                quicksortWithCutoff(quickSortArr3, 50);
                endTime = System.nanoTime();
                long duration3 = endTime - startTime;

                int[] quickSortArr4 = generateRandomArray(SIZES[i], SIZES[i] * 10);
                startTime = System.nanoTime();
                quicksortWithCutoff(quickSortArr4, 50);
                endTime = System.nanoTime();
                long duration4 = endTime - startTime;

                int[] quickSortArr5 = generateRandomArray(SIZES[i], 1000000);
                startTime = System.nanoTime();
                quicksortWithCutoff(quickSortArr5, 50);
                endTime = System.nanoTime();
                long duration5 = endTime - startTime;

                long averageDuration = (duration1 + duration2 + duration3 + duration4 + duration5) / 5;
                System.out.printf("%-5d %d%n", SIZES[i], averageDuration);

                fw.write("," + averageDuration);
                // Capture memory usage (bytes) after timing for this size
                long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                memoryUsageQuickWithCutoff50[i] = usedMem;
                if (i == 5) {
                    fw.write("\n");
                }
            }

            // Quick sort (With Cutoff = 100)
            fw.write("Quick Sort (With Cutoff = 100)");
            System.out.println("\nQuick Sort (With Cutoff = 100)");
            System.out.println("------------------");
            System.out.println("Size  Time (ns)");
            // Track memory usage per size
            long[] memoryUsageQuickWithCutoff100 = new long[SIZES.length];
            for (int i = 0; i < 6; i++) {
                int[] quickSortArr1 = generateRandomArray(SIZES[i], 9);
                long startTime = System.nanoTime();
                quicksortWithCutoff(quickSortArr1, 100);
                long endTime = System.nanoTime();
                long duration1 = endTime - startTime;

                int[] quickSortArr2 = generateRandomArray(SIZES[i], SIZES[i] / 10);
                startTime = System.nanoTime();
                quicksortWithCutoff(quickSortArr2, 100);
                endTime = System.nanoTime();
                long duration2 = endTime - startTime;

                int[] quickSortArr3 = generateRandomArray(SIZES[i], SIZES[i]);
                startTime = System.nanoTime();
                quicksortWithCutoff(quickSortArr3, 100);
                endTime = System.nanoTime();
                long duration3 = endTime - startTime;

                int[] quickSortArr4 = generateRandomArray(SIZES[i], SIZES[i] * 10);
                startTime = System.nanoTime();
                quicksortWithCutoff(quickSortArr4, 100);
                endTime = System.nanoTime();
                long duration4 = endTime - startTime;

                int[] quickSortArr5 = generateRandomArray(SIZES[i], 1000000);
                startTime = System.nanoTime();
                quicksortWithCutoff(quickSortArr5, 100);
                endTime = System.nanoTime();
                long duration5 = endTime - startTime;

                long averageDuration = (duration1 + duration2 + duration3 + duration4 + duration5) / 5;
                System.out.printf("%-5d %d%n", SIZES[i], averageDuration);

                fw.write("," + averageDuration);
                // Capture memory usage (bytes) after timing for this size
                long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                memoryUsageQuickWithCutoff100[i] = usedMem;
                if (i == 5) {
                    fw.write("\n");
                }
            }

            

            // Append separate memory usage table to avoid conflicting with timing row
            fw.write("\nMemory\n");
            fw.write("Size, 50, 500, 1000, 2000, 5000, 10000\n");
            fw.write("Insertion Sort");
            for (int i = 0; i < 6; i++) {
                fw.write("," + memoryUsageInsertion[i]);
            }
            fw.write("\nHeap Sort");
            for(int i = 0; i < 6; i++) {
                fw.write("," + memoryUsageHeap[i]);
            }
            fw.write("\nMerge Sort");
            for(int i = 0; i < 6; i++) {
                fw.write("," + memoryUsageMerge[i]);
            }
            fw.write("\nQuick Sort (No Cutoff)");;
            for(int i = 0; i < 6; i++) {    
                fw.write("," + memoryUsageQuickNoCutoff[i]);
            }
            fw.write("\nQuick Sort (With Cutoff = 10)");
            for(int i = 0; i < 6; i++) {    
                fw.write("," + memoryUsageQuickWithCutoff10[i]);
            }
            fw.write("\nQuick Sort (With Cutoff = 50)");
            for(int i = 0; i < 6; i++) {    
                fw.write("," + memoryUsageQuickWithCutoff50[i]);
            }
            fw.write("\nQuick Sort (With Cutoff = 100)");
            for(int i = 0; i < 6; i++) {    
                fw.write("," + memoryUsageQuickWithCutoff100[i]);
            }
        }
    }
}
