import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

/****************************************************************
 PROGRAM:   SecondaryWindow.java
 AUTHOR:    Ryhan Khan
 DUE DATE:  15/11/2024

 FUNCTION:  This class stores data and handles processes required to display GUI elements and necessary functionality for the Automation Console.
 ****************************************************************/
public class SecondaryWindow extends JFrame {
    private Socket socket = null;
    private DataOutputStream streamOut = null;
    private SecondWindowThread client = null;
    private String serverName = "localhost";
    private int serverPort = 4444;

    private JPanel contentPane;
    private JButton buttonCancel, addItemButton, processButton;
    private JTextField barcodeTextField, sectionTextField, textField3, textField4, textField5, textField6, textField7, textField8;
    private JTextArea textArea1;
    private JComboBox actionsDropdown;
    private JScrollPane tableScrollPane;
    private JLabel serverStatusText, sortText;
    private ArchiveCDTable tableModel;

    private ArchiveCD selectedCD;
    private SaveData fileData;
    private char sortSection;

    public SecondaryWindow() {
        setContentPane(contentPane);
        setSize(800, 600);
        setTitle("Automation Console");

        tableModel.getTable().setRowSelectionInterval(0, 0); // Selects the first item on the table by default.

        // Binds the window close functionality to the cancel button.
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onCancel();
            }
        });

        // Binds the process functionality to the process button.
        processButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onProcess();
            }
        });

        // Binds the hide/show add item button functionality to the section text field.
        sectionTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkAddVisibility();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkAddVisibility();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkAddVisibility();
            }
        });

        // Binds the add item functionality to the add item button.
        addItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onAddItem();
            }
        });

        // Binds the table clicking functionality to the table model.
        tableModel.getTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { onTableClick(); }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onCancel();
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        connect(serverName, serverPort); // connects this program to the server.
    }

    /**
     * The cancel button on-click method.
     * This method gets called each time the user presses the cancel button.
     */
    private void onCancel() {
        dispose(); // Clears window resources.
    }

    /**
     * Establishes whether the 'add item' button will be selectable.
     * This method is called each time the user enters data into the section text field.
     */
    private void checkAddVisibility() {
        // Checks if the section text field matches the selected section in the table.
        if (sectionTextField.getText().equals(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 2).toString()))
        {
            addItemButton.setEnabled(false); // disables the button.
            return;
        }

        addItemButton.setEnabled(true); // enables the button.
    }

    /**
     * The add item button on-click method.
     * This method gets called each time the user presses the add item button.
     */
    private void onAddItem() {

    }

    /**
     * The process button on-click method.
     * This method gets called each time the user presses the process button.
     *
     * This will send a message back to the Archive CD Console.
     */
    private void onProcess() {
        send();
    }

    private void onTableClick() {
        sectionTextField.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 2).toString()); // Sets the 'section' text field to the table column.
        barcodeTextField.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 5).toString()); // Sets the 'barcode' text field to the table column.

        // Creates a new CD object.
        selectedCD = new ArchiveCD(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 1).toString(), tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 3).toString().charAt(0), Integer.parseInt(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 5).toString()));
    }

    /**
     * Initializes the window UI components.
     * This method creates the initial table model and loads data from the .txt file.
     */
    private void createUIComponents() {
        DataManager dataManager = new DataManager("CD_ArchivePrototype_SampleData.txt");
        fileData = dataManager.loadFile(); // Loads data from the selected file.

        tableModel = new ArchiveCDTable(fileData.toObjectArray(), 7); // Creates the table.
        tableScrollPane = new JScrollPane(tableModel.getTable()); // Attaches the table to the scroll pane.

        actionsDropdown = new JComboBox();

//        // Binds an action to the dropdown menu.
//        actionsDropdown.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String value = actionsDropdown.getSelectedItem().toString();
//            }
//        });
    }

    /**
     * Connects the program to the server.
     *
     * @param serverName the name of the server connection.
     * @param serverPort the port of the server connection.
     */
    public void connect(String serverName, int serverPort)
    {
        serverStatusText.setText("Establishing connection. Please wait ..."); // Updates server connection status label.

        try
        {
            socket = new Socket(serverName, serverPort);
            serverStatusText.setText("Connected: " + socket);
            open();
        }
        catch (UnknownHostException uhe)
        {
            serverStatusText.setText("Host unknown: " + uhe.getMessage()); // Updates server connection status label with warning message.
        }
        catch (IOException ioe)
        {
            serverStatusText.setText("Unexpected exception: " + ioe.getMessage()); // Updates server connection status label with error message.
        }
    }

    /**
     * Opens the server output stream.
     */
    public void open()
    {
        try
        {
            streamOut = new DataOutputStream(socket.getOutputStream());
            client = new SecondWindowThread(this, socket); // Initializes server client.
        }
        catch (IOException ioe)
        {
            serverStatusText.setText("Error opening output stream: " + ioe); // Updates server connection status label with error message.
        }
    }

    /**
     * Closes the server output stream.
     */
    public void close()
    {
        try
        {
            if (streamOut != null)
            {
                streamOut.close(); // Closes stream.
            }
            if (socket != null)
            {
                socket.close(); // Closes socket.
            }
        }
        catch (IOException ioe)
        {
            serverStatusText.setText("Error closing ..."); // Updates server connection status label with error message.
        }

        client.close(); // Closes client;
        client.interrupt(); // Stops the client.
    }

    /**
     * Sends a message to the server.
     * This message will be sent to all clients of the server.
     */
    private void send() {
        try
        {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy - h:mma"); // Sets the format of the date text.
            String output = LocalDateTime.now().format(dtf) + " - RCVD - " + actionsDropdown.getSelectedItem() + " - "  + selectedCD.getBarcode() + " " + selectedCD.getTitle() + ";" + tableModel.getTable().getSelectedRow();

            streamOut.writeUTF(output); // Writes the message to the output stream.
            streamOut.flush();
            serverStatusText.setText("sent.");
        }
        catch (IOException ioe)
        {
            serverStatusText.setText("Sending error: " + ioe.getMessage()); // Updates server connection status label with error message.
            close(); // Close the connection.
        }
    }

    /**
     * Handles the message from the server.
     *
     * @param msg the message that is received.
     */
    public void handle(String msg)
    {
        // Checks if the message is ".bye".
        if (StringUtils.equals(msg, ".bye"))
        {
            serverStatusText.setText("Good bye. Press EXIT button to exit ...");
            close(); // Closes the connection.
        }
        else
        {
            String[] temp = msg.split(";"); // Splits the message using semicolons as a divider.

            // Checks if a sort action has been requested.
            if (temp[1].equals("Mostly Sort") || temp[1].equals("Random Sort") || temp[1].equals("Reverse Sort"))
            {
                // Updates the text fields.
                sortText.setText(temp[1]);
                sortSection = temp[2].charAt(0);
                actionsDropdown.setSelectedItem("Sort");
                sortTable();
                return;
            }

            // Updates the text fields.
            barcodeTextField.setText(temp[1]);
            sectionTextField.setText(temp[2]);
            actionsDropdown.setSelectedItem(temp[3]);

            selectedCD = new ArchiveCD(temp[4], temp[2].charAt(0), Integer.parseInt(temp[1])); // Creates a CD object using the values from the message.
            tableModel.updateTable(tableModel.toObjectArray(fileData.dataCollection));
            tableModel.getTable().setRowSelectionInterval(Integer.parseInt(temp[5]), Integer.parseInt(temp[5])); // Sets the selected row on the table.

            serverStatusText.setText("received."); // Updates the server connection progress label.
        }
    }

    /**
     * This method will check to see which sorting algorithm should be executed.
     */
    private void sortTable() {
        // Checks which sorting algorithm will be used.
        switch (sortText.getText())
        {
            case "Random Sort":
                randomSort();
                break;
            case "Reverse Sort":
                reverseSort();
                break;
            case "Mostly Sort":
                mostlySort();
                break;
        }
    }

    /**
     * This method will 'mostly' sort the table.
     */
    private void mostlySort() {
        tableModel.updateTable(tableModel.insertionSortForSection(sortSection, fileData.dataCollection)); // Refreshes the table to display the sorted data.
    }

    /**
     * This method will sort the table in reverse.
     */
    private void reverseSort() {
        Object[][] tempArr = tableModel.shellSortByTitle(fileData.dataCollection); // Sorts the array using a shell sort.
        reverseArray(tempArr); // Reverses the array.
        tableModel.updateTable(tempArr); // Refreshes the table to display the sorted data.
    }

    /**
     * This method will reverse an object array.
     *
     * @param row the array that will be sorted.
     */
    private void reverseArray(Object[] row) {
        int left = 0;
        int right = row.length - 1;

        // Loops through each item in the array.
        while (left < right)
        {
            Object temp = row[left]; // Stores the first object.

            // Swaps the items around.
            row[left] = row[right];
            row[right] = temp;

            // Moves onto the next element.
            left++;
            right--;
        }
    }

    /**
     * This method will randomly sort the table.
     */
    private void randomSort() {
        ArrayList<Vector<Object>> rows = new ArrayList<>();

        // Loops through each item in the table.
        for (int i = 0; i < tableModel.getTable().getModel().getRowCount(); i++)
        {
            Vector<Object> row = tableModel.model.getDataVector().get(i); // Gets the table row.
            rows.add(row);
        }

        Collections.shuffle(rows); // Shuffle all rows.

        tableModel.model.setRowCount(0); // Clears the model.

        // Adds shuffled rows back to the table.
        for (Vector<Object> row : rows)
        {
            tableModel.model.addRow(row.toArray());
        }
    }
}
