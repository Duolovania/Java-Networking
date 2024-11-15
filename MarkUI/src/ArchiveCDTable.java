import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * ArchiveCDTable.java
 *
 * The archive CD table class for the application.
 * This class handles the structure/layout of the data table, as well as the sorting functionality that is used in the program.
 *
 * Version 1.00.
 * Author: Ryhan Khan.
 */
public class ArchiveCDTable {
    private JTable m_Table;
    private Object[][] tableData;
    private String[] columnNames;
    DefaultTableModel model;

    /**
     * This constructor is used to generate the JTable and define the column header names.
     *
     * @param data The data that is loaded into each table row.
     * @param columns The number of columns that will be displayed on the table. Default = -1.
     */
    public ArchiveCDTable(Object[][] data, int columns) {
        String[] tempColumns = {
                "Title",
                "Author",
                "Section",
                "X",
                "Y",
                "Barcode",
                "Description",
                "On-Loan"
        };

        if (columns == -1) columns = tempColumns.length; // Default number of columns can be used if -1 is input into the method.

        columnNames = new String[columns]; // A new list of column names based on the specified number of columns.

        for (int i = 0; i < columns; i++)
        {
            columnNames[i] = tempColumns[i];
        }

        tableData = data;

        // Formats the table structure.
        model = new DefaultTableModel(tableData, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 2:
                        return char.class;
                    case 3:
                        return int.class;
                    case 4:
                        return int.class;
                    case 5:
                        return int.class;
                    case 7:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }
        };

        m_Table = new JTable(model);
    }

    /**
     * This method retrieves the JTable object.
     * @return the JTable object.
     */
    public JTable getTable() {
        return m_Table;
    }

    /**
     * This method changes the contents within the JTable without affecting the format.
     * @param newData the 2-Dimensional Object array that is replacing the original data.
     */
    public void updateTable(Object[][] newData) {
        model.setDataVector(newData, columnNames);
        m_Table.setModel(model); // Replaces the original table model with a new model.
    }

    /**
     * This method sorts an ArchiveCD array by its title using the shell sort algorithm.
     * Note: This used for reading purposes and does not affect the order of the original data collection.
     *
     * @param originalArray the ArchiveCD array that is being sorted.
     *
     * @return a 2-Dimensional Object Array that can be used by Java UI elements.
     */
    public Object[][] shellSortByTitle(ArchiveCD[] originalArray) {
        int arrayLength = 0;
        for (int i = 0; i < originalArray.length; i++) if (originalArray[i] != null) arrayLength++; // Counts how many valid items are in the array.

        String[] tempArr = new String[arrayLength]; // Creates an array to store CD titles.
        ArchiveCD[] sortedArray = new ArchiveCD[originalArray.length]; // Sets the sorted array

        // Retrieves each title from the collection.
        for (int i = 0; i < tempArr.length; i++)
        {
            tempArr[i] = originalArray[i].getTitle();
        }

        new ShellSort<>(tempArr).sort(); //  Runs the shell sort algorithm on the temp array.

        // Cycles through each title.
        for (int i = 0; i < tempArr.length; i++)
        {
            String tempTitle  = tempArr[i]; // Temporary title to be used in lambda function.
            Optional<ArchiveCD> result = Arrays.stream(originalArray).filter(item -> item.getTitle().equals(tempTitle)).findFirst(); // Finds a CD with a title that matches the sorted title.
            if (result.isPresent()) sortedArray[i] = result.get(); // Sets the current CD to the correct CD in the sorted list of titles.
        }

        return toObjectArray(sortedArray); // Returns an object array
    }

    /**
     * This method sorts an ArchiveCD array by its title.
     * Note: This used for reading purposes and does not affect the order of the original data collection.
     *
     * @param originalArray the ArchiveCD array that is being sorted.
     *
     * @return a 2-Dimensional Object Array that can be used by Java UI elements.
     */
    public Object[][] sortByTitle(ArchiveCD[] originalArray) {

        int arrayLength = 0;
        for (int i = 0; i < originalArray.length; i++) if (originalArray[i] != null) arrayLength++; // Counts how many valid items are in the array.

        String[] tempArr = new String[arrayLength]; // Creates an array to store CD titles.
        ArchiveCD[] sortedArray = new ArchiveCD[originalArray.length]; // Sets the sorted array

        // Retrieves each title from the collection.
        for (int i = 0; i < tempArr.length; i++)
        {
            tempArr[i] = originalArray[i].getTitle();
        }

        Arrays.sort(tempArr); // Sorts the array.

        // Cycles through each title.
        for (int i = 0; i < tempArr.length; i++)
        {
            String tempTitle  = tempArr[i]; // Temporary title to be used in lambda function.
            Optional<ArchiveCD> result = Arrays.stream(originalArray).filter(item -> item.getTitle().equals(tempTitle)).findFirst(); // Finds a CD with a title that matches the sorted title.
            if (result.isPresent()) sortedArray[i] = result.get(); // Sets the current CD to the correct CD in the sorted list of titles.
        }

        return toObjectArray(sortedArray); // Returns an object array
    }

    /**
     * This method sorts an ArchiveCD array by its author name using the shell sort algorithm.
     * Note: This used for reading purposes and does not affect the order of the original data collection.
     *
     * @param originalArray the ArchiveCD array that is being sorted.
     *
     * @return a 2-Dimensional Object Array that can be used by Java UI elements.
     */
    public Object[][] shellSortByAuthor(ArchiveCD[] originalArray) {

        int arrayLength = 0;
        for (int i = 0; i < originalArray.length; i++) if (originalArray[i] != null) arrayLength++; // Counts how many valid items are in the array.

        String[] tempArr = new String[arrayLength]; // Creates an array to store CD authors.
        ArchiveCD[] sortedArray = new ArchiveCD[originalArray.length]; // Sets the sorted array

        // Retrieves each author from the collection.
        for (int i = 0; i < tempArr.length; i++)
        {
            tempArr[i] = originalArray[i].getAuthor();
        }

        new ShellSort<>(tempArr).sort(); //  Runs the shell sort algorithm on the temp array.

        // Cycles through each title.
        for (int i = 0; i < tempArr.length; i++)
        {
            String tempAuthor  = tempArr[i]; // Temporary author to be used in lambda function.
            Optional<ArchiveCD> result = Arrays.stream(originalArray).filter(item -> item.getAuthor().equals(tempAuthor)).findFirst(); // Finds a CD with an author that matches the sorted authors.
            if (result.isPresent()) sortedArray[i] = result.get(); // Sets the current CD to the correct CD in the sorted list of authors.
        }

        return toObjectArray(sortedArray); // Returns an object array
    }

    /**
     * This method sorts an ArchiveCD array by its author name.
     * Note: This used for reading purposes and does not affect the order of the original data collection.
     *
     * @param originalArray the ArchiveCD array that is being sorted.
     *
     * @return a 2-Dimensional Object Array that can be used by Java UI elements.
     */
    public Object[][] sortByAuthor(ArchiveCD[] originalArray) {

        int arrayLength = 0;
        for (int i = 0; i < originalArray.length; i++) if (originalArray[i] != null) arrayLength++; // Counts how many valid items are in the array.

        String[] tempArr = new String[arrayLength]; // Creates an array to store CD authors.
        ArchiveCD[] sortedArray = new ArchiveCD[originalArray.length]; // Sets the sorted array

        // Retrieves each author from the collection.
        for (int i = 0; i < tempArr.length; i++)
        {
            tempArr[i] = originalArray[i].getAuthor();
        }

        Arrays.sort(tempArr); // Sorts the array.

        // Cycles through each title.
        for (int i = 0; i < tempArr.length; i++)
        {
            String tempAuthor  = tempArr[i]; // Temporary author to be used in lambda function.
            Optional<ArchiveCD> result = Arrays.stream(originalArray).filter(item -> item.getAuthor().equals(tempAuthor)).findFirst(); // Finds a CD with an author that matches the sorted authors.
            if (result.isPresent()) sortedArray[i] = result.get(); // Sets the current CD to the correct CD in the sorted list of authors.
        }

        return toObjectArray(sortedArray); // Returns an object array
    }


    public Object[][] insertionSortForSection(char section, ArchiveCD[] originalArray) {

        int arrayLength = 0;
        ArrayList<ArchiveCD> tempStringList = new ArrayList<>();

        for (int i = 0; i < originalArray.length; i++)
        {
            if (originalArray[i] != null)
            {
                if (originalArray[i].getSection() == section)
                {
                    tempStringList.add(originalArray[i]);
                    arrayLength++; // Counts how many valid items are in the array.

                }

            }
        }

        String[] tempArr = new String[arrayLength]; // Creates an array to store CD barcodes.
        ArchiveCD[] sortedArray = new ArchiveCD[originalArray.length]; // Sets the sorted array

        for (int i = 0; i < tempStringList.toArray().length; i++)
        {
            tempArr[i] = tempStringList.get(i).getTitle();
        }

        InsertionSort sortAlgorithm = new InsertionSort();
        sortAlgorithm.insertionSort(tempArr); //  Runs the shell sort algorithm on the temp array.

        // Cycles through each title.
        for (int i = 0; i < tempArr.length; i++)
        {
            String tempTitle  = tempArr[i]; // Temporary author to be used in lambda function.
            Optional<ArchiveCD> result = Arrays.stream(originalArray).filter(item -> item.getTitle().equals(tempTitle)).findFirst(); // Finds a CD with an author that matches the sorted authors.
            if (result.isPresent()) sortedArray[i] = result.get(); // Sets the current CD to the correct CD in the sorted list of authors.
        }

        return toObjectArray(sortedArray); // Returns an object array
    }

    /**
     * This method sorts an ArchiveCD array by its barcode number using the shell sort algorithm.
     * Note: This used for reading purposes and does not affect the order of the original data collection.
     *
     * @param originalArray the ArchiveCD array that is being sorted.
     *
     * @return a 2-Dimensional Object Array that can be used by Java UI elements.
     */
    public Object[][] shellSortByBarcode(ArchiveCD[] originalArray) {

        int arrayLength = 0;
        for (int i = 0; i < originalArray.length; i++) if (originalArray[i] != null) arrayLength++; // Counts how many valid items are in the array.

        Integer[] tempArr = new Integer[arrayLength]; // Creates an array to store CD barcodes.
        ArchiveCD[] sortedArray = new ArchiveCD[originalArray.length]; // Sets the sorted array

        // Retrieves each barcode from the collection.
        for (int i = 0; i < tempArr.length; i++)
        {
            tempArr[i] = originalArray[i].getBarcode();
        }

        new ShellSort<>(tempArr).sort(); //  Runs the shell sort algorithm on the temp array.

        // Cycles through each title.
        for (int i = 0; i < tempArr.length; i++)
        {
            int tempAuthor  = tempArr[i]; // Temporary barcode to be used in lambda function.
            Optional<ArchiveCD> result = Arrays.stream(originalArray).filter(item -> item.getBarcode() == tempAuthor).findFirst(); // Finds a CD with a barcode that matches the sorted barcode.
            if (result.isPresent()) sortedArray[i] = result.get(); // Sets the current CD to the correct CD in the sorted list of barcodes.
        }

        return toObjectArray(sortedArray); // Returns an object array
    }

    /**
     * This method sorts an ArchiveCD array by its barcode number.
     * Note: This used for reading purposes and does not affect the order of the original data collection.
     *
     * @param originalArray the ArchiveCD array that is being sorted.
     *
     * @return a 2-Dimensional Object Array that can be used by Java UI elements.
     */
    public Object[][] sortByBarcode(ArchiveCD[] originalArray) {

        int arrayLength = 0;
        for (int i = 0; i < originalArray.length; i++) if (originalArray[i] != null) arrayLength++; // Counts how many valid items are in the array.

        int[] tempArr = new int[arrayLength]; // Creates an array to store CD barcodes.
        ArchiveCD[] sortedArray = new ArchiveCD[originalArray.length]; // Sets the sorted array

        // Retrieves each barcode from the collection.
        for (int i = 0; i < tempArr.length; i++)
        {
            tempArr[i] = originalArray[i].getBarcode();
        }

        Arrays.sort(tempArr); // Sorts the array.

        // Cycles through each title.
        for (int i = 0; i < tempArr.length; i++)
        {
            int tempAuthor  = tempArr[i]; // Temporary barcode to be used in lambda function.
            Optional<ArchiveCD> result = Arrays.stream(originalArray).filter(item -> item.getBarcode() == tempAuthor).findFirst(); // Finds a CD with a barcode that matches the sorted authors.
            if (result.isPresent()) sortedArray[i] = result.get(); // Sets the current CD to the correct CD in the sorted list of barcodes.
        }

        return toObjectArray(sortedArray); // Returns an object array
    }

    /**
     * Converts an ArchiveCD array to a 2-Dimensional Object Array.
     *
     * @param dataCollection The array that is being converted.
     * @return An array converted to a suitable format that can be used in the application.
     */
    public Object[][] toObjectArray(ArchiveCD[] dataCollection) {
        int arrayLength = 0; // Sets the number of rows to 0 by default.
        for (int i = 0; i < dataCollection.length; i++) if (dataCollection[i] != null) arrayLength++; // Counts how many valid items are in the array. 'Valid items' are items that are not null.

        int columns = 9; // Sets the number of columns.
        int rows = arrayLength; // Sets the number of rows to the number of valid items in the collection. This is so that the table does not show blank entries.

        Object[][] data = new Object[rows][columns]; // Creates a blank 2D object array with the correct size.

        for (int i = 0; i < rows; i++) {
            data[i][0] = dataCollection[i].getTitle(); // Sets the 'title' column.
            data[i][1] = dataCollection[i].getAuthor(); // Sets the 'author' column.
            data[i][2] = dataCollection[i].getSection(); // Sets the 'section' column.
            data[i][3] = dataCollection[i].getX(); // Sets the 'x' column.
            data[i][4] = dataCollection[i].getY(); // Sets the 'y' column.
            data[i][5] = dataCollection[i].getBarcode(); // Sets the 'barcode' column.
            data[i][6] = dataCollection[i].getDescription(); // Sets the 'description' column.
            data[i][7] = dataCollection[i].getLoan(); // Sets the 'loan' column.
        }

        return data; // Returns the newly generated object array.
    }
}
