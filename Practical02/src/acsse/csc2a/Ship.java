package acsse.csc2a;
/**
 * @author MP Baloyi
 * @version P02
 */
public class Ship {
	/**
	 * The Ship class represents a spaceship that transports messages between planets.
	 */
	
	    private String ShipID; // Unique identifier of the spaceship
	    private static String ShipName; // Name of the spaceship
	    private static Message[] arrmessages; // Array of messages carried by the spaceship

	    /**
	     * This is the Ship constructor where we instantiate
	     * @param id       the ID of the spaceship, in the format SHXXXX where X is a digit
         * @param name     the name of the spaceship
         * @param messages an array of Message objects that the spaceship is carrying
	     */
	    public Ship(String id, String name, Message[] messages) {
	        this.ShipID = "SH"+id;
	        Ship.ShipName = name;
	        Ship.arrmessages = messages;
	    }

	    
	    /**
	     * Displays the messages carried by the spaceship.
	     */
	   
	    public void printMessages() {
	        System.out.println("SpaceShip " + ShipID + " (" + ShipName + ")"); //printing ship details 
			for (Message message : arrmessages) {
	            System.out.println(message);
	        }
	        System.out.println();
	    }
	    

	    
	    /**
	     * Gets the unique identifier of the spaceship.
	     *
	     * @return the unique identifier of the spaceship
	     */
	    public String getShipID() {
	        return ShipID;
	    }
	    
	    /**
	     * Sets the unique identifier of the spaceship.
	     *
	     * @param ID the unique identifier of the spaceship
	     */
	    public void setID(String ID) {
	        this.ShipID = ID;
	    }
        
	    /**
	     * Gets the name of the spaceship.
	     *
	     * @return the name of the spaceship
	     */
	    public String getShipName() {
	        return ShipName;
	    }
	    
	    /**
	     * Sets the name of the spaceship.
	     *
	     * @param name the name of the spaceship
	     */
	    public void setName(String name) {
	        Ship.ShipName = name;
	    }
	    
	    /**
	     * Gets the array of messages carried by the spaceship.
	     *
	     * @return the array of messages carried by the spaceship
	     */
	    public static  Message[] getMessage() {
	        return arrmessages;
	    }
	    
	   

	    /**
	     * This is where we set the message array
	     * @param messages Messages of each ship
	     */
	    public void setMessages(Message[] messages) {
	        Ship.arrmessages = messages;
	    }
}
