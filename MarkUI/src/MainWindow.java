import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.table.*;

public class MainWindow extends javax.swing.JDialog {
    private javax.swing.JPanel contentPane;
    private javax.swing.JButton buttonCancel, button1, button2, button3, button4, button5, button6,
            button7, button8, button9, button10, button11, button12, button13, button14, button15, button16, button17, button18, button19, button20;
    private javax.swing.JTextField textField1, textField2, textField3, textField4, textField5, textField6, textField7, textField8;
    private javax.swing.JTextArea textArea1, textArea2;
    private JScrollPane tableScrollPane;

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

    public static void main(String[] args) {
        MainWindow dialog = new MainWindow();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        Object[][] data = {
                {"Kathy", "Smith",
                        "Snowboarding", 'C', 5, 5, 5, false},
                {"Kathy", "Smith",
                        "Snowboarding", 'B', 5, 5, 5, true},
                {"Kathy", "Smith",
                        "Snowboarding", 'L', 5, 5, 5, false}
        };

        ArchiveCDTable tableModel = new ArchiveCDTable(data);
        tableScrollPane = new JScrollPane(tableModel.getTable());
    }
}
