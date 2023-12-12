import acsse.csc2a.Message;


import acsse.csc2a.Message.Planet;
import acsse.csc2a.Ship;
/**
 * @author MP Baloyi
 * @version P02
 */
public class Main {

	public static void main(String[] args) {
		
		Ship ship1 = new Ship("0001", "Destiny", new Message[]{
                new Message("000001", "English", "Good Morning!", Planet.EARTH, Planet.JUPITER),
                new Message("000002", "Pedi", "Dumela!", Planet.VENUS, Planet.EARTH),
                new Message("000003", "Zulu", "Sawubona!", Planet.MARS, Planet.SATURN)
        });
		
		/**
	     * Printing the messages in each ship {@Link Ship#printMessages}
	     * 
	     */
		 ship1.printMessages();

	        Ship ship2 = new Ship("0002", "Lexx", new Message[]{
	                new Message("000004", "English", "Please to meet you", Planet.EARTH, Planet.SATURN),
	                new Message("000005", "Pedi", "Ke thabela go go tseba", Planet.JUPITER,Planet.MARS),
	                new Message( "000006","Zulu","Ngiya jabula ukukwazi",Planet.MERCURY,Planet.URANUS)
	        });
	        ship2.printMessages(); //PrintMessage function is called for each ship
	        
	        Ship ship3 = new Ship("0003", "Orion", new Message[]{
	                new Message("000021", "English", "Good night", Planet.NEPTUNE, Planet.JUPITER),
	                new Message("000022", "Tsonga", "U etlela kahle", Planet.VENUS, Planet.SATURN),
	                new Message("000033", "Venda", "Lo kovhela", Planet.PLUTO, Planet.MARS)
	        });
	        ship3.printMessages();
		
	}}
