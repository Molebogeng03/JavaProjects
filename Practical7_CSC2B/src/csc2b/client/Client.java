package csc2b.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * @author Baloyi MP
 *
 */
public class Client extends Application {

	public static void main(String[] args) {
	  launch(args);	
	}
	/**
	 *  Client start method
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		ZEDEMClientPane root = new ZEDEMClientPane();
		Scene scene = new Scene(root, 800,800);
		primaryStage.setTitle("ZEDEMClient");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
