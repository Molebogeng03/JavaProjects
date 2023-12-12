package acsse.csc2b.client;
/**
 * @author Baloyi MP 221048179
 * @version Practical 04
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ImgClient extends Application
{ 
	public ImgClient ()
	{
		
	}
	
	public static void main(String [] args)
	{
		Application.launch(args);
	}

	/**
     * Starts the JavaFX application by creating and displaying the main client window.
     *
     * @param primaryStage The primary stage for the JavaFX application.
     */
	@Override
	public void start(Stage primaryStage)
	{
		try 
		{
			ClientPane pane = new ClientPane(); 
			Scene myScene = new Scene(pane); 
			
			primaryStage.setScene(myScene);
			primaryStage.setHeight(700);
			primaryStage.setWidth(950);
			primaryStage.setTitle("Image Transfer");
			primaryStage.show(); 
			
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage()); 
			System.err.println("Stage Exception"); 
		}
	}
	
}
