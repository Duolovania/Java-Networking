/**
 * SaveData.java
 *
 * The save data class for the application.
 * This class stores file data as objects to be used throughout the application.
 *
 * Version 1.00.
 * Author: Ryhan Khan.
 */
public class SaveData
{
    public ArchiveCD[] dataCollection;
    public int count;

    // Converts the ArchiveCD data collection to a 2-Dimensional Object Array.

    /**
     * This method converts the ArchiveCD array to a suitable format that can be used in the application.
     *
     * @return a 2-Dimensional Object Array with the generated table data.
     */
    public Object[][] toObjectArray() {
        int columns = 9; // Sets the number of columns.
        int rows = count; // Sets the number of rows to the number of entries in collection.

        Object[][] data = new Object[rows][columns]; // Creates a blank 2D object array with the correct size.

        // Cycles through each row in the collection.
        for (int i = 0; i < rows; i++) {
//            data[i][0] = dataCollection[i].getID(); // Ignores the 'ID' column.
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
