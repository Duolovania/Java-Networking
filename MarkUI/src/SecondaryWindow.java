import java.awt.event.ActionListener;

public class SecondaryWindow extends javax.swing.JDialog {
    private javax.swing.JPanel contentPane;
    private javax.swing.JButton buttonCancel, button1, processButton, button2;
    private javax.swing.JTextField textField1, textField2, textField3, textField4, textField5, textField6, textField7, textField8;
    private javax.swing.JTextArea textArea1;
    private  javax.swing.JTable table1;
    private javax.swing.JComboBox comboBox1;

    public SecondaryWindow() {
        setContentPane(contentPane);
        setModal(true);

        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onCancel();
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

    public static void main(String[] args) {
        SecondaryWindow dialog = new SecondaryWindow();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
