import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.*;

/**
 * DataManager.java
 *
 * The file and data manager for re-purposing suggestions application.
 * This class handles the saving and loading functionality for the data stored in the application.
 *
 * Version 1.00.
 * Author: Ryhan Khan.
 */
public class DataManager
{
    private String fileName;

    public DataManager(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * Gets the file name for the save data file.
     *
     * @return the file name that is used to save/load data.
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * Writes newly created data to file.
     *
     * @param newData a collection of suggestions.
     */
    public void saveFile(ArchiveCD[] newData)
    {
        try
        {
            BufferedWriter output = new BufferedWriter(new FileWriter(fileName)); // Retrieves data from the file path.

            output.write("ID;Title;Author;Section;X;Y;BarCode;Description;OnLoan");
            output.newLine(); // Creates a new line.

            for (int i = 0; i < newData.length; i++) // Loops for each item in the data collection.
            {
                if (newData[i] == null) // Checks if the data is valid.
                {
                    break; // Ignores the item.
                }

                output.write(newData[i].toString());
                output.newLine(); // Creates a new line.
            }

            output.close();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage()); // Outputs an error message if an error has occurred.
        }
    }

    /**
     * Writes newly hashmap data to file.
     *
     * @param name the file name.
     * @param hashMap the saved hashmap.
     */
    public void saveHashMap(String name, HashMap<String, List<String>> hashMap)
    {
        try
        {
            BufferedWriter output = new BufferedWriter(new FileWriter(name)); // Retrieves data from the file path.

            // Loops through each item in the hashmap.
            for (Map.Entry<String, List<String>> entry : hashMap.entrySet())
            {
                output.write(entry.getKey() + ": "); // Write the key.
                output.write(String.join(", ", entry.getValue())); // Write the list values, joined by a comma.

                output.newLine(); // Adds a new line.
            }

            System.out.println("HashMap values saved to " + name);
            output.close();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage()); // Outputs an error message if an error has occurred.
        }
    }

    /**
     * Retrieves the data from a file (line-by-line).
     *
     * @return the retrieved data in the form of a SaveData object.
     */
    public SaveData loadFile()
    {
        SaveData data = new SaveData(); // Creates a temporary save data object.

        data.dataCollection = new ArchiveCD[100]; // Sets the data collection to blank.
        data.count = 0; // Sets the data collection amount to none.
        int lineNum = 0;

        try
        {
            BufferedReader input = new BufferedReader(new FileReader(fileName)); // Retrieves data from the file path.
            String line;
            while((line = input.readLine()) != null) // Loops repeats for every line in the file.
            {
                if (lineNum > 0)
                {
                    String[] temp = line.split(";"); // Splits the line by semicolons.
                    boolean onLoan = !temp[8].equals("No");

                    data.dataCollection[data.count] = new ArchiveCD(Integer.parseInt(temp[0]), temp[1], temp[2], temp[3].charAt(0), Integer.parseInt(temp[4]),Integer.parseInt(temp[5]), Integer.parseInt(temp[6]), temp[7], onLoan); // Sets data collection to loaded data.
                    data.count++; // Increases the amount of items in the list.
                }

                lineNum++;
            }

            input.close(); // Closes the file.
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage()); // Outputs an error message.

            data.dataCollection = null;
            data.count = 0;
        }
        return data; // Returns the loaded data.
    }
}