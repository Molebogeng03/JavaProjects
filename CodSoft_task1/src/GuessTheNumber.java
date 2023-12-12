import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class GuessTheNumber extends Application {

    private Random random = new Random();
    private int hiddenNumber;
    private int attempts;
    private int roundsWon;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Guess the Random number");
        generateRandomNumber();

        
        Label command = new Label("Enter your guess (between 1 and 100):");
        TextField numInput = new TextField();
        Button guessButton = new Button("Submit Guess");
        Text resultText = new Text();
        Button playAgainButton = new Button("Play Again");
        Label roundsWonLabel = new Label("Rounds won: 0");

       
        guessButton.setOnAction(e -> handleGuess(numInput, resultText, playAgainButton, roundsWonLabel));
        playAgainButton.setOnAction(e -> playAgain(numInput, resultText, playAgainButton, roundsWonLabel));

       
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(command, numInput, guessButton, resultText, playAgainButton, roundsWonLabel);

      
        Scene scene = new Scene(layout, 500, 200);
        primaryStage.setScene(scene);

      
        primaryStage.show();
    }

    private void generateRandomNumber() {
    	hiddenNumber = random.nextInt(100) + 1;
        attempts = 0;
    }

    private void handleGuess(TextField numInput, Text resultText, Button playAgainButton, Label roundsWonLabel) {
      
        attempts++;

   
        int userGuess = Integer.parseInt(numInput.getText());

       
        if (userGuess == hiddenNumber) {
            resultText.setText("Congratulations! You guessed the correct number in " + attempts + " attempts.");
            roundsWon++;
            playAgainButton.setDisable(false); 
            roundsWonLabel.setText("Rounds won: " + roundsWon);
        } else if (userGuess < hiddenNumber) {
            resultText.setText("Too low! Try again.");
        } else {
            resultText.setText("Too high! Try again.");
        }


        if (attempts == 5) {
            resultText.setText("Oh no, maximum attempts reached. The correct number was " + hiddenNumber + ".");
            playAgainButton.setDisable(false); 
        }
    }

    private void playAgain(TextField numInput, Text resultText, Button playAgainButton, Label roundsWonLabel) {
        generateRandomNumber();
        numInput.clear();
        resultText.setText("");
        playAgainButton.setDisable(true); 
    }
}
