package csc2b.server;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ZEDEMHandler implements Runnable {
	/**
	 * @author Baloyi MP
	 *
	 */
private Socket connectionToClient;
	
	//Text streams
	private PrintWriter txtout=null;
	private BufferedReader txtin=null;
	
	//Binary streams
	private DataOutputStream dos=null;
	private DataInputStream dis=null;
	
	//Byte streams
	private OutputStream os=null;
	private InputStream is=null;
	
	private boolean processing;
	private boolean authenticated;

	/**
	 * Establishing connection
	 * @param connection
	 */
	public ZEDEMHandler(Socket connection) {
		
    	this.connectionToClient = connection;
    	try {
    		os = connectionToClient.getOutputStream();
    		is =connectionToClient.getInputStream();
    		
    		dos = new DataOutputStream(os);
    		dis = new DataInputStream(is);
    		
    		txtout = new PrintWriter(os);
    		txtin = new BufferedReader(new InputStreamReader(is));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//handle command cases
	@Override
	public void run() {
		System.out.println("Start processing commands");
    	processing =true;
    	
    	try {
    		while(processing) {
    		
    		String requestln = txtin.readLine();
    		System.out.println("Got requestln: " + requestln);
    		StringTokenizer st = new StringTokenizer(requestln);
    		String command = st.nextToken();
			if(command.equals("AUTH"))
			{
				String username = st.nextToken();
				String pass = st.nextToken();
				
				if(matchUser(username, pass))
				{
					sendMessage("Ja, Successfully logged in");
					authenticated = true;
					System.out.println("Logged in");
				}
				else
				{
					sendMessage("Nee, Unsucessful login");
					authenticated = false;	
				}	
			}
			else if(command.equals("LIST"))
			{
				String fileList = "";
				System.out.println("LIST command received");
				if(authenticated == true)
				{
					ArrayList<String> fileArrList = getFileList();
					String strToSend ="";
					for(String s: fileArrList)
					{
						strToSend += s +"#";
					}
					sendMessage(strToSend);
					System.out.println("StrToSend:" + strToSend);
				}
				else
				{
					sendMessage("Nee Not authenticated");
				}
			}
			else if(command.equals("ZEDEMGET"))
			{
				if(authenticated)
				{
					String fileID = st.nextToken();
					String fileName = idToFileName(fileID);
					File audioFile = new File("data/server/"+fileName);
					if(audioFile.exists())
					{
						System.out.println("File found");
						sendMessage("Ja " + audioFile.length()+" bytes");
						//NB: When Sending a file
						FileInputStream fis = new FileInputStream(audioFile);
						byte[] buffer = new byte[1024];
						int n = 0;
						while((n = fis.read(buffer))>0)//read file into byte[]
						{
							dos.write(buffer,0,n); 
							dos.flush();
						}
						fis.close();
						
						System.out.println("File sent to client");
					}
				}
				else
				{
					sendMessage("Nee Not authenticated");
				}
			}
			else if(command.equals("LOGOUT"))
			{
				if(authenticated)
				{
					authenticated = false;
					sendMessage("Ja Logged out");
					dos.close();
					dis.close();
					
					txtin.close();
					txtout.close();
					connectionToClient.close();
					processing = false;	
				}
				else
				{
					sendMessage("Nee Not authenticated");
				}
			}
			else
			{
				//invalid command
			}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	finally
    	{
    		System.out.println("Finally");
    	}
	}
	
	/**
	 * sendMessage helper method
	 * @param message
	 */
	private void sendMessage(String message) {
		// TODO Auto-generated method stub
		txtout.println(message);
    	txtout.flush();
		
	}

	/**
	 * matchUser helper method
	 * @param userN
	 * @param passW
	 * @return
	 */
	private boolean matchUser(String userN, String passW)
	{
		boolean found = false;
		
		//Code to search users.txt file for match with userN and passW.
		File userFile = new File("data/server/users.txt");
		try
		{
		    Scanner scan = new Scanner(userFile);
		    while(scan.hasNextLine()&&!found)
		    {
			String line = scan.nextLine();
			String lineSec[] = line.split("\\s");
	    		
			if(userN.equals(lineSec[0])&& passW.equals(lineSec[1]))
			{
				found = true;
			}
	    }
	    scan.close();
		}
		catch(IOException ex)
		{
		    ex.printStackTrace();
		}
		
		return found;
	}
	
	/**
	 * method to get file list
	 * @return
	 */
	private ArrayList<String> getFileList()
	{
		ArrayList<String> result = new ArrayList<String>();
		
		//Code to add list text file contents to the arraylist.
		File lstFile = new File("data/server/List.txt");
		try
		{
			Scanner scan = new Scanner(lstFile);
			while(scan.hasNext())
			{
				result.add(scan.nextLine());
			}
						
			scan.close();
		}	    
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * String method to get file ID
	 * @param strID
	 * @return
	 */
	private String idToFileName(String strID)
	{
		String result ="";
		
		//Code to find the file name that matches strID
		File lstFile = new File("data/server/List.txt");
    	try
    	{
    		Scanner scan = new Scanner(lstFile);
    		String line = "";
    		while(scan.hasNext())
    		{
    			line = scan.nextLine();
    			StringTokenizer tokenizer = new StringTokenizer(line);
    			String strid = tokenizer.nextToken();
    			String fileName = tokenizer.nextToken();
    			if(strid.equals(strID))
    			{
    				result = fileName;
    			}
    		}
    		//Read filename from file
    		
    		scan.close();
    	}
    	catch(IOException ex)
    	{
    		ex.printStackTrace();
    	}
		return result;
	}
}
