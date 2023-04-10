package JavaFx;


import commands.Instruction;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.Parser;
import network.SiloNetwork;
import network.SiloState;

import java.util.List;


public class SiloApp extends Application {

    private static SiloNetwork network;
    private static final int ROWS = 2;
    private static final int COLS = 2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        FlowPane root = new FlowPane();

        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: black;");
        gridPane.setPadding(new javafx.geometry.Insets(50, 50, 50, 50));
        gridPane.setHgap(50);
        gridPane.setVgap(50);


        network = new SiloNetwork(ROWS, COLS, COLS * ROWS);
        Parser parser = new Parser();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < ROWS; col++) {
                String program1 = "ADD 1\n" + "SAVE\n" + "ADD 1";
                List<Instruction> instructions = parser.parse(program1);
                SiloState silo = network.createSilo(row, col);
                silo.setInstructions(instructions);
                silo.setCode(program1);
                gridPane.add(silo.getSiloGraphic(), col, row);

                Thread thread = silo.getThread();
                thread.start();

            }
        }


        Button startButton = new Button("Start");
        startButton.setOnAction(e -> {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    SiloState silo = network.getSiloState(row, col);
                    silo.toggleExecution();
                }
            }
        });

        Button stopButton = new Button("Stop");
        stopButton.setOnAction(event -> {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    SiloState silo = network.getSiloState(row, col);
                    silo.toggleExecution();
                }
            }
        });

        Button stepButton = new Button("Step");
        stepButton.setOnAction(event -> {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    SiloState silo = network.getSiloState(row, col);
                    try {
                        silo.step();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        root.getChildren().addAll(gridPane, startButton, stopButton, stepButton);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Assembly Silo Program");
        primaryStage.show();
    }


}
