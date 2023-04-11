package gui;

import commands.Instruction;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import network.Parser;
import network.SiloNetwork;
import network.SiloState;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class AssemblySilosGUI extends Application {

    private static SiloNetwork network;
    private int ROWS;
    private int COLS;
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

        Parser inputParser = new Parser();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Input File");
        File inputFile = fileChooser.showOpenDialog(primaryStage);
        if (inputFile != null) {
            Parser.InputFileData fileData = null;
            try {
                fileData = inputParser.parseInputFile(inputFile.getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ROWS = fileData.numRows;
            COLS = fileData.numCols;

            network = new SiloNetwork(ROWS, COLS, COLS * ROWS);

            int counter = 0;
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    String program;
                    if (counter >= fileData.siloInstructions.size()) {
                        program = "";
                    } else {
                        program = fileData.siloInstructions.get(counter);
                    }
                    SiloState silo = network.createSilo(row, col);
                    silo.setCode(program);
                    gridPane.add(silo.getSiloGraphic(), col, row);
                    counter++;
                }
            }
        } else {
            System.exit(0);
        }

        Button startButton = new Button("Start");
        Button pauseButton = new Button("Pause");
        Button stopButton = new Button("Stop");


        startButton.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));
        startButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-min-width: 50; -fx-min-height: 50;");

        startButton.setOnAction(e -> {
            network.startSilos();
            pauseButton.setText("Pause");
            isPaused = false;
        });



        pauseButton.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));
        pauseButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-min-width: 50; -fx-min-height: 50;");
        pauseButton.setOnAction(event -> {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (!isPaused) {
                        network.stopSilos();
                        isPaused = true;
                        pauseButton.setText("Step");
                    } else {
                        network.stepSilos();
                    }
                }
            }
        });


        stopButton.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));
        stopButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-min-width: 50; -fx-min-height: 50;");
        stopButton.setOnAction(event -> {
            network.resetSilos();
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
            network.stopThreads();
            Platform.exit();
            System.exit(0);
        });

    }



}
