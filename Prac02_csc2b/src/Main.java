import java.io.IOException;

/**
 * server application
 * @author Baloyi, MP
 * @version Practical_02
 *
 */
public class Main {
	
	/**
     * Main method to start the language detection server on the 8888 port.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        int port = 8888;
        try {
            Server server = new Server(port);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
