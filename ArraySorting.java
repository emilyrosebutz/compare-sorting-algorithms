public class ArraySorting {
    // Insertion sort
    void insertionSort(int[] a) {
        insertionSort(a, 0, a.length - 1);
    }

    void insertionSort(int[] a, int left, int right) {
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

    void percDown(int[] a, int i, int n) {
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

    void heapsort(int[] a) {
        for (int i = a.length / 2 - 1; i >= 0; i--) /* buildHeap */
            percDown(a, i, a.length);
        for (int i = a.length - 1; i > 0; i--) {
            swapReferences(a, 0, i); /* deleteMax */
            percDown(a, 0, i);
        }
    }

    // Merge sort
    void merge(int[] a, int[] tmpArray,
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

    void mergeSort(int[] a, int[] tmpArray, int left, int right) {
        if (left < right) {
            int center = (left + right) / 2;
            mergeSort(a, tmpArray, left, center);
            mergeSort(a, tmpArray, center + 1, right);
            merge(a, tmpArray, left, center + 1, right);
        }
    }

    void mergeSort(int[] a) {
        int[] tmpArray = new int[a.length];

        mergeSort(a, tmpArray, 0, a.length - 1);
    }

    // Quick sort (no cutoff)
    void quicksortNoCutoff(int[] a) {
        quicksortNoCutoff(a, 0, a.length - 1);
    }

    void quicksortNoCutoff(int[] a, int left, int right) {
        if (left < right) {
            int pivot = median3(a, left, right);

            // Begin partitioning
            int i = left, j = right - 1;
            for (;;) {
                while (a[++i] < pivot) {
                }
                while (a[--j] > pivot) {
                }
                if (i < j)
                    swapReferences(a, i, j);
                else
                    break;
            }

            swapReferences(a, i, right - 1); // Restore pivot

            quicksortNoCutoff(a, left, i - 1); // Sort small elements
            quicksortNoCutoff(a, i + 1, right); // Sort large elements
        }
    }

    // Quick sort (with cutoff)
    // cutoff values of 10, 50, 200 (where small subarrays are sorted using
    // insertion sort)
    int median3(int[] a, int left, int right) {
        int center = (left + right) / 2;
        if (a[center] < a[left])
            swapReferences(a, left, center);
        if (a[right] < a[left])
            swapReferences(a, left, right);
        if (a[right] < a[center])
            swapReferences(a, center, right);

        // Place pivot at position right- 1
        swapReferences(a, center, right - 1);
        return a[right - 1];
    }

    void quicksortWithCutoff(int[] a, int cutoff) {
        quicksortWithCutoff(a, 0, a.length - 1, cutoff);
    }

    void quicksortWithCutoff(int[] a, int left, int right, int cutoff) {
        if (left + cutoff <= right) {
            int pivot = median3(a, left, right);

            int i = left, j = right - 1;
            for (;;) {
                while (a[++i] < pivot) {
                }
                while (a[--j] > pivot) {
                }
                if (i < j)
                    swapReferences(a, i, j);
                else
                    break;
            }

            swapReferences(a, i, right - 1); // Restore pivot

            quicksortWithCutoff(a, left, i - 1, cutoff);
            quicksortWithCutoff(a, i + 1, right, cutoff);
        } else {
            insertionSort(a, left, right);
        }
    }

    // generate random integer arrays and test different number ranges or
    // distributions to observe algorithm behavior under various conditions

    // record the runtime for each algorithm using System.nanoTime()

    // record memory usage using Runtime.getRuntime().totalMemory() -
    // Runtime.getRuntime().freeMemory()

    // repeat experiments for array sizes 50, 500, 1000, 2000, 5000, and 10000

    // OUTPUTS:
    // Console - timing results for each algorithm and array size
    // results.csv - recorded runtimes (and memory, if applicable)
    // README.pdf - one line graph showing the average runtime for each algorithm
    // across all sizes,
    // with labeled axes and legend
}
