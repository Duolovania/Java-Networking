//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Main implements IApp {
    public static void main(String[] args) {
        Server server;

        // Starts the server.
        if (args.length != 1)
        {
            server = new Server(4444);
        }
        else
        {
            server = new Server(Integer.parseInt(args[0]));
        }

        IApp app = new Main();
        ShutdownInterceptor shutdownInterceptor = new ShutdownInterceptor(app);
        Runtime.getRuntime().addShutdownHook(shutdownInterceptor);
        app.start();


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

    public void start() {

        try {
            System.out.println("Starting graceful shutdown.");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void shutDown() {
        // Do a graceful shutdown here
        System.out.println("Shutdown is called");

        try {
            System.out.println("Sleeping for 1 second before shutting down");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}