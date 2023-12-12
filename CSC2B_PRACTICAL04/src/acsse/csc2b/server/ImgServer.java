package acsse.csc2b.server;
/**
 * @author Baloyi MP 221048179
 * @version Practical 04
 */
import java.io.IOException;
import java.net.ServerSocket;

public class ImgServer
{
	private static final int PORT = 9876; 
	private static ServerSocket serverSocket = null; 
	private boolean isRunning = false; 
	
	 /**
     * Constructs a Server object and attempts to open a server socket on the specified port.
     */
	public ImgServer () 
	{
		try 
		{
			serverSocket = new ServerSocket(PORT); 
			System.out.println("Server running on port: " + PORT + " and waiting for connection"); 
			isRunning = true; 
		} 
		catch (IOException e) 
		{
			System.err.println("Could not open a Server Socket on port"  + e.getMessage()); 
		}  
	}
	
	/**
     * Starts the server by continuously accepting client connections and spawning a new thread to handle each client.
     */
	public void startServer()
	{
		while(isRunning ==true)
		{
			try 
			{
				Thread thr = new Thread(new ClientHandler(serverSocket.accept()));
				thr.start();
			} catch (IOException e) 
			{
				e.printStackTrace();
			} 
			
		}
	}
	
	public static void main (String [] args)
	{
		ImgServer server = new ImgServer(); 
		server.startServer();
	}
	
}