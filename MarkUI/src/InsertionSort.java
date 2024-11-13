public class InsertionSort {
    public static void insertionSort(String[] array)
    {
        for (int i = 1; i < array.length; i++) {
            String key = array[i];
            int j = i - 1;

            // Move elements of array[0..i-1] that are greater than key to one position ahead of their current position
            while (j >= 0 && array[j].compareTo(key) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }
}
