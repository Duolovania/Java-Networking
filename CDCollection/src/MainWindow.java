import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

//CHAT RELATED ---------------------------
import java.net.*;
import java.io.*;
//----------------------------------------

/****************************************************************
 PROGRAM:   MainWindow.java
 AUTHOR:    Ryhan Khan
 DUE DATE:  15/11/2024

 FUNCTION:  This class stores data and handles processes required to display GUI elements and necessary functionality for the Archive CD Console.
 ****************************************************************/

public class MainWindow extends javax.swing.JFrame {

    private Socket socket = null;
    private DataOutputStream streamOut = null;
    private MainWindowThread client = null;
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

        binaryTree = new BinaryTree(); // Initializes the binary tree object.
        doublyLinkedList = new DLList(); // Initializes the doubly linked list object.

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

        // Binds the 'mostly sort' functionality to the 'mostly sort' button.
        mostlySortButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onMostlySort();
            }
        });

        // Binds the random sort functionality to the random sort button.
        sortRandomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onRandomSort();
            }
        });

        // Binds the reverse sort functionality to the reverse sort button.
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

        // call onCancel() when cross is clicked.
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
     *
     * This will display a diagram of the binary tree.
     */
    private void onGraphical() {

    }

    /**
     * The process log button on-click method.
     * This method gets called each time the user presses the process log button.
     *
     * This will clear the process log text area and display the server log details.
     */
    private void onProcessLog() {
        processTextArea.setText(""); // Clears the process log text area.
        barCodeState = FindBarCodeState.LinkedList; // Sets the find bar code state to the doubly linked list.

        processTextArea.setText(doublyLinkedList.display()); // Displays the doubly linked list.
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

            dataManager.saveFile(fileData.dataCollection); // Saves changes to the .txt file.

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

        dataManager.saveFile(fileData.dataCollection); // Saves changes to the .txt file.
        System.out.println("Saved Data File");

        tableModel.updateTable(fileData.toObjectArray()); // Refreshes the table to display the new entry.
    }

    /**
     *  The 'mostly sort' button on-click method.
     *  This method gets called each time the user presses the 'mostly sort' button.
     *
     *  This will send a message to the Archive CD window, triggering the corresponding sorting algorithm.
     */
    private void onMostlySort() {
        // Checks if the text field does not contain any text.
        if (isNullOrEmpty(sortTextField.getText()))
        {
            JOptionPane.showMessageDialog(this, "Please enter a section."); // Outputs warning message.
            return;
        }

        send(" ;Mostly Sort;" + sortTextField.getText().charAt(0)); // Sends the message to the Automation Console.
    }

    /**
     *  The random sort button on-click method.
     *  This method gets called each time the user presses the random sort button.
     *
     *  This will send a message to the Archive CD window, triggering the corresponding sorting algorithm.
     */
    private void onRandomSort() {
        // Checks if the text field does not contain any text.
        if (isNullOrEmpty(sortTextField.getText()))
        {
            JOptionPane.showMessageDialog(this, "Please enter a section."); // Outputs warning message.
            return;
        }

        send(";Random Sort;" + sortTextField.getText().charAt(0)); // Sends the message to the Automation Console.
    }

    /**
     *  The reverse sort button on-click method.
     *  This method gets called each time the user presses the  reverse sort button.
     *
     *  This will send a message to the Archive CD window, triggering the corresponding sorting algorithm.
     */
    private void onReverseSort() {
        // Checks if the text field does not contain any text.
        if (isNullOrEmpty(sortTextField.getText()))
        {
            JOptionPane.showMessageDialog(this, "Please enter a section."); // Outputs warning message.
            return;
        }

        send(";Reverse Sort;" + sortTextField.getText().charAt(0)); // Sends the message to the Automation Console.
    }

    /**
     * The title sort button on-click method.
     * The method that gets called each time the user presses the title sort button.
     *
     * This will sort the collection by title and output the result onto the table UI.
     */
    private void onSortTitle() {
        tableModel.updateTable(tableModel.shellSortByTitle(fileData.dataCollection)); // Refreshes the table to display the sorted data.

        binaryTree = new BinaryTree(); // Initializes the binary tree.

        // Loops through each item in the collection.
        for (int i = 0; i < fileData.count; i++)
        {
//            String nodeValue = fileData.dataCollection[i].getBarcode() + " " + fileData.dataCollection[i].getTitle();
            StringBuilder nodeValue = new StringBuilder();
            nodeValue.append(fileData.dataCollection[i].getBarcode()).append(" ").append(fileData.dataCollection[i].getTitle()); // Adds the barcode and title together using the string builder.
            binaryTree.addNode(i, nodeValue.toString()); // Adds the node to the binary tree.
        }
    }

    /**
     * The author sort button on-click method.
     * The method that gets called each time the user presses the author sort button.
     *
     * This will sort the collection by author and output the result onto the table UI.
     */
    private void onSortAuthor() {
        tableModel.updateTable(tableModel.shellSortByAuthor(fileData.dataCollection)); // Refreshes the table to display the sorted data.
    }

    /**
     * The barcode sort button on-click method.
     * The method that gets called each time the user presses the barcode sort button.
     *
     * This will sort the collection by barcode and output the result onto the table UI.
     */
    private void onSortBarcode() {
        tableModel.updateTable(tableModel.shellSortByBarcode(fileData.dataCollection)); // Refreshes the table to display the sorted data.
    }

    /**
     * The binary tree 'in-order' button on-click method.
     * The method that gets called each time the user presses the 'in-order' button.
     *
     * This will output the values of the binary tree using an in-order traversal.
     */
    private void onInOrder() {
        processTextArea.setText(""); // Clears the process log text area.
        binaryTree.inOrderTraverseTree(binaryTree.root, processTextArea); // Outputs binary tree node values using the in-order traversal.

        barCodeState = FindBarCodeState.BinaryTree; // Sets the find bar code state to the binary tree.
    }

    /**
     * The binary tree 'pre-order' button on-click method.
     * The method that gets called each time the user presses the 'pre-order' button.
     *
     * This will output the values of the binary tree using a pre-order traversal.
     */
    private void onPreOrder() {
        processTextArea.setText(""); // Clears the process log text area.
        binaryTree.preorderTraverseTree(binaryTree.root, processTextArea); // Outputs binary tree node values using the pre-order traversal.

        barCodeState = FindBarCodeState.BinaryTree; // Sets the find bar code state to the binary tree.
    }

    /**
     * The binary tree 'post-order' button on-click method.
     * The method that gets called each time the user presses the 'post-order' button.
     *
     * This will output the values of the binary tree using a post-order traversal.
     */
    private void onPostOrder() {
        processTextArea.setText(""); // Clears the process log text area.
        binaryTree.postOrderTraverseTree(binaryTree.root, processTextArea); // Outputs binary tree node values using the post-order traversal.

        barCodeState = FindBarCodeState.BinaryTree; // Sets the find bar code state to the binary tree.
    }

    /**
     * The hashmap save button on-click method.
     * The method that gets called each time the user presses the save button.
     *
     * This will generate the hashmap data.
     */
    private void onHashSave() {
        binaryTree.genHashMap(binaryTree.root); // Generates the hashmap.
        dataManager.saveHashMap("HashmapData.txt", binaryTree.hashMap); // Saves the hashmap to file.
    }

    /**
     * The hashmap display button on-click method.
     * The method that gets called each time the user presses the display button.
     *
     * This will display the hashmap data onto the screen.
     */
    private void onHashDisplay() {
        binaryTree.genHashMap(binaryTree.root); // Generates the hashmap.
        processTextArea.setText(binaryTree.hashDisplay()); // Displays the hashmap on the process log text area.
    }

    /**
     * The return button on-click method.
     * The method that gets called each time the user presses the return button.
     *
     * This will send a message to the Automation Console requesting the return of an item.
     */
    private void onReturn() {
        // Checks if any of the text fields are blank/no CD is selected.
        if (isNullOrEmpty(barcodeTextField.getText()) || isNullOrEmpty(titleTextField.getText()) || isNullOrEmpty(sectionTextField.getText()))
        {
            JOptionPane.showMessageDialog(this, "No CD is selected."); // Outputs warning message.
            return;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy - h:mma"); // Sets the format for the date text.

        StringBuilder output = new StringBuilder();
        output.append(LocalDateTime.now().format(dtf)).append(" - SENT - Return Item - ").append(barcodeTextField.getText()).append(" ").append(titleTextField.getText()); // Strings together the log output.

        doublyLinkedList.insertAtEnd(output + "\n"); // Adds the output to the doubly linked list.
        processTextArea.setText(doublyLinkedList.display()); // Refreshes the process log.

        output.delete(0, output.length()); // Clears the string builder.
        output.append(";").append(barcodeTextField.getText()).append(";").append(sectionTextField.getText()).append(";Return;").append(titleTextField.getText()).append(";").append(tableModel.getTable().getSelectedRow()); // Strings together the message.

        send(output.toString()); // Sends the message to the Automation Console.
    }

    /**
     * The retrieve button on-click method.
     * The method that gets called each time the user presses the retrieve button.
     *
     * This will send a message to the Automation Console requesting retrieval of an item.
     */
    private void onRetrieve() {
        // Checks if any of the text fields are blank/no CD is selected.
        if (isNullOrEmpty(barcodeTextField.getText()) || isNullOrEmpty(titleTextField.getText()) || isNullOrEmpty(sectionTextField.getText()))
        {
            JOptionPane.showMessageDialog(this, "No CD is selected.");
            return;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy - h:mma");

//        // Before optimization.
//        String output = LocalDateTime.now().format(dtf) + " - SENT - Retrieve Item - " + barcodeTextField.getText() + " " + titleTextField.getText();
//
//        doublyLinkedList.insertAtEnd(output + "\n");
//        processTextArea.setText(doublyLinkedList.display());
//
//        String message = ";" + barcodeTextField.getText() + ";" + sectionTextField.getText() + ";Retrieve;" + titleTextField.getText() + ";" + tableModel.getTable().getSelectedRow();
//        send(message);

        StringBuilder output = new StringBuilder();
        output.append(LocalDateTime.now().format(dtf)).append(" - SENT - Retrieve Item - ").append(barcodeTextField.getText()).append(" ").append(titleTextField.getText()); // Strings together the log output.

        doublyLinkedList.insertAtEnd(output + "\n"); // Adds the output to the doubly linked list.
        processTextArea.setText(doublyLinkedList.display()); // Refreshes the process log.

        output.delete(0, output.length()); // Clears the string builder.
        output.append(";").append(barcodeTextField.getText()).append(";").append(sectionTextField.getText()).append(";Retrieve;").append(titleTextField.getText()).append(";").append(tableModel.getTable().getSelectedRow()); // Strings together the message.

        send(output.toString()); // Sends the message to the Automation Console.
    }

    /**
     * The 'add to collection' button on-click method.
     * The method that gets called each time the user presses the return button.
     *
     * This will send a message to the Automation Console requesting for an item to be added to the collection.
     */
    private void onCollection() {
        // Checks if any of the text fields are blank/no CD is selected.
        if (isNullOrEmpty(barcodeTextField.getText()) || isNullOrEmpty(titleTextField.getText()) || isNullOrEmpty(sectionTextField.getText()))
        {
            JOptionPane.showMessageDialog(this, "No CD is selected.");
            return;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy - h:mma");

        StringBuilder output = new StringBuilder();
        output.append(LocalDateTime.now().format(dtf)).append(" - SENT - Add Item - ").append(barcodeTextField.getText()).append(" ").append(titleTextField.getText()); // Strings together the log output.

        doublyLinkedList.insertAtEnd(output + "\n"); // Adds the output to the doubly linked list.
        processTextArea.setText(doublyLinkedList.display()); // Refreshes the process log.

        output.delete(0, output.length()); // Clears the string builder.
        output.append(";").append(barcodeTextField.getText()).append(";").append(sectionTextField.getText()).append(";Add;").append(titleTextField.getText()).append(";").append(tableModel.getTable().getSelectedRow()); // Strings together the message.

        send(output.toString()); // Sends the message to the Automation Console.
    }

    /**
     * The remove button on-click method.
     * The method that gets called each time the user presses the remove button.
     *
     * This will send a message to the Automation Console requesting for the removal of an item.
     */
    private void onRemove() {
        // Checks if any of the text fields are blank/no CD is selected.
        if (isNullOrEmpty(barcodeTextField.getText()) || isNullOrEmpty(titleTextField.getText()) || isNullOrEmpty(sectionTextField.getText()))
        {
            JOptionPane.showMessageDialog(this, "No CD is selected.");
            return;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy - h:mma");

        StringBuilder output = new StringBuilder();
        output.append(LocalDateTime.now().format(dtf)).append(" - SENT - Remove Item - ").append(barcodeTextField.getText()).append(" ").append(titleTextField.getText()); // Strings together the log output.

        doublyLinkedList.insertAtEnd(output + "\n"); // Adds the output to the doubly linked list.
        processTextArea.setText(doublyLinkedList.display()); // Refreshes the process log.

        output.delete(0, output.length()); // Clears the string builder.
        output.append(";").append(barcodeTextField.getText()).append(";").append(sectionTextField.getText()).append(";Remove;").append(titleTextField.getText()).append(";").append(tableModel.getTable().getSelectedRow()); // Strings together the message.

        send(output.toString()); // Sends the message to the Automation Console.
    }

    /**
     * The find by bar code method.
     * The method that gets called each time the user presses enters text into the 'find by bar code' text field.
     *
     * This will perform the corresponding searching process depending on whether a binary tree or doubly linked list is displayed on the process log.
     *
     * @param value the search term.
     */
    private void findBarCode(String value) {
        // Checks which search function will be used.
        switch (barCodeState)
        {
            case None:
                return;
            case BinaryTree:
                binaryTree.findNode(binaryTree.root, value);
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
        dataManager = new DataManager("CD_FileData.txt"); // Creates a new data manager and loads the data file name.
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
        processTextArea.setEditable(false); // Ensures that the user cannot add anything to the process log directly.
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
            client = new MainWindowThread(this, socket); // Initializes server client.
        }
        catch (IOException ioe)
        {
            serverStatusText.setText("Error opening output stream:" + ioe); // Updates server connection status label with error message.
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
     *
     * @param msg the message that will be sent.
     */
    private void send(String msg) {
        try
        {
            streamOut.writeUTF(msg); // Writes the message to the output stream.
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

            doublyLinkedList.insertAtEnd(temp[0] + "\n"); // Adds the retrieved message to the doubly linked list.
            processTextArea.setText(doublyLinkedList.display()); // Refreshes the process log text area.

            tableModel.getTable().setRowSelectionInterval(Integer.parseInt(temp[1]), Integer.parseInt(temp[1])); // Sets the selected row on the table.

            // Checks which action was retrieved.
            if (msg.contains("Retrieve"))
            {
                tableModel.getTable().setValueAt(true, Integer.parseInt(temp[1]), 7); // Sets the CD on loan.
            }
            else if (msg.contains("Return"))
            {
                tableModel.getTable().setValueAt(false, Integer.parseInt(temp[1]), 7); // Sets the CD off loan.
            }
            else if (msg.contains("Remove"))
            {
                tableModel.getTable().remove(Integer.parseInt(temp[1])); // Removes the item from the table.
            }

            serverStatusText.setText("received."); // Updates the server connection progress label.
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
        if (StringUtils.isEmpty(text)) return true;
        return false;
    }
}
