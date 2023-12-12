import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GradeCalculatorFX extends Application {

    private TextField[] subjectFields;
    private int numSubjects;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Grade Calculator");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Prompt user to enter the number of subjects
        Label numSubjectsLabel = new Label("Enter the number of subjects:");
        TextField numSubjectsField = new TextField();
        grid.add(numSubjectsLabel, 0, 0);
        grid.add(numSubjectsField, 1, 0);

        // Enter Button
        Button enterButton = new Button("Enter");
        grid.add(enterButton, 2, 0);

        // Calculate Button
        Button calculateButton = new Button("Calculate");
        grid.add(calculateButton, 0, 14);

        // Display Results
        Label totalMarksLabel = new Label("Total Marks:");
        Label averagePercentageLabel = new Label("Average Percentage:");
        Label gradeLabel = new Label("Grade:");
        grid.add(totalMarksLabel, 0, 15);
        grid.add(averagePercentageLabel, 0, 16);
        grid.add(gradeLabel, 0, 17);

        // Results for each subject
        Label[] subjectResultLabels = new Label[5]; // Assuming a maximum of 5 subjects, adjust as needed
        for (int i = 0; i < subjectResultLabels.length; i++) {
            subjectResultLabels[i] = new Label();
            grid.add(subjectResultLabels[i], 2, i + 1);
        }

        // Action for the Enter Button
        enterButton.setOnAction(e -> {
            // Get the number of subjects entered by the user
            numSubjects = Integer.parseInt(numSubjectsField.getText());

            // Labels and TextFields for entering marks
            Label[] subjectLabels = new Label[numSubjects];
            subjectFields = new TextField[numSubjects];
            for (int i = 0; i < numSubjects; i++) {
                subjectLabels[i] = new Label("Subject " + (i + 1) + ":");
                subjectFields[i] = new TextField();
                grid.add(subjectLabels[i], 0, i + 1);
                grid.add(subjectFields[i], 1, i + 1);
            }
        });

        // Action for the Calculate Button
        calculateButton.setOnAction(e -> {
            // Calculate total marks and average percentage
            int totalMarks = 0;
            for (TextField field : subjectFields) {
                totalMarks += Integer.parseInt(field.getText());
            }
            double averagePercentage = (double) totalMarks / numSubjects;

            // Grade Calculation: Assign grades based on the average percentage achieved
            char grade;
            if (averagePercentage >= 90) {
                grade = 'A';
            } else if (averagePercentage >= 80) {
                grade = 'B';
            } else if (averagePercentage >= 70) {
                grade = 'C';
            } else if (averagePercentage >= 60) {
                grade = 'D';
            } else {
                grade = 'F';
            }

            // Display Results
            totalMarksLabel.setText("Total Marks: " + totalMarks);
            averagePercentageLabel.setText("Average Percentage: " + String.format("%.2f", averagePercentage) + "%");
            gradeLabel.setText("Grade: " + grade);

            // Display results for each subject
            for (int i = 0; i < numSubjects; i++) {
                int marks = Integer.parseInt(subjectFields[i].getText());
                double subjectPercentage = (double) marks / 100 * 100;
                subjectResultLabels[i].setText("Subject " + (i + 1) + ": " + marks + " ( " + String.format("%.2f", subjectPercentage) + "% )");
            }
        });

        Scene scene = new Scene(grid, 800, 500);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
