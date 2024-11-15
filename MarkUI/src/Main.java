//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {
    public static void main(String[] args) {
        ChatServer server;

        // Starts the server.
        if (args.length != 1)
        {
            server = new ChatServer(4444);
        }
        else
        {
            server = new ChatServer(Integer.parseInt(args[0]));
        }

        // Runs both windows at the same time.
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow(); // Creates a new window.
            mainWindow.pack(); // Sets the window size to the preferred size and layouts.
            mainWindow.setLocation(100, 100); // Position on screen
            mainWindow.setVisible(true); // Enables window visibility.

            SecondaryWindow secondaryWindow = new SecondaryWindow(); // Creates a new window.
            secondaryWindow.pack(); // Sets the window size to the preferred size and layouts.
            secondaryWindow.setLocation(900, 100); // Position on screen
            secondaryWindow.setVisible(true); // Enables window visibility.
        });
    }
}