package acsse.csc2b.server;
/**
 * @author Baloyi MP 221048179
 * @version Practical 04
 */
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;


public class ClientHandler implements Runnable
{
	private Socket clientConnection = null;
	private BufferedReader reader =null; 
	private PrintWriter writer = null;

	/**
	 * Constructs a ClientHandler object with the provided socket connection.
	 *
	 * @param newSocketConnection The socket connection to the client.
	 */
	public ClientHandler(Socket newSocketConnection)
	{
		this.clientConnection = newSocketConnection;  
	}

	@Override
	public void run() 
	{
		
		String requestLine = ""; 
		try 
		{
			reader = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
			requestLine = reader.readLine(); 
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
		} 
		//// Handle pull command
		if(requestLine.equals("PULL"))
		{
			//return a list of image files and IDs
			System.out.println("Command PULL");
			
			try(Scanner fileScanner = new Scanner(new File("data/server/ImgList.txt"))) //using automatic resource management
			{
				StringBuffer list = new StringBuffer();
				while(fileScanner.hasNext())
				{
					list.append(fileScanner.nextLine() +"#?"); 
				}
				responsed(new String(list)); 
				System.out.println("Image List Pulled");
				
			}
			catch (FileNotFoundException e) 
			{
				System.err.println(e.getMessage()); 
				System.err.println("File Not Found For ImgList"); 
				
			} 
		}
		else if (requestLine.startsWith("DOWNLOAD")) {
		    // DOWNLOAD <ID>
		    StringTokenizer tokenizer = new StringTokenizer(requestLine, "#?");
		    System.out.println("Image Requested: Command " + tokenizer.nextToken());
		    String ID = tokenizer.nextToken();

		    try (Scanner fileScanner = new Scanner(new File("data/server/ImgList.txt"))) {
		        while (fileScanner.hasNext()) {
		            String line = fileScanner.nextLine();
		            StringTokenizer lineTokenizer = new StringTokenizer(line);
		            String imageID = lineTokenizer.nextToken();
		            String fileName = lineTokenizer.nextToken();

		            if (imageID.equals(ID)) {
		                File fileToSend = new File("data/server/" + fileName);
		                if (fileToSend.exists()) {
		                    sendFile(fileToSend);
		                    System.out.println(imageID + ": File sent");
		                } else {
		                    responsed("NOTFOUND"); // Respond with "NOTFOUND" if the file doesn't exist
		                    System.err.println("File not found");
		                }
		                break; // Break out of the loop after sending or responding
		            }
		        }
		    } catch (FileNotFoundException e) {
		        System.err.println(e.getMessage());
		        System.err.println("File Not Found For ImgList");
		    }
		

			
		}else if (requestLine.substring(0, 7).equals("UPLOAD")) {
		   
		    StringTokenizer tokenizer = new StringTokenizer(requestLine, "#?");
		    String command = tokenizer.nextToken();
		    String ID = tokenizer.nextToken();
		    String name = tokenizer.nextToken();
		    String size = tokenizer.nextToken();
		    
		    
		    receiveFile(ID, name, size);
		    
		    
		    File uploadedFile = new File("data/client/" + name);
		    if (uploadedFile.exists() && uploadedFile.length() == Long.parseLong(size)) {
		        
		        System.out.println("HAPPY"); // Print "HAPPY"
		        responsed("HAPPY"); // Reply with success
		    } else {
		       
		        System.out.println("SAD"); // Print "SAD"
		        responsed("SAD"); // Reply with failure
		    }
		}

		}

	//Sends a response to the client.
	private void responsed(String response)
	{
		try 
		{
			writer = new PrintWriter(clientConnection.getOutputStream(), true);
			writer.println(response);
			writer.flush();
		} 
		catch (IOException e) 
		{
			System.err.println(e.getMessage()); 
			System.err.println("IO Exception");
		} 
		
	}
	
	 /**
     * Receives a file from the client and saves it on the server.
     *
     * @param ID   The ID of the file.
     * @param name The name of the file.
     * @param size The size of the file.
     */
	private void receiveFile(String ID, String name, String size)
	{
		try 
		{
			File fileToReceive = new File("data\server\\" + name); 
			FileOutputStream fos = new FileOutputStream(fileToReceive);
			DataInputStream dataIn = new DataInputStream(clientConnection.getInputStream()); 
			
			int fileSize = Integer.parseInt(size); 
			
			byte [] buffer = new byte[1024]; 
			int n = 0; 
			int totalBytes = 0; 
			while(totalBytes!=fileSize)
			{
				n = dataIn.read(buffer, 0, buffer.length); 
				fos.write(buffer,0, n);
				fos.flush();
				totalBytes+= n; 
			}
			dataIn.close();
			fos.close();
			
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println(e.getMessage()); 
			System.err.println("File Not Found"); 
		} catch (IOException e) 
		{
			System.err.println(e.getMessage()); 
			System.err.println("IO Exception"); 
		} 
			
	}
	
	/**
     * Sends a file to the client.
     *
     * @param fileToSend The file to send.
     */
	private void sendFile(File fileToSend)
	{
		try
		{
			
			FileInputStream fis = new FileInputStream(fileToSend); 
			DataOutputStream dataOut = new DataOutputStream(clientConnection.getOutputStream()); 
			
			
			responsed(String.valueOf(fileToSend.length()));
			
			
			byte [] buffer = new byte [2048]; 
			int n = 0; 
			while( (n = fis.read(buffer)) > 0 )
			{
				dataOut.write(buffer);
				dataOut.flush();
			}
			fis.close();
			dataOut.close();
	
			System.out.println("The file requested has been sent to the client\n"); 
			
		}
		catch(IOException e)
		{
			System.err.println(e.getMessage());
			System.err.println("File Not Found"); 
		}
	}

}
