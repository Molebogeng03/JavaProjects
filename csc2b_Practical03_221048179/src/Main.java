import acsse.csc2b.server.Server;

/**
 * @author Baloyi MP
 * @version Practical_03
 */
public class Main {

    public static void main(String[] args) {
        int port = 4321; //connects to port 4321
        
        Server s = new Server(port); //instantiating server
        s.connection(); //establishing connection
    }
}
