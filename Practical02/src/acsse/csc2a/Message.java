package acsse.csc2a;
/**
 * @author MP Baloyi
 * @version P02
 */

/**
 * Represents a message that can be transported by a spaceship.
 */
public class Message {
	
	/**
	 * This this the enumeration for the nine planets
	 *
	 */
	public enum Planet {
	    MERCURY,
	    VENUS,
	    EARTH,
	    MARS,
	    JUPITER,
	    SATURN,
	    URANUS,
	    NEPTUNE,
	    PLUTO  
	}
	
	//Declaration of the variables
	    
	    private String MSGID; // Unique identifier of the message
	    public  String language; // language of the message
	    private String contents; // Text of the message
	    private Planet sourcePlanet; // Planet from which the message is being sent
	    private Planet destinationPlanet; // Planet to which the message is being sent
	    
	    /**
	     * Constructs a new Message object with the given ID, contents, source planet,
	     * destination planet, and language.
	     * 
	     * @param id                the unique identifier of the message
	     * @param contents          the text of the message
	     * @param sourcePlanet      the planet from which the message is being sent
	     * @param destinationPlanet the planet to which the message is being sent
	     * @param language          the language of the message
	     */
	    public Message(String id, String language, String contents,Planet sourcePlanet, Planet destinationPlanet) {
	        
	        this.MSGID = "MSG"+id;
	        this.language= language;
	        this.contents = contents;
	        this.sourcePlanet = sourcePlanet;
	        this.destinationPlanet = destinationPlanet;
	    }


	    //accessors and mutators of the Message attributes 
	    /**
	     * Returns the unique identifier of the message.
	     * Gets the Message ID
	     * @return the ID of the message
	     */
	    //Getting and setting the Message ID
	    public String getMSGID() {
	        return MSGID;
	    }
	    
	    /**
	     * @param ID Sets the Message ID
	     */
	    public void setMSGID(String ID) {
	        this.MSGID = ID;
	    }

	    //Getting and setting the language
	    /**
	     * @return Gets the language of the message
	     */
	    public String getLanguage() {
	        return language;
	    }
	    
	    /**
	     * @param language Sets the language of the message
	     */
	    public void setLanguage(String language) {
	        this.language = language;
	    }

	    //Getting and setting the contents
	    /**
	     * @return Gets the contents of the message
	     */
	    public String getContents() {
	        return contents;
	    }
	    
	    /**
	     * @param contents Sets contents of the message
	     */
	    public void setContents(String contents) {
	        this.contents = contents;
	    }
	    
	    /**
	     * Returns the planet from which the message is being sent.
	     * 
	     * @return the source planet of the message
	     */
	    //Getting and setting the source planet
	    public Planet getSourcePlanet() {
	        return sourcePlanet;
	    }
	    
	    /**
	     * @param sourcePlanet Sets the planet the message is sent from
	     */
	    public void setSourcePlanet(Planet sourcePlanet) {
	        this.sourcePlanet = sourcePlanet;
	    }

	    //Getting and setting the destination planet
	    /**
	     * @return Gets the planet the message is sent to
	     */
	    public Planet getDestinationPlanet() {
	        return destinationPlanet;
	    }

	    /**
	     * @param destinationPlanet Sets the planet the message is sent to
	     */
	    public void setDestinationPlanet(Planet destinationPlanet) {
	        this.destinationPlanet = destinationPlanet;
	    }

	    /**
	     * toString () method of the Message class returns the string representation of the Message
	     */
	    public String toString() {
	    	 return "Message " + MSGID + ": " + "Contents.... " +contents+"     "+" Language.... " + language+"     " +" From.... " + sourcePlanet+"     " + " To.... " + destinationPlanet;
	    }
	

}
