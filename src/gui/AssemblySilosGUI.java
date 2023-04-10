package gui;

import commands.Instruction;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import network.Parser;
import network.SiloNetwork;
import network.SiloState;

import java.util.List;
import java.util.Objects;


public class AssemblySilosGUI extends Application {

    private static SiloNetwork network;
    private static final int ROWS = 2;
    private static final int COLS = 2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        HBox root = new HBox();
        root.setStyle("-fx-background-color: black;");

        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: black;");
        gridPane.setPadding(new javafx.geometry.Insets(50, 50, 50, 50));
        gridPane.setHgap(50);
        gridPane.setVgap(50);


        network = new SiloNetwork(ROWS, COLS, COLS * ROWS);


        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < ROWS; col++) {
                String program1 = "ADD 1\n" + "SAVE\n" + "ADD 1";
                SiloState silo = network.createSilo(row, col);
                silo.setCode(program1);
                gridPane.add(silo.getSiloGraphic(), col, row);

                Thread thread = silo.getThread();
                thread.start();

            }
        }


        Button startButton = new Button("Start");
        startButton.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));
        startButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-min-width: 50; -fx-min-height: 50;");

        startButton.setOnAction(e -> {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    SiloState silo = network.getSiloState(row, col);
                    silo.toggleExecution();
                    silo.updateInstructionsFromGraphic();
                }
            }
        });

        Button stopButton = new Button("Stop");
        stopButton.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));
        stopButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-min-width: 50; -fx-min-height: 50;");
        stopButton.setOnAction(event -> {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    SiloState silo = network.getSiloState(row, col);
                    silo.toggleExecution();
                }
            }
        });

        Button stepButton = new Button("Step");
        stepButton.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));
        stepButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-min-width: 50; -fx-min-height: 50;");
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

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(startButton, stopButton, stepButton);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonBox.setSpacing(20);

        root.getChildren().addAll(buttonBox, gridPane);


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Assembly Silo Program");
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            stopThreads();
            Platform.exit();
            System.exit(0);
        });

    }

    private void stopThreads() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                SiloState silo = network.getSiloState(row, col);
                silo.getThread().interrupt();
            }
        }
    }


}
