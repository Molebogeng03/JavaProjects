package acsse.csc2b.server;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import acsse.csc2b.thread.Client;

/**
 * @author Baloyi MP
 * @version Practical_03
 */
public class Server {
	//variables
	ServerSocket serverSocket;
	Socket socket;
	DataOutputStream dataOutput;
	int port;
	boolean running;

	// initializes the Server instance
	public Server(int port) {
		// Initialising variables
		this.port = port;

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server is connected to port: " + serverSocket.getLocalPort());
			running = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Connects with clients and processes incoming requests using threads.
	 *
	 * @return void
	 */
	public void connection() {
		while (running) {
			try {

				socket = serverSocket.accept();

				dataOutput = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

				// Calling thread for connection
				Client t = new Client(socket);
				Thread thread = new Thread(t);
				thread.start();

			} catch (IOException e) {
				try {
					dataOutput.writeBytes("ERROR 500, an error occured");
				} catch (IOException ex) {
					ex.printStackTrace();
				}

			}

		}
	}

}