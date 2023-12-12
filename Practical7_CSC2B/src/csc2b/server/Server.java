package csc2b.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * @author Baloyi MP
 *
 */
public class Server {
	private ServerSocket server; //server Socket
	private boolean isReady;
	
	/**
	 * Server method
	 * @param port
	 */
	public Server(int port) {
		try {
			server = new ServerSocket(port);
			System.out.println("Server created on port: " + port);
			isReady = true;
			
			while(isReady)
			{
				System.out.println("Waiting to accept clients...");
				Socket clientConn = server.accept();
				Thread clientThread = new Thread(new ZEDEMHandler(clientConn));
				clientThread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    /**
     * Setup server socket and pass on handling of request
     * @param argv
     */
    public static void main(String[] argv)
    {
    	
    	Server server = new Server(2021);    	
    }
}