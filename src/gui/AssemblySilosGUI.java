package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import network.SiloNetwork;
import network.SiloState;



public class AssemblySilosGUI extends Application {

    private static SiloNetwork network;
    private static final int ROWS = 3;
    private static final int COLS = 3;

    private boolean isPaused = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        HBox root = new HBox();
        root.setStyle("-fx-background-color: black;");

        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: black;");

        network = new SiloNetwork(ROWS, COLS, COLS * ROWS);


        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < ROWS; col++) {
                String program1 = "NOOP";
                SiloState silo = network.createSilo(row, col);
                silo.setCode(program1);
                gridPane.add(silo.getSiloGraphic(), col, row);

                Thread thread = silo.getThread();
                thread.start();

            }
        }

        Button startButton = new Button("Start");
        Button pauseButton = new Button("Pause");
        Button stopButton = new Button("Stop");


        startButton.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));
        startButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-min-width: 50; -fx-min-height: 50;");

        startButton.setOnAction(e -> {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    SiloState silo = network.getSiloState(row, col);
                    silo.updateInstructionsFromGraphic();
                    silo.toggleExecution(true);
                }
            }
            pauseButton.setText("Pause");
            isPaused = false;
        });



        pauseButton.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));
        pauseButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-min-width: 50; -fx-min-height: 50;");
        pauseButton.setOnAction(event -> {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (!isPaused) {
                        SiloState silo = network.getSiloState(row, col);
                        silo.toggleExecution(false);
                        isPaused = true;
                        pauseButton.setText("Step");
                    } else {
                        SiloState silo = network.getSiloState(row, col);
                        try {
                            silo.step();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }


                }
            }
        });


        stopButton.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));
        stopButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-min-width: 50; -fx-min-height: 50;");
        stopButton.setOnAction(event -> {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    SiloState silo = network.getSiloState(row, col);
                    silo.reset();
                }
            }
        });

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(startButton, pauseButton, stopButton);
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
