package JavaFx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Test extends Application {

    @Override
    public void start(Stage primaryStage) {
        int numRows = 4;
        int numCols = 4;
        int numSilos = numRows * numCols;

        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: black;");
        gridPane.setPadding(new javafx.geometry.Insets(50, 50, 50, 50));
        gridPane.setHgap(50);
        gridPane.setVgap(50);

        // Create grid of Silos
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (row * numCols + col >= numSilos) {
                    break;
                }
                Silo silo = new Silo();
                gridPane.add(silo, col, row);
            }
        }

        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
