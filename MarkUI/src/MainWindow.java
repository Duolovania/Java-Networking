import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.table.*;
import javax.xml.crypto.Data;
import java.awt.event.*;

public class MainWindow extends javax.swing.JDialog {
    private JPanel contentPane;
    private JButton buttonCancel, button1, button2, button3, button4, button5, button6,
            button7, button8, button9, button10, button11, button12, button13, button14, button15, button16, button17, button18, button19, button20;
    private JTextField textField1, textField2, textField3, textField4, textField5, textField6, textField7, textField8, textField9;
    private JTextArea textArea1, textArea2;
    private JScrollPane tableScrollPane;
    private DataManager dataManager;
    ArchiveCDTable tableModel;
    SaveData fileData;
    Object[][] data;

    public MainWindow() {
        setContentPane(contentPane);
        setModal(true);

        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onCancel();
            }
        });

        button16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onPopup();
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
        SecondaryWindow dialog = new SecondaryWindow();
        dialog.pack();
        dialog.setVisible(true);
    }

    private void onTableClick() {
        textField1.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 0).toString());
        textField2.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 1).toString());
        textField9.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 2).toString());

        textField3.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 3).toString());
        textField4.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 4).toString());
        textField5.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 5).toString());
        textArea1.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 6).toString());
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

            tableModel = new ArchiveCDTable(sampleData);
        }
        else
        {
            tableModel = new ArchiveCDTable(fileData.toObjectArray());
        }

        tableScrollPane = new JScrollPane(tableModel.getTable());
    }
}
