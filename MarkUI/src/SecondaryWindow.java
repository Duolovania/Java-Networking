import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SecondaryWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel, button1, processButton, button2;
    private JTextField textField1, textField2, textField3, textField4, textField5, textField6, textField7, textField8;
    private JTextArea textArea1;
    private JComboBox comboBox1;
    private JScrollPane tableScrollPane;
    private MainWindow mainWindow;
    private ArchiveCDTable tableModel;

    public SecondaryWindow(MainWindow main) {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Automation Console");

        mainWindow = main;

        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onCancel();
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
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onTableClick() {
        textField2.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 2).toString()); // Sets the 'section' text field to the table column.
        textField1.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 5).toString()); // Sets the 'barcode' text field to the table column.
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        // Checks if the loaded data is not valid.
        if (mainWindow == null)
        {
            DataManager dataManager = new DataManager("CD_ArchivePrototype_SampleData.txt");
            SaveData fileData = dataManager.loadFile();

            JOptionPane.showMessageDialog(this, "Failed to retrieve Archive Console data. Loading data from '" + dataManager.getFileName() + "'.");

            tableModel = new ArchiveCDTable(fileData.toObjectArray(), 7);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Success!");

            SaveData fileData = mainWindow.fileData;
            tableModel = new ArchiveCDTable(fileData.toObjectArray(), 7);
        }

        tableScrollPane = new JScrollPane(tableModel.getTable());
    }
}
