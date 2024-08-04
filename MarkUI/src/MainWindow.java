import javax.swing.*;
import java.awt.event.*;

public class MainWindow extends javax.swing.JDialog {
    private JPanel contentPane;
    private JButton buttonCancel, newItemButton, updateButton, retrieveButton, returnButton, collectionButton, removeButton,
            sortRandomButton, mostlySortButton, reverseSortButton, processButton, preOrderButton, inOrderButton, hashSaveButton, hashDisplayButton, postOrderButton, graphicalButton, sortTitleButton, sortAuthorButton, sortBarcodeButton, searchButton;
    private JTextField titleTextField, authorTextField, xTextField, yTextField, barcodeTextField, sortTextField, findBarcodeTextField, searchTextField, sectionTextField;
    private JTextArea descriptionTextArea, processTextArea;
    private JScrollPane tableScrollPane;
    private DataManager dataManager;
    private ArchiveCDTable tableModel;
    public SaveData fileData;

    public MainWindow() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Archive Console");

        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onCancel();
            }
        });

        graphicalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onPopup();
            }
        });
        newItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onNewItem();
            }
        });

        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onUpdate();
            }
        });

        sortTitleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onSortTitle();
            }
        });

        sortAuthorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onSortAuthor();
            }
        });

        sortBarcodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onSortBarcode();
            }
        });

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
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onPopup() {
        SecondaryWindow dialog = new SecondaryWindow(this);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void onTableClick() {
        titleTextField.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 0).toString());
        authorTextField.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 1).toString());
        sectionTextField.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 2).toString());

        xTextField.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 3).toString());
        yTextField.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 4).toString());
        barcodeTextField.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 5).toString());
        descriptionTextArea.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 6).toString());
    }

    private void onNewItem() {
        tableModel.getTable().setSelectionMode(0); // Deselects the Jtable selection.

        titleTextField.setText(null);
        authorTextField.setText(null);
        sectionTextField.setText(null);

        xTextField.setText(null);
        yTextField.setText(null);
        barcodeTextField.setText(null);
        descriptionTextArea.setText(null);
    }

    private void onUpdate() {
        // TODO: Add constraints for fields.


        // Checks if an item in the table is selected.
        if (!tableModel.getTable().getSelectionModel().isSelectionEmpty())
        {
            tableModel.getTable().getModel().setValueAt(titleTextField.getText(), tableModel.getTable().getSelectedRow(), 0);
            tableModel.getTable().getModel().setValueAt(authorTextField.getText(), tableModel.getTable().getSelectedRow(), 1);
            tableModel.getTable().getModel().setValueAt(sectionTextField.getText(), tableModel.getTable().getSelectedRow(), 2);
            tableModel.getTable().getModel().setValueAt(xTextField.getText(), tableModel.getTable().getSelectedRow(), 3);
            tableModel.getTable().getModel().setValueAt(yTextField.getText(), tableModel.getTable().getSelectedRow(), 4);
            tableModel.getTable().getModel().setValueAt(barcodeTextField.getText(), tableModel.getTable().getSelectedRow(), 5);
            tableModel.getTable().getModel().setValueAt(descriptionTextArea.getText(), tableModel.getTable().getSelectedRow(), 6);

            return;
        }

        fileData.count++;

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

        tableModel.updateTable(fileData.toObjectArray());
    }

    private void onSortTitle() {
        tableModel.updateTable(tableModel.sortByTitle(fileData.dataCollection));
    }

    private void onSortAuthor() {
        tableModel.updateTable(tableModel.sortByAuthor(fileData.dataCollection));
    }

    private void onSortBarcode() {
        tableModel.updateTable(tableModel.sortByBarcode(fileData.dataCollection));
    }

    public static void main(String[] args) {
        MainWindow dialog = new MainWindow();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        dataManager = new DataManager("CD_ArchivePrototype_SampleData.txt");
        fileData = dataManager.loadFile();

        // Checks if the loaded data is not valid.
        if (fileData.dataCollection == null)
        {
            JOptionPane.showMessageDialog(this, "Error loading file with name: '" + dataManager.getFileName() + "'. Loading sample data.");

            Object[][] sampleData = {
                    {"Kathy", "Smith",
                            "Snowboarding", 'C', 5, 5, 5, false},
                    {"Kathy", "Smith",
                            "Snowboarding", 'B', 5, 5, 5, true},
                    {"Kathy", "Smith",
                            "Snowboarding", 'L', 5, 5, 5, false}
            };

            tableModel = new ArchiveCDTable(sampleData, -1);
        }
        else
        {
            tableModel = new ArchiveCDTable(fileData.toObjectArray(), -1);
        }

        tableScrollPane = new JScrollPane(tableModel.getTable());
    }
}
