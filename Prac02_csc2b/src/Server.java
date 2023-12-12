import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private int numRequests;

    /**
     * Constructs a Server object listens on the specified port for incoming connections.
     *
     * @param port The port number on which the server listens for incoming connections.
     * @throws IOException if an I/O error occurs when opening the server socket.
     */
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        // Notifying the user that the Port is ready for connect
        System.out.println("Ready for incoming connections...");
        numRequests = 0;
        threadPool = Executors.newCachedThreadPool(); // Using a thread pool for managing client connections
    }

    /**
     * Starts the language detection server and accepts client connections.
     */
    public void start() {
        try {
            while (true) {
                Socket socket = serverSocket.accept(); // Accept incoming client connection
                threadPool.execute(new ClientHandler(socket)); // Create a new thread to handle the client
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ClientHandler class to process requests for each connected client in a separate thread.
     */
    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter writer;
        private BufferedReader reader;

        public ClientHandler(Socket socket) {
            clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Printing output to client
                writer.println("HELLO - you may make 4 requests and I’ll try to detect your language");

                // Wait for the "START" message from the client
                String startMessage = reader.readLine();

                if (startMessage != null && startMessage.equalsIgnoreCase("START")) {
                    // Sending the "REQUEST or DONE" message to the client
                    writer.println("REQUEST or DONE");

                    // Loop through processing requests
                    while (numRequests < 4) {
                        // Reading User input
                        String response = reader.readLine();

                        if (response != null && response.trim().toLowerCase().startsWith("request ")) {
                           numRequests++;

                            // Reading the User's input
                            response = response.substring(8); // Removing the "REQUEST " part

                            // Checking the type of question
                            StringTokenizer token = new StringTokenizer(response, " ");
                            String firstWord = token.nextToken();
                            Random random = new Random();

                            // Checking if the response contains "ngiyabonga" or "mina"
                            if (response.toLowerCase().contains("ngiyabonga") || response.toLowerCase().contains("mina")) {
                                writer.println("0" + numRequests + " I detect some Zulu here.");
                            }
                         // if first word is "Is"...
                            else if (firstWord.toUpperCase().startsWith("IS")) {
                                // Generate a random number between 0 and 2
                                int randomNum = random.nextInt(3);

                                // Respond with one of the three possible language detection results based on the random number
                                if (randomNum == 0) {
                                    writer.println("0" + numRequests + " Anglais?");
                                } else if (randomNum == 1) {
                                    writer.println("0" + numRequests + " English?");
                                } else if (randomNum == 2) {
                                    writer.println("0" + numRequests + " Maybe Afrikaans?");
                                }
                            }

                            // Checking if the response contains "Dumela"
                            else if (response.toLowerCase().contains("dumela")) {
                                writer.println("0" + numRequests + " I greet you in Sotho!");
                            }
                            // If the word does not match any of the conditions
                            else {
                                int numRandom = random.nextInt(3);
                                if (numRandom == 0) {
                                    writer.println("0" + numRequests + " Howzit");
                                } else if (numRandom == 1) {
                                    writer.println("0" + numRequests + " I’m still learning");
                                } else if (numRandom == 2) {
                                    writer.println("0" + numRequests + " No idea");
                                }
                            }
                            //Goodbye message after user says 'DONE'
                        } else if (response != null && response.equalsIgnoreCase("DONE")) {
                            writer.println("0" + numRequests + " GOOD BYE - [" + numRequests + "] requests answered");
                            break;
                        } else {
                            // Handling the case where the user misspells "REQUEST" or sends an invalid command
                            writer.println("Invalid command. Please use the format 'REQUEST [message]' or 'DONE'.");
                        }
                        //Goodbye message after 4 requests have been made
                        if (numRequests >= 4) {
                            writer.println("05 GOOD BYE - [4] queries answered");
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Closing the socket and streams in the finally block
                try {
                    if (writer != null) {
                        writer.close();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                    if (clientSocket != null) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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
