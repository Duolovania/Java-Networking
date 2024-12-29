/**
 * ShellSort.java
 *
 * The shell sort class for the application.
 * This class handles the shell sorting algorithm to be used in the application.
 *
 * Version 1.00.
 * Author: Ryhan Khan.
 */
public class ShellSort<T extends Comparable<T>>
{
    private final T[] arr;

    /**
     * This constructor takes in the array (of any type) that will be sorted.
     * Note: int arrays are not supported. To use an 'int' array, you must convert/change to an array of the 'Integer' type.
     *
     * @param arr The array that will be sorted.
     */
    public ShellSort(T[] arr) {
        this.arr = arr; // Copies the array being inserted.
    }

    /**
     * This method handles the sorting algorithm.
     */
    public void sort() {
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < arr.length; i++) {
                int j = i;
                while (j >= gap && arr[j].compareTo(arr[j - gap]) < 0) {
                    T temp = arr[j];
                    arr[j] = arr[j - gap];
                    arr[j - gap] = temp;
                    j -= gap;
                }
            }
        }
    }
}
