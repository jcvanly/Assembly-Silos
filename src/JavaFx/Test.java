package JavaFx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Test extends Application {

    @Override
    public void start(Stage primaryStage) {
        int numRows = 3;
        int numCols = 3;

        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: black;");
        gridPane.setPadding(new javafx.geometry.Insets(50, 50, 50, 50));
        gridPane.setHgap(50);
        gridPane.setVgap(50);

        // Create grid of Silos
        for (int row = 1; row < numRows+1; row++) {
            for (int col = 1; col < numCols+1; col++) {

            }
        }

        Label testlabel = new Label("IN.A");
        testlabel.setStyle("-fx-text-fill: white;");
        gridPane.add(testlabel, 1, 0);
        testlabel.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));
        GridPane.setHalignment(testlabel, javafx.geometry.HPos.CENTER);

        Label testlabel2 = new Label("OUT.A");
        testlabel2.setStyle("-fx-text-fill: white;");
        gridPane.add(testlabel2, 3, 4);
        testlabel2.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));
        GridPane.setHalignment(testlabel2, javafx.geometry.HPos.CENTER);


        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
