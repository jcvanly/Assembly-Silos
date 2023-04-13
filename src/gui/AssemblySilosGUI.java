package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
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
import network.Stream;
import java.io.File;
import java.io.IOException;

public class AssemblySilosGUI extends Application {

    private static SiloNetwork network;
    private int ROWS;
    private int COLS;
    private boolean isPaused = false;
    private Parser.InputFileData fileData;
    private HBox root;
    private HBox buttonBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parser inputParser = new Parser();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Input File");
        File inputFile = fileChooser.showOpenDialog(primaryStage);
        if (inputFile != null) {
            try {
                fileData = inputParser.parseInputFile(inputFile.getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ROWS = fileData.getNumRows();
            COLS = fileData.getNumCols();
            network = new SiloNetwork(ROWS, COLS, fileData.getInputStreams(), fileData.getOutputStreams());
        } else {
            System.exit(0);
        }

        root = new HBox();
        root.setStyle("-fx-background-color: black;");
        GridPane gridPane = createGridPane();

        Button pauseButton = createPauseButton();
        Button startButton = createStartButton(pauseButton);
        Button stopButton = createStopButton();

        buttonBox = createButtonBox(startButton, pauseButton, stopButton);
        HBox streamsBox = populateStreams(gridPane);
        VBox sideDisplay = createSideDisplay(streamsBox,buttonBox);

        populateGridPaneWithSilos(gridPane);

        root.getChildren().addAll(sideDisplay, gridPane);

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

    private void populateGridPaneWithSilos(GridPane gridPane) {
        int gridRows = ROWS + 2;
        int gridCols = COLS + 2;
        int counter = 0;
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                if (row == 0 || row == gridRows - 1 || col == 0 || col == gridCols - 1) {
                    //do nothing
                } else {
                    String program;
                    if (counter >= fileData.getSiloInstructions().size()) {
                        program = "";
                    } else {
                        program = fileData.getSiloInstructions().get(counter);
                    }
                    SiloState silo = network.createSilo(row - 1, col - 1);
                    silo.getSiloGraphic().setCodeArea(program);
                    gridPane.add(silo.getSiloGraphic(), col, row);
                    counter++;
                }
            }
        }
    }

    private HBox populateStreams(GridPane gridPane) {
        HBox streamsBox = new HBox();

        //For each input & out put stream, get graphic and add to streamsBox
        for (int i = 0; i < network.getInputStreams().size(); i++) {
            Stream stream = network.getInputStreams().get(i);
            StreamGraphic streamGraphic = stream.getStreamGraphic();
            streamGraphic.setStreamLabel("IN." + (char)('A' + i));
            streamsBox.getChildren().add(streamGraphic);
            GridPane.setHalignment(streamGraphic.getStreamLabel(), HPos.CENTER);
            gridPane.add(streamGraphic.getStreamLabel(),stream.getCol()+1, stream.getRow()+1);
        }
        for (int i = 0; i < network.getOutputStreams().size(); i++) {
            Stream stream = network.getOutputStreams().get(i);
            StreamGraphic streamGraphic = stream.getStreamGraphic();
            streamGraphic.setStreamLabel("OUT." + (char)('A' + i));
            streamsBox.getChildren().add(streamGraphic);
            GridPane.setHalignment(streamGraphic.getStreamLabel(), HPos.CENTER);
            gridPane.add(streamGraphic.getStreamLabel(), stream.getCol()+1, stream.getRow()+1);
        }

        streamsBox.setAlignment(Pos.CENTER);
        streamsBox.setSpacing(20);
        return streamsBox;
    }

    private VBox createSideDisplay(HBox streamsBox, HBox buttonBox) {
        VBox sideDisplay = new VBox();

        sideDisplay.getChildren().addAll(streamsBox, buttonBox);
        sideDisplay.setSpacing(20);
        sideDisplay.setAlignment(Pos.CENTER);
        return sideDisplay;
    }

    private HBox createButtonBox(Button startButton, Button pauseButton, Button stopButton) {
        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(startButton, pauseButton, stopButton);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonBox.setSpacing(20);
        return buttonBox;
    }

    private Button createStopButton() {
        Button stopButton = new Button("Stop");
        styleButton(stopButton);
        stopButton.setOnAction(event -> {
            reset();
        });
        return stopButton;
    }

    private Button createPauseButton() {
        Button pauseButton = new Button("Pause");
        styleButton(pauseButton);

        pauseButton.setOnAction(event -> {
            if (!isPaused) {
                network.pauseSilos();
                pauseButton.setText("Step");
                isPaused = true;
            } else {
                network.stepSilos();
            }
        });
        return pauseButton;
    }

    private Button createStartButton(Button pauseButton) {
        Button startButton = new Button("Start");
        styleButton(startButton);
        startButton.setOnAction(e -> {
            network.startSilos();
            pauseButton.setText("Pause");
            isPaused = false;
        });
        return startButton;
    }

    private void styleButton(Button button) {
        button.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));
        button.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-min-width: 50; -fx-min-height: 50;");
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: black;");
        return gridPane;
    }

    // Reconstruct entire display from the stored filedata
    public void reset() {
        network.stopSilos();
        network.stopThreads();
        network = new SiloNetwork(ROWS, COLS, fileData.getInputStreams(), fileData.getOutputStreams());
        GridPane gridPane = createGridPane();
        HBox streamsBox = populateStreams(gridPane);
        VBox sideDisplay = createSideDisplay(streamsBox, buttonBox);
        populateGridPaneWithSilos(gridPane);
        root.getChildren().clear();
        root.getChildren().addAll(sideDisplay, gridPane);
    }
}
