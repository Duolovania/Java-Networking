import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class SecondaryWindow extends JFrame {
    private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ChatClientThread2 client = null;
    private String serverName = "localhost";
    private int serverPort = 4444;

    private JPanel contentPane;
    private JButton buttonCancel, button1, processButton;
    private JTextField textField1, textField2, textField3, textField4, textField5, textField6, textField7, textField8;
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

        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onCancel();
            }
        });

        processButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onProcess();
            }
        });

        textField2.getDocument().addDocumentListener(new DocumentListener() {
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

        button1.addActionListener(new java.awt.event.ActionListener() {
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

        connect(serverName, serverPort);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void checkAddVisibility() {
        if (textField2.getText().equals(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 2).toString()))
        {
            button1.setEnabled(false);
            return;
        }

        button1.setEnabled(true);
    }

    private void onAddItem() {
//        for (a)
    }

    private void onProcess() {
        send();
    }

    private void onTableClick() {
        textField2.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 2).toString()); // Sets the 'section' text field to the table column.
        textField1.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 5).toString()); // Sets the 'barcode' text field to the table column.

        selectedCD = new ArchiveCD(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 1).toString(), tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 3).toString().charAt(0), Integer.parseInt(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 5).toString()));
    }

    private void createUIComponents() {
        DataManager dataManager = new DataManager("CD_ArchivePrototype_SampleData.txt");
        fileData = dataManager.loadFile();


        tableModel = new ArchiveCDTable(fileData.toObjectArray(), 7);

        tableScrollPane = new JScrollPane(tableModel.getTable());

        actionsDropdown = new JComboBox();

        actionsDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = actionsDropdown.getSelectedItem().toString();
                System.out.println(value);
            }
        });
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
            client = new ChatClientThread2(this, socket);
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

    private void send() {
        try
        {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy - h:mma");

            String output = LocalDateTime.now().format(dtf) + " - RCVD - " + actionsDropdown.getSelectedItem() + " - "  + selectedCD.getBarcode() + " " + selectedCD.getTitle() + ";" + tableModel.getTable().getSelectedRow();

            streamOut.writeUTF(output);
            streamOut.flush();
            serverStatusText.setText("sent.");
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

            if (temp[1].equals("Mostly Sort") || temp[1].equals("Random Sort") || temp[1].equals("Reverse Sort"))
            {
                sortText.setText(temp[1]);
                sortSection = temp[2].charAt(0);
                actionsDropdown.setSelectedItem("Sort");
                sortTable();
                return;
            }

            textField1.setText(temp[1]);
            textField2.setText(temp[2]);
            actionsDropdown.setSelectedItem(temp[3]);

            selectedCD = new ArchiveCD(temp[4], temp[2].charAt(0), Integer.parseInt(temp[1]));
            tableModel.getTable().setRowSelectionInterval(Integer.parseInt(temp[5]), Integer.parseInt(temp[5]));

            serverStatusText.setText("received.");
        }
    }

    private void sortTable() {
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

    private void mostlySort() {
        tableModel.updateTable(tableModel.insertionSortForSection(sortSection, fileData.dataCollection)); // Refreshes the table to display the sorted data.
    }

    private void reverseSort() {
        Object[][] tempArr = tableModel.shellSortByTitle(fileData.dataCollection);
        reverse2DArray(tempArr);
        tableModel.updateTable(tempArr);
    }

    public static void reverse2DArray(Object[][] array) {
        reverseArray(array);
    }

    private static void reverseArray(Object[] row) {
        int left = 0;
        int right = row.length - 1;
        while (left < right) {
            Object temp = row[left];
            row[left] = row[right];
            row[right] = temp;
            left++;
            right--;
        }
    }

    private void randomSort() {
        ArrayList<Vector<Object>> rows = new ArrayList<>();
        for (int i = 0; i < tableModel.getTable().getModel().getRowCount(); i++) {
            Vector<Object> row = (Vector<Object>) tableModel.model.getDataVector().get(i);
            rows.add(row);
        }

        // Shuffle the list of rows
        Collections.shuffle(rows);

        // Clear the model and add shuffled rows back
        tableModel.model.setRowCount(0);
        for (Vector<Object> row : rows)
        {
            tableModel.model.addRow(row.toArray());
        }
    }
}
