package acsse.csc2b.thread;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * @author Baloyi MP
 * @version Practical_03
 */
public class Client implements Runnable {

    private final Socket socket;

    /**
     * Initializes a new instance of the ClientHandler class.
     *
     * @param socket
     */
    public Client(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            request();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closing();
        }
    }

    //Handles the client request.
     
    private void request() throws IOException {
        BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());

        String request = read.readLine();

        if (request != null) {
            System.out.println("Received request: " + request);
            StringTokenizer requestToken = new StringTokenizer(request);
            String requestMethod = requestToken.nextToken();
            String requestPage = requestToken.nextToken();

            if ("GET".equals(requestMethod)) {
                handleGetRequest(dataOutput, requestPage);
            } else {
                sendResponse(dataOutput, "500 Internal Server Error", "text/plain", "500 Internal Server Error");
            }
        }
    }

    /**
     * Handles a GET request from the client.
     *
     * @param dataOutput
     * @param request
     * @throws IOException
     */
    private void handleGetRequest(DataOutputStream dataOutput, String request) throws IOException {
        switch (request) {
            case "/Afrikaans":
                showPage(dataOutput, "Afrikaans.html");
                break;
            case "/Zulu":
                showPage(dataOutput, "Zulu.html");
                break;
            case "/ZuluWithImage":
                showPage(dataOutput, "ZuluWithImage.html");
                break;
            case "/Africa.jpg":
                showImage(dataOutput, "Africa.jpg");
                break;
            default:
                sendResponse(dataOutput, "404 Not Found", "text/plain", "404 Not Found");
                break;
        }
    }

    /**
     * Displays page contents.
     *
     * @param dataOutput  - The data output stream to write the response.
     * @param filename - The name of the HTML file to display.
     * @throws IOException If an I/O error occurs.
     */
    private void showPage(DataOutputStream dataOutput, String filename) throws IOException {
        File file = new File("data/" + filename);

        if (file.exists()) {
            sendResponse(dataOutput, "200 OK", "text/html", readFileContents(file));
        } else {
            sendResponse(dataOutput, "404 Not Found", "text/plain", "404 Not Found");
        }
    }

    /**
     * Displays image content.
     *
     * @param dataOutput  - The data output stream to write the response.
     * @param filename - The name of the image file to display.
     * @throws IOException If an I/O error occurs.
     */
    private void showImage(DataOutputStream dataOutput, String filename) throws IOException {
        File file = new File("data/" + filename);

        if (file.exists()) {
            sendImageResponse(dataOutput, "200 OK", "image/jpeg", file);
        } else {
            sendResponse(dataOutput, "404 Not Found", "text/plain", "404 Not Found");
        }
    }

    /**
     * Reads the contents of files.
     *
     */
    private String readFileContents(File file) throws IOException {
        BufferedReader read = new BufferedReader(new FileReader(file));
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = read.readLine()) != null) {
            content.append(line);
        }

        read.close();
        return content.toString();
    }

    /**
     * Sends an HTTP response to the client.
     *
     * @param dataOutput     - The data output stream to write the response.
     * @param status      - The HTTP status code and message.
     * @param contentType - The response content type.
     * @param content     - The contents to send.
     * @throws IOException If an I/O error occurs.
     */
    private void sendResponse(DataOutputStream dataOutput, String status, String contentType, String content)
            throws IOException {
    	dataOutput.writeBytes("HTTP/1.1 " + status + "\r\n");
    	dataOutput.writeBytes("Content-Type: " + contentType + "\r\n");
    	dataOutput.writeBytes("\r\n");
    	dataOutput.writeBytes(content);
    	dataOutput.flush();
    }

    //sending image response
    private void sendImageResponse(DataOutputStream dataOutput, String status, String contentType, File file)
            throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        fileInputStream.read(buffer);
        fileInputStream.close();

        dataOutput.writeBytes("HTTP/1.1 " + status + "\r\n");
        dataOutput.writeBytes("Content-Type: " + contentType + "\r\n");
        dataOutput.writeBytes("Content-Length: " + buffer.length + "\r\n");
        dataOutput.writeBytes("\r\n");
        dataOutput.write(buffer);
        dataOutput.flush();
    }

    // Closing the socket.
     
    private void closing() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
