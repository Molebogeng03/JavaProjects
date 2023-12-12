package acsse.csc2b.client;
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
import java.util.Random;
import java.util.StringTokenizer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * The ClientPane class represents the graphical user interface and functionality of the client application.
 */
public class ClientPane extends StackPane
{
	private Button uploadButton = new Button("UPLOAD"); 
	private Button listButton = new Button("PULL"); 
	private Button downloadButton = new Button("DOWNLOAD");
	private TextField downloadField = new TextField(); 
	private ImageView imageViewer = new ImageView(); 
	private TextArea  statusArea = new TextArea("Client Response:\n");
	private final int PORT = 9876;
	 private TextField uploadStatusLabel = new TextField();
	 private TextField downloadStatusLabel = new TextField();
	private PrintWriter writer = null; 
	private BufferedReader reader = null; 
	private Socket connection = null; 
	
	public ClientPane()
	{
		
		setup();
		//Connecting to Socket
		connect(); 
		
		//Pull button 
		listButton.setOnAction(event -> 
		{
			//send the Pull command to the server
			sendCommand("PULL"); 
			String line = ""; 
			try 
			{
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				line = reader.readLine(); 
			} catch (IOException e) 
			{
				e.printStackTrace();
			} 
			
			StringTokenizer tokenizer = new StringTokenizer(line, "#?"); 
			while(tokenizer.hasMoreTokens())
			{
				statusArea.appendText(tokenizer.nextToken() +"\n"); 
			}
		
			
		});
		
	    // Upload button
        uploadButton.setOnAction(event -> {
            final FileChooser fChooser = new FileChooser();
            fChooser.setTitle("Select Image to Upload");
            fChooser.setInitialDirectory(new File("data/client"));
            File fileToUpload = fChooser.showOpenDialog(null);

            // Uploading to server
            try {
                FileInputStream fis = new FileInputStream(fileToUpload);
                DataOutputStream dataOut = new DataOutputStream(connection.getOutputStream());

                sendCommand("POSTIMG" + "#?" + String.valueOf(new Random().nextInt(0, 100)) + "#?" + fileToUpload.getName() + "#?" + String.valueOf(fileToUpload.length()));

                // Sending the actual file
                byte[] buffer = new byte[1024];
                int n = 0;
                while ((n = fis.read(buffer)) > 0) {
                    dataOut.write(buffer);
                    dataOut.flush();
                }
                dataOut.close();
                fis.close();

                // Update the uploadStatusLabel on success
                uploadStatusLabel.setText("HAPPY");
                imageViewer.setImage(new Image("file:data/client/Custard-Slice.jpg"));
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
                System.err.println("File not found");
                // Update the uploadStatusLabel on failure
                uploadStatusLabel.setText("SAD");
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.err.println("Could not upload");
                // Update the uploadStatusLabel on failure
                uploadStatusLabel.setText("Upload failed.");
            }
        });
		
		//download button 
        downloadButton.setOnAction(event -> {
            if (downloadField.getText().isEmpty()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setContentText("Input Image ID");
                alert.showAndWait();
            } else {
            	try {
                    int ID = Integer.parseInt(downloadField.getText());
                    sendCommand("GETIMG#?" + ID);

                    String response = getResponse();
                    if (response != null) {
                        if (response.equals("NOTFOUND")) {
                            System.out.println("Failed to download image: File not found");
                        } else {
                            int fileSize = Integer.parseInt(response);
                            String fileName = ""; // Replace with the actual file name based on the image ID
                            File fileToReceive = new File("data/server/" + fileName);
                            try (FileOutputStream fos = new FileOutputStream(fileToReceive);
                                 DataInputStream dataIn = new DataInputStream(connection.getInputStream())) {

                                byte[] buffer = new byte[1024];
                                int n;
                                int totalBytes = 0;

                                while (totalBytes != fileSize) {
                                    n = dataIn.read(buffer, 0, buffer.length);
                                    fos.write(buffer, 0, n);
                                    fos.flush();
                                    totalBytes += n;
                                }

                                System.out.println("Image downloaded successfully");
                                downloadStatusLabel.setText("Download Successful");
                    }
                        }
                }} catch (FileNotFoundException e) {
                    System.err.println(e.getMessage());
                    System.err.println("Could not find file");
                    downloadStatusLabel.setText("Download Failed");
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    System.err.println("IOException");
                    downloadStatusLabel.setText("Download Failed");
                }
            }
        });
 

	}
	//Setting up the GUI
	private void setup()
	{
		downloadField.setPrefWidth(35);
		uploadButton.setPrefSize(100, 20); 
		listButton.setPrefSize(100, 20);
		downloadButton.setPrefSize(100, 20);
		imageViewer.setFitWidth(200); 
		imageViewer.setFitHeight(200); 
		statusArea.setMaxHeight(200);
		statusArea.setEditable(false);
		
		HBox retrieveBox = new HBox(5, downloadButton, downloadField); 
		HBox hBox = new HBox(10, listButton, uploadButton, retrieveBox);
		hBox.setAlignment(Pos.TOP_CENTER);
		hBox.setPadding(new Insets(20, 15, 20, 15));
		
		HBox centreBox = new HBox(10, imageViewer, statusArea);
		centreBox.setAlignment(Pos.CENTER);
		
		 VBox layout = new VBox(10, hBox, centreBox, uploadStatusLabel, downloadStatusLabel );
	     layout.setAlignment(Pos.CENTER);
	     this.getChildren().addAll(layout);
		
		
		StackPane.setMargin(hBox, new Insets(25, 25, 25, 25));
		
		this.getChildren().addAll(new VBox(20, hBox, centreBox)); 
	}
	
	//sending command
	private void sendCommand(String command)
	{
		try 
		{
			writer = new PrintWriter(connection.getOutputStream(), true);
			writer.println(command);
			writer.flush();
		}
		catch (IOException e) 
		{
			System.err.println(e.getMessage()); 
			System.err.println("Could not send command"); 
		} 
	}
	
	 /**
     * Retrieves a response from the server.
     *
     * @return The server's response.
     */
	private String getResponse()
	{
		String line = null; 
		
		try
		{
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			line = reader.readLine(); 
			return line; 
		} 
		catch (IOException e) 
		{
			System.err.println(e.getMessage()); 
			System.err.println("Could not get response"); 
			
		} 
		
		return null; 
	}
	
	/**
     * Connects to the server.
     */
	private void connect()
	{
		try
		{
			connection = new Socket("localhost", PORT);
			if(connection.isConnected())
			{
				Alert alert = new Alert(AlertType.CONFIRMATION); 
				alert.setContentText("Server Connecting to  " + connection.getPort());
				alert.showAndWait(); 
				
			}
		}
		catch(IOException e)
		{
			System.err.println(e.getMessage()); 
			System.err.println("Unable to connect"); 
		}
		
	}
}