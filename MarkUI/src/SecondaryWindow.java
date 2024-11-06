import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SecondaryWindow extends JDialog {
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
    private JLabel serverStatusText;
    private ArchiveCDTable tableModel;

    public SecondaryWindow() {
        setContentPane(contentPane);
        setModal(false);
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

    private void onProcess() {
        send();
    }

    private void onTableClick() {
        textField2.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 2).toString()); // Sets the 'section' text field to the table column.
        textField1.setText(tableModel.getTable().getModel().getValueAt(tableModel.getTable().getSelectedRow(), 5).toString()); // Sets the 'barcode' text field to the table column.
    }

    private void createUIComponents() {
        DataManager dataManager = new DataManager("CD_ArchivePrototype_SampleData.txt");
        SaveData fileData = dataManager.loadFile();

//        JOptionPane.showMessageDialog(this, "Failed to retrieve Archive Console data. Loading data from '" + dataManager.getFileName() + "'.");

        tableModel = new ArchiveCDTable(fileData.toObjectArray(), 7);

        tableScrollPane = new JScrollPane(tableModel.getTable());

        String[] dropDownItems = {"Add", "Remove", "Retrieve", "Return", "Sort"};
        DefaultComboBoxModel dModel = new DefaultComboBoxModel(dropDownItems);

        actionsDropdown = new JComboBox(dModel);
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
            streamOut.writeUTF("Hello");
            streamOut.flush();
            serverStatusText.setText("sent.");
//            processTextArea.setText(""); // Reset vlaues or something
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
            // NEW -----------------------------------
//            currentAssocWord++;
//            wordList[currentAssocWord] = new AssocData(msg);
//            for (int i = 0; i < currentAssocWord; i++)
//            {
//                System.out.println("Handle Method: " + i + " - " + wordList[i].words);
//            }

            serverStatusText.setText("received.");
            textField1.setText(msg);
            //----------------------------------------
        }
    }
}
