public class HeapSort {
    public static void main(String[] args) {
        int[] arr = {14, 7, 2, 9, 11, 8, 6, 1, 3, 19};
        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();
        heapSort(arr, arr.length);
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }

    /**
     * 维护堆的性质
     *
     * @param arrs
     * @param len
     * @param i
     */
    private static void heapify(int[] arrs, int len, int i) {
        int largest = i;
        int lson = i * 2 + 1;
        int rson = i * 2 + 2;
        if (lson < len && arrs[largest] < arrs[lson]) {
            largest = lson;
        }
        if (rson < len && arrs[largest] < arrs[rson]) {
            largest = rson;
        }
        if (largest != i) {
            swap(arrs, largest, i);
            heapify(arrs, len, largest);
        }
    }

    private static void swap(int[] arrs, int i, int j) {
        int tmp = arrs[i];
        arrs[i] = arrs[j];
        arrs[j] = tmp;
    }

    /**
     * 堆排序
     *
     * @param arrs
     * @param len
     */
    public static void heapSort(int[] arrs, int len) {
        // 建堆
        int i;
        for (i = len / 2 - 1; i >= 0; i--) {
            heapify(arrs, len, i);
        }
        // 堆排序
        for (i = len - 1; i > 0; i--) {
            swap(arrs, i, 0);
            heapify(arrs, i, 0);
        }
    }
}
