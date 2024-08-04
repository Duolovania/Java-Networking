import javax.swing.*;
import javax.swing.table.*;
import java.util.Arrays;
import java.util.Optional;

public class ArchiveCDTable {
    private JTable m_Table;
    private Object[][] tableData;
    private String[] columnNames;
    DefaultTableModel model;

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
     * This method sorts an ArchiveCD array by its title.
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
     * This method sorts an ArchiveCD array by its author name.
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

    /**
     * This method sorts an ArchiveCD array by its barcode number.
     * @param originalArray the ArchiveCD array that is being sorted.
     *
     * @return a 2-Dimensional Object Array that can be used by Java UI elements.
     */
    public Object[][] sortByBarcode(ArchiveCD[] originalArray) {

        int arrayLength = 0;
        for (int i = 0; i < originalArray.length; i++) if (originalArray[i] != null) arrayLength++; // Counts how many valid items are in the array.

        int[] tempArr = new int[arrayLength]; // Creates an array to store CD authors.
        ArchiveCD[] sortedArray = new ArchiveCD[originalArray.length]; // Sets the sorted array

        // Retrieves each author from the collection.
        for (int i = 0; i < tempArr.length; i++)
        {
            tempArr[i] = originalArray[i].getBarcode();
        }

        Arrays.sort(tempArr); // Sorts the array.

        // Cycles through each title.
        for (int i = 0; i < tempArr.length; i++)
        {
            int tempAuthor  = tempArr[i]; // Temporary author to be used in lambda function.
            Optional<ArchiveCD> result = Arrays.stream(originalArray).filter(item -> item.getBarcode() == tempAuthor).findFirst(); // Finds a CD with an author that matches the sorted authors.
            if (result.isPresent()) sortedArray[i] = result.get(); // Sets the current CD to the correct CD in the sorted list of authors.
        }

        return toObjectArray(sortedArray); // Returns an object array
    }

    // Converts an ArchiveCD array to a 2-Dimensional Object Array.
    private Object[][] toObjectArray(ArchiveCD[] dataCollection) {
        int arrayLength = 0;
        for (int i = 0; i < dataCollection.length; i++) if (dataCollection[i] != null) arrayLength++; // Counts how many valid items are in the array.

        int columns = 9;
        int rows = arrayLength;

        Object[][] data = new Object[rows][columns];

        for (int i = 0; i < rows; i++) {
            data[i][0] = dataCollection[i].getTitle();
            data[i][1] = dataCollection[i].getAuthor();
            data[i][2] = dataCollection[i].getSection();
            data[i][3] = dataCollection[i].getX();
            data[i][4] = dataCollection[i].getY();
            data[i][5] = dataCollection[i].getBarcode();
            data[i][6] = dataCollection[i].getDescription();
            data[i][7] = dataCollection[i].getLoan();
        }

        return data;
    }
}
