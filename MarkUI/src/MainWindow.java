import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.io.IOException;
import java.time.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

//CHAT RELATED ---------------------------
import java.net.*;
import java.io.*;
//----------------------------------------

/**
 * MainWindow.java
 *
 * The main class for the application.
 * This class stores data and handles processes required to display GUI elements and necessary functionality.
 *
 * Version 1.00.
 * Author: Ryhan Khan.
 */
public class MainWindow extends javax.swing.JFrame {

    private Socket socket = null;
//    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ChatClientThread1 client = null;
    private String serverName = "localhost";
    private int serverPort = 4444;

    private JPanel contentPane;
    private JButton buttonCancel, newItemButton, updateButton, retrieveButton, returnButton, collectionButton, removeButton,
            sortRandomButton, mostlySortButton, reverseSortButton, processButton, preOrderButton, inOrderButton, hashSaveButton, hashDisplayButton, postOrderButton, graphicalButton, sortTitleButton, sortAuthorButton, sortBarcodeButton, searchButton;
    private JTextField titleTextField, authorTextField, xTextField, yTextField, barcodeTextField, sortTextField, findBarcodeTextField, searchTextField, sectionTextField;
    private JTextArea descriptionTextArea, processTextArea;
    private JScrollPane tableScrollPane;
    private JLabel serverStatusText;
    private DataManager dataManager;
    private ArchiveCDTable tableModel;
    public SaveData fileData;

    private BinaryTree binaryTree;
    private DLList doublyLinkedList;

    private enum FindBarCodeState {
        None,
        LinkedList,
        BinaryTree;
    }

    private FindBarCodeState barCodeState = FindBarCodeState.None;

    public MainWindow() {
        setContentPane(contentPane);
        setTitle("Archive Console"); // Sets the label of the window.

        binaryTree = new BinaryTree();
        doublyLinkedList = new DLList();

        // Binds the search functionality to the search button.
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onSearch();
            }
        });

        // Binds the window close functionality to the cancel button.
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onCancel();
            }
        });

        // Binds the binary tree pre-order functionality to the pre-order button.
        preOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onPreOrder();
            }
        });

        // Binds the binary tree in-order functionality to the in-order button.
        inOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onInOrder();
            }
        });

        // Binds the binary tree post-order functionality to the post-order button.
        postOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onPostOrder();
            }
        });

        // Binds the graphics functionality to the graphical button.
        graphicalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onGraphical();
            }
        });

        // Binds the hashmap saving functionality to the save button.
        hashSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onHashSave();
            }
        });

        // Binds the hashmap displaying functionality to the display button.
        hashDisplayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onHashDisplay();
            }
        });

        // Binds the new item functionality to the new button.
        newItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onNewItem();
            }
        });

        // Binds the update/save functionality to the update button.
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onUpdate();
            }
        });

        // Binds the sort by title functionality to the title sort button.
        sortTitleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onSortTitle();
            }
        });

        // Binds the sort by author functionality to the author sort button.
        sortAuthorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) { onSortAuthor(); }
        });

        // Binds the sort by barcode functionality to the barcode sort button.
        sortBarcodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onSortBarcode();
            }
        });

        // Binds the automation retrieve functionality to the retrieve button.
        retrieveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onRetrieve();
            }
        });

        // Binds the automation return functionality to the return button.
        returnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onReturn();
            }
        });

        // Binds the automation remove functionality to the remove button.
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onRemove();
            }
        });

        // Binds the automation 'add to collection' functionality to the collection button.
        collectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onCollection();
            }
        });

        // Binds the process log functionality to the process log button.
        processButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onProcessLog();
            }
        });

        mostlySortButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onMostlySort();
            }
        });

        sortRandomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onRandomSort();
            }
        });

        reverseSortButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onReverseSort();
            }
        });

        // Binds the 'find bar code' functionality to the find bar code text field.
        findBarcodeTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                findBarCode(findBarcodeTextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                findBarCode(findBarcodeTextField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                findBarCode(findBarcodeTextField.getText());
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

        connect(serverName, serverPort);
    }

    /**
     * The search button on-click method.
     * This method gets called each time the user presses the search button.
     */
    private void onSearch() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel.model); // Creates a new table row sorter using the Archive CD table model.
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchTextField.getText())); // Adds the search text field to the filter. The '(?i)' ensures that the filter is case-insensitive.

        tableModel.getTable().setRowSorter(sorter); // Applies the filter to the table model and sorts the table rows accordingly.
    }

    /**
     * The cancel button on-click method.
     * This method gets called each time the user presses the cancel button.
     */
    private void onCancel() {
        dispose(); // Frees resources and destroys the window.
    }

    /**
     * The graphical button on-click method.
     * This method gets called each time the user presses the graphical button.
     */
    private void onGraphical() {
        SecondaryWindow dialog = new SecondaryWindow();
        dialog.pack();
        dialog.setVisible(true);
    }

    private void onProcessLog() {
        processTextArea.setText("");
        barCodeState = FindBarCodeState.LinkedList;

        processTextArea.setText(doublyLinkedList.display());
    }

    /**
     * The table on-click method.
     * This method gets called each time the user presses a table row.
     *
     * This will set the details panel fields with the table row that has been selected.
     */
    private void onTableClick() {
        titleTextField.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 0).toString()); // Sets the 'title' text field to the table column.
        authorTextField.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 1).toString()); // Sets the 'author' text field to the table column.
        sectionTextField.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 2).toString()); // Sets the 'section' text field to the table column.

        xTextField.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 3).toString()); // Sets the 'x' text field to the table column.
        yTextField.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 4).toString()); // Sets the 'y' text field to the table column.
        barcodeTextField.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 5).toString()); // Sets the 'barcode' text field to the table column.
        descriptionTextArea.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 6).toString()); // Sets the 'description' text field to the table column.
    }

    /**
     * The new button on-click method.
     * This method gets called each time the user presses the new button.
     *
     * This will reset all fields in the details panel to a blank state.
     */
    private void onNewItem() {
        tableModel.getTable().setSelectionMode(0); // Deselects the Jtable selection.

        titleTextField.setText(null); // Clears the 'title' text field.
        authorTextField.setText(null); // Clears the 'author' text field.
        sectionTextField.setText(null); // Clears the 'section' text field.

        xTextField.setText(null); // Clears the 'x' text field.
        yTextField.setText(null); // Clears the 'y' text field.
        barcodeTextField.setText(null); // Clears the 'barcode' text field.
        descriptionTextArea.setText(null); // Clears the 'description' text field.
    }

    /**
     *  The update/save button on-click method.
     *  This method gets called each time the user presses the update/save button.
     *
     *  This will either update the selected table row from the table or add a new item depending on whether the new button has been pressed.
     */
    private void onUpdate() {
        // Checks if the text fields that must only have numerical values contains any alphabetical characters.
        if (xTextField.getText().matches(".*[a-z].*"))
        {
            // Returns an error message.
            JOptionPane.showMessageDialog(this, "x-value cannot contain characters other than numerical values");
            return; // Exits the method to prevent any changes.
        }
        else if (yTextField.getText().matches(".*[a-z].*"))
        {
            // Returns an error message.
            JOptionPane.showMessageDialog(this, "y-value cannot contain characters other than numerical values");
            return; // Exits the method to prevent any changes.
        }
        else if (barcodeTextField.getText().matches(".*[a-z].*"))
        {
            // Returns an error message.
            JOptionPane.showMessageDialog(this, "Barcode cannot contain characters other than numerical values");
            return; // Exits the method to prevent any changes.
        }

        // Checks if an item in the table is selected.
        if (!tableModel.getTable().getSelectionModel().isSelectionEmpty())
        {
            tableModel.getTable().getModel().setValueAt(titleTextField.getText(), tableModel.getTable().getSelectedRow(), 0); // Updates the table row 'title' column.
            tableModel.getTable().getModel().setValueAt(authorTextField.getText(), tableModel.getTable().getSelectedRow(), 1); // Updates the table row 'author' column.
            tableModel.getTable().getModel().setValueAt(sectionTextField.getText(), tableModel.getTable().getSelectedRow(), 2); // Updates the table row 'section' column.
            tableModel.getTable().getModel().setValueAt(xTextField.getText(), tableModel.getTable().getSelectedRow(), 3); // Updates the table row 'x' column.
            tableModel.getTable().getModel().setValueAt(yTextField.getText(), tableModel.getTable().getSelectedRow(), 4); // Updates the table row 'y' column.
            tableModel.getTable().getModel().setValueAt(barcodeTextField.getText(), tableModel.getTable().getSelectedRow(), 5); // Updates the table row 'barcode' column.
            tableModel.getTable().getModel().setValueAt(descriptionTextArea.getText(), tableModel.getTable().getSelectedRow(), 6); // Updates the table row 'description' column.

            fileData.dataCollection[tableModel.getTable().getSelectedRow()] = new ArchiveCD(tableModel.getTable().getSelectedRow(),
                    titleTextField.getText(),
                    authorTextField.getText(),
                    sectionTextField.getText().charAt(0),
                    Integer.parseInt(xTextField.getText()),
                    Integer.parseInt(yTextField.getText()),
                    Integer.parseInt(barcodeTextField.getText()),
                    descriptionTextArea.getText(),
                    false
            );

            dataManager.saveFile(fileData.dataCollection);

            return; // Exits the method to prevent further changes.
        }

        fileData.count++; // Increases the number of data entries.

        // Adds a new entry to the data collection.
        fileData.dataCollection[fileData.count - 1] = new ArchiveCD(fileData.count,
                 titleTextField.getText(),
            authorTextField.getText(),
            sectionTextField.getText().charAt(0),
            Integer.parseInt(xTextField.getText()),
            Integer.parseInt(yTextField.getText()),
            Integer.parseInt(barcodeTextField.getText()),
            descriptionTextArea.getText(),
                 false
        );

        dataManager.saveFile(fileData.dataCollection);
        System.out.println("Saved Data File");

        tableModel.updateTable(fileData.toObjectArray()); // Refreshes the table to display the new entry.
    }

    private void onMostlySort() {
        if (isNullOrEmpty(sortTextField.getText()))
        {
            JOptionPane.showMessageDialog(this, "Please enter a section.");
            return;
        }

        send(" ;Mostly Sort;" + sortTextField.getText().charAt(0));
    }

    private void onRandomSort() {
        if (isNullOrEmpty(sortTextField.getText()))
        {
            JOptionPane.showMessageDialog(this, "Please enter a section.");
            return;
        }

        send(";Random Sort;" + sortTextField.getText().charAt(0));
    }

    private void onReverseSort() {
        if (isNullOrEmpty(sortTextField.getText()))
        {
            JOptionPane.showMessageDialog(this, "Please enter a section.");
            return;
        }

        send(";Reverse Sort;" + sortTextField.getText().charAt(0));
    }

    /**
     * The title sort button on-click method.
     * The method that gets called each time the user presses the title sort button.
     */
    private void onSortTitle() {
        tableModel.updateTable(tableModel.shellSortByTitle(fileData.dataCollection)); // Refreshes the table to display the sorted data.

        binaryTree = new BinaryTree();

        for (int i = 0; i < fileData.count; i++)
        {
            String nodeValue = fileData.dataCollection[i].getBarcode() + " " + fileData.dataCollection[i].getTitle();
            binaryTree.addNode(i, nodeValue);
        }
    }

    /**
     * The author sort button on-click method.
     * The method that gets called each time the user presses the author sort button.
     */
    private void onSortAuthor() {
        tableModel.updateTable(tableModel.shellSortByAuthor(fileData.dataCollection)); // Refreshes the table to display the sorted data.
    }

    /**
     * The barcode sort button on-click method.
     * The method that gets called each time the user presses the barcode sort button.
     */
    private void onSortBarcode() {
        tableModel.updateTable(tableModel.shellSortByBarcode(fileData.dataCollection)); // Refreshes the table to display the sorted data.
    }

    private void onInOrder() {
        processTextArea.setText("");
        binaryTree.inOrderTraverseTree(binaryTree.root, processTextArea);

        barCodeState = FindBarCodeState.BinaryTree;
    }

    private void onPreOrder() {
        processTextArea.setText("");
        binaryTree.preorderTraverseTree(binaryTree.root, processTextArea);

        barCodeState = FindBarCodeState.BinaryTree;
    }

    private void onPostOrder() {
        processTextArea.setText("");
        binaryTree.postOrderTraverseTree(binaryTree.root, processTextArea);

        barCodeState = FindBarCodeState.BinaryTree;
    }

    private void onHashSave() {
        binaryTree.genHashMap(binaryTree.root);
    }

    private void onHashDisplay() {
        processTextArea.setText(binaryTree.hashDisplay());
    }

    private void onRetrieve() {
        // Checks if any of the text fields are blank/no CD is selected.
        if (isNullOrEmpty(barcodeTextField.getText()) || isNullOrEmpty(titleTextField.getText()) || isNullOrEmpty(sectionTextField.getText()))
        {
            JOptionPane.showMessageDialog(this, "No CD is selected.");
            return;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy - h:mma");

        String output = LocalDateTime.now().format(dtf) + " - SENT - Retrieve Item - " + barcodeTextField.getText() + " " + titleTextField.getText();

        doublyLinkedList.insertAtEnd(output + "\n");
        processTextArea.setText(doublyLinkedList.display());

        String message = ";" + barcodeTextField.getText() + ";" + sectionTextField.getText() + ";Retrieve;" + titleTextField.getText() + ";" + tableModel.getTable().getSelectedRow();
        send(message);
    }

    private void onReturn() {
        // Checks if any of the text fields are blank/no CD is selected.
        if (isNullOrEmpty(barcodeTextField.getText()) || isNullOrEmpty(titleTextField.getText()) || isNullOrEmpty(sectionTextField.getText()))
        {
            JOptionPane.showMessageDialog(this, "No CD is selected.");
            return;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy - h:mma");

        String output = LocalDateTime.now().format(dtf) + " - SENT - Return Item - " + barcodeTextField.getText() + " " + titleTextField.getText();

        doublyLinkedList.insertAtEnd(output + "\n");
        processTextArea.setText(doublyLinkedList.display());

        String message = ";" + barcodeTextField.getText() + ";" + sectionTextField.getText() + ";Return;" + titleTextField.getText() + ";" + tableModel.getTable().getSelectedRow();
        send(message);
    }

    private void onCollection() {
        // Checks if any of the text fields are blank/no CD is selected.
        if (isNullOrEmpty(barcodeTextField.getText()) || isNullOrEmpty(titleTextField.getText()) || isNullOrEmpty(sectionTextField.getText()))
        {
            JOptionPane.showMessageDialog(this, "No CD is selected.");
            return;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy - h:mma");

        String output = LocalDateTime.now().format(dtf) + " - SENT - Add Item -" + barcodeTextField.getText() + " " + titleTextField.getText();

        doublyLinkedList.insertAtEnd(output + "\n");
        processTextArea.setText(doublyLinkedList.display());

        String message = ";" + barcodeTextField.getText() + ";" + sectionTextField.getText() + ";Add;" + titleTextField.getText() + ";" + tableModel.getTable().getSelectedRow();
        send(message);
    }

    private void onRemove() {
        // Checks if any of the text fields are blank/no CD is selected.
        if (isNullOrEmpty(barcodeTextField.getText()) || isNullOrEmpty(titleTextField.getText()) || isNullOrEmpty(sectionTextField.getText()))
        {
            JOptionPane.showMessageDialog(this, "No CD is selected.");
            return;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy - h:mma");

        String output = LocalDateTime.now().format(dtf) + " - SENT - Remove Item -" + barcodeTextField.getText() + " " + titleTextField.getText();

        doublyLinkedList.insertAtEnd(output + "\n");
        processTextArea.setText(doublyLinkedList.display());

        String message = ";" + barcodeTextField.getText() + ";" + sectionTextField.getText() + ";Remove;" + titleTextField.getText() + ";" + tableModel.getTable().getSelectedRow();
        send(message);
    }

    private void findBarCode(String value) {
        switch (barCodeState)
        {
            case None:
                return;
            case BinaryTree:
//                binaryTree.findNode(value);
                break;
            case LinkedList:
                doublyLinkedList.find(value);
                break;
        }
    }

    /**
     * Initializes the window UI components.
     * This method creates the initial table model and loads data from the .txt file.
     */
    private void createUIComponents() {
        dataManager = new DataManager("CD_ArchivePrototype_SampleData.txt"); // Creates a new data manager and loads the data file name.
        fileData = dataManager.loadFile(); // Reads data from the loaded file.

        // Checks if the loaded data is not valid.
        if (fileData.dataCollection == null)
        {
            JOptionPane.showMessageDialog(this, "Error loading file with name: '" + dataManager.getFileName() + "'. Loading sample data."); // Outputs a warning message.

            // Generates sample data.
            Object[][] sampleData = {
                    {"Kathy", "Smith",
                            "Snowboarding", 'C', 5, 5, 5, false},
                    {"Kathy", "Smith",
                            "Snowboarding", 'B', 5, 5, 5, true},
                    {"Kathy", "Smith",
                            "Snowboarding", 'L', 5, 5, 5, false}
            };

            tableModel = new ArchiveCDTable(sampleData, -1); // Loads the table model with sample data.
        }
        else
        {
            tableModel = new ArchiveCDTable(fileData.toObjectArray(), -1); // Loads the table model with file data.
        }

        tableScrollPane = new JScrollPane(tableModel.getTable()); // Binds the table to a scroll pane.

        processTextArea = new JTextArea();
        processTextArea.setEditable(false);
    }

    public void connect(String serverName, int serverPort)
    {
        System.out.println("Establishing connection. Please wait ...");
        try
        {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            open();
        }
        catch (UnknownHostException uhe)
        {
            System.out.println("Host unknown: " + uhe.getMessage());
        }
        catch (IOException ioe)
        {
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
    }

    public void open()
    {
        try
        {
            streamOut = new DataOutputStream(socket.getOutputStream());
            client = new ChatClientThread1(this, socket);
        }
        catch (IOException ioe)
        {
            System.out.println("Error opening output stream: " + ioe);
        }
    }

    public void close()
    {
        try
        {
            if (streamOut != null)
            {
                streamOut.close();
            }
            if (socket != null)
            {
                socket.close();
            }
        }
        catch (IOException ioe)
        {
            System.out.println("Error closing ...");
        }
        client.close();
        client.interrupt();
    }

    private void send(String msg) {
        try
        {
            streamOut.writeUTF(msg);
            streamOut.flush();
        }
        catch (IOException ioe)
        {
            System.out.println("Sending error: " + ioe.getMessage());
            close();
        }
    }

    public void handle(String msg)
    {
        if (msg.equals(".bye"))
        {
            System.out.println("Good bye. Press EXIT button to exit ...");
            close();
        }
        else
        {
            String[] temp = msg.split(";");

            doublyLinkedList.insertAtEnd(temp[0] + "\n");
            processTextArea.setText(doublyLinkedList.display());

            tableModel.getTable().setRowSelectionInterval(Integer.parseInt(temp[1]), Integer.parseInt(temp[1]));

            if (msg.contains("Retrieve"))
            {
                tableModel.getTable().setValueAt(true, Integer.parseInt(temp[1]), 7);
            }
            else if (msg.contains("Return"))
            {
                tableModel.getTable().setValueAt(false, Integer.parseInt(temp[1]), 7);
            }
            else if (msg.contains("Remove"))
            {
                tableModel.getTable().remove(Integer.parseInt(temp[1]));
            }
        }
    }

    /**
     * This method checks if a string is null or empty.
     *
     * @param text this parameter takes in a string that is used for checking.
     * @return the result of the null/empty check.
     */
    private boolean isNullOrEmpty(String text)
    {
        if (text == null || text.isEmpty()) return true;
        return false;
    }
}
