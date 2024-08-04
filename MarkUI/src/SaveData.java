public class SaveData
{
    public ArchiveCD[] dataCollection;
    public int count;

    // Converts the ArchiveCD data collection to a 2-Dimensional Object Array.
    public Object[][] toObjectArray() {
        int columns = 9;
        int rows = count;

        Object[][] data = new Object[rows][columns];

        for (int i = 0; i < rows; i++) {
//            data[i][0] = dataCollection[i].getID();
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
