
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

/**
 * A Simple SMTP Client application to send emails using SMTP protocol.
 * @author MP Baloyi
 */
public class Main extends Application {
    private TextField hostField;
    private TextField portField;
    private TextField senderField;
    private TextField recipientField;
    private TextArea contentArea;
    private TextField connectionStatusField;
    private TextField emailDetailsField;
    private TextField subjectField;

    private boolean isConnected = false; // Track connection status
    private Button sendButton; // sendButton 

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simple SMTP Client");

        hostField = new TextField();
        portField = new TextField();
        senderField = new TextField();
        recipientField = new TextField();
        contentArea = new TextArea();
        connectionStatusField = new TextField();
        connectionStatusField.setEditable(false);
        emailDetailsField = new TextField();
        emailDetailsField.setEditable(false);
        subjectField = new TextField();

        Button connectButton = new Button("Connect");
        connectButton.setOnAction(e -> connectToHost());

        sendButton = new Button("Send"); // Initialize the sendButton at the class level
        sendButton.setOnAction(e -> sendEmail());
        sendButton.setDisable(true); // Disable send button until connected

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        gridPane.add(new Label("Host:"), 0, 0);
        gridPane.add(hostField, 1, 0);

        gridPane.add(new Label("Port:"), 0, 1);
        gridPane.add(portField, 1, 1);

        gridPane.add(connectButton, 0, 2); // Move the connectButton to column 2, row 1
        gridPane.add(connectionStatusField, 1, 2); // Span 2 columns for the connectionStatusField

        gridPane.add(new Label("Sender:"), 0, 3);
        gridPane.add(senderField, 1, 3);

        gridPane.add(new Label("Recipient:"), 0, 4);
        gridPane.add(recipientField, 1, 4);

        gridPane.add(new Label("Subject:"), 0, 5);
        gridPane.add(subjectField, 1, 5);

        gridPane.add(new Label("Content:"), 0, 6);
        gridPane.add(contentArea, 1, 6);

        gridPane.add(sendButton, 0, 7); // Move the sendButton to column 1, row 7
        gridPane.add(emailDetailsField, 1, 7); // Move the emailDetailsField to column 1, row 8

        Button clearButton = new Button("Clear All");
        clearButton.setOnAction(e -> clearFields()); // Attach event handler to the clear button
        gridPane.add(clearButton, 1, 8); // Add the clear button to column 1, row 8

        VBox vbox = new VBox(gridPane);
        vbox.setSpacing(10);

        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Clear all input fields and reset the connection status.
     */
    private void clearFields() {
        hostField.clear();
        portField.clear();
        senderField.clear();
        recipientField.clear();
        subjectField.clear();
        contentArea.clear();
        connectionStatusField.clear();
        emailDetailsField.clear();
        isConnected = false;
        sendButton.setDisable(true); // Disable the send button after clearing the fields
    }

    /**
     * Connect to the SMTP host with the provided hostname and port number.
     * Displays connection status in the GUI.
     */
    private void connectToHost() {
        String hostname = hostField.getText();
        int port = Integer.parseInt(portField.getText());

        try {
            try (Socket socket = new Socket(hostname, port)) {
                isConnected = true;
                String connectionStatus = "Connected to the SMTP server.";
                connectionStatusField.setText(connectionStatus);

                // Enable the send button after successful connection
                sendButton.setDisable(false);
            } catch (IOException e) {
                isConnected = false;
                String connectionStatus = "Error connecting to the SMTP server. Please check the host and port.";
                connectionStatusField.setText(connectionStatus);
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            isConnected = false;
            String connectionStatus = "Error: Invalid port number.";
            connectionStatusField.setText(connectionStatus);
        }
    }

    /**
     * Send the email using SMTP connection.
     *  displays the email details in the GUI.
     */
    private void sendEmail() {
        if (!isConnected) {
            String connectionStatus = "Not connected to the SMTP server. Please connect first.";
            connectionStatusField.setText(connectionStatus);
            return;
        }

        String sender = senderField.getText() + "@csc2b.uj.ac.za";
        String recipient = recipientField.getText() + "@csc2b.uj.ac.za";
        String subject = subjectField.getText();
        String content = contentArea.getText();

        try (Socket socket = new Socket(hostField.getText(), Integer.parseInt(portField.getText()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            this.readResponse(in); // Read the server's welcome message

            this.writeMessage(out, "HELO " + hostField.getText());
            this.readResponse(in);

            this.writeMessage(out, "MAIL FROM: <" + sender + ">");
            this.readResponse(in);

            this.writeMessage(out, "RCPT TO: <" + recipient + ">");
            this.readResponse(in);

            this.writeMessage(out, "DATA");
            this.readResponse(in);

            // Sending the email content
            this.writeMessage(out, "From: <" + sender + ">");
            this.writeMessage(out, "To: <" + recipient + ">");
            this.writeMessage(out, "Subject: " + subject);
            this.writeMessage(out, content);
            this.writeMessage(out, ".");
            this.readResponse(in);

            this.writeMessage(out, "QUIT");
            this.readResponse(in);

            //  email details field
            String emailDetails = "From: " + sender + "\n"
                    + "To: " + recipient + "\n"
                    + "Subject: " + subject + "\n"
                    + "Content:\n" + content;
            emailDetailsField.setText(emailDetails);

            // Clear the email details field
            emailDetailsField.clear();

            // Update the connection status field with the success message
            String emailStatus = "Email Sent Successfully!";
            emailDetailsField.setText(emailStatus);

        } catch (IOException e) {
            e.printStackTrace();
            String emailStatus = "Error sending email. Please check the connection and email details.";
            emailDetailsField.setText(emailStatus);
        }
    }

    //WriteMessage function
    private void writeMessage(BufferedWriter out, String message) throws IOException {
        out.write(message + "\r\n");
        out.flush();
    }
    
    
     /**
	     * Method to read the server response from the BufferedReader.
	     *
	     * @param reader -read the server response.
	     * @throws IOException If an I/O error occurs.
	     */
    private void readResponse(BufferedReader in) throws IOException {
        String response = in.readLine();
        System.out.println("Server: " + response);
        connectionStatusField.setText("Server: " + response);
    }
   }
