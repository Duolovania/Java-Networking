/**
 * InsertionSort.java
 *
 * The insertion sort class for the application.
 * This class handles the insertion sorting algorithm to be used in the application.
 *
 * Version 1.00.
 * Author: Ryhan Khan.
 */
public class InsertionSort
{
    /**
     * This method handles the sorting algorithm.
     *
     * @param array the array that will be sorted.
     */
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
