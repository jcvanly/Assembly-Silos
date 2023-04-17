package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import network.Parser;
import network.SiloNetwork;
import network.SiloState;
import network.Stream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javafx.scene.image.Image;


/**
 * Luke McDougall, Jack Vanlyssel, Spoorthi Menta
 *
 * This Java code is an implementation of a graphical user interface (GUI) for an
 * assembly silo simulation. The application extends the JavaFX Application class
 * and makes use of JavaFX components such as Stage, Scene, GridPane, HBox, VBox,
 * and Button to create the layout and controls. The simulation is based on a
 * SiloNetwork class that represents a network of silos and input/output streams.
 * The simulation can be started, paused, stepped through, and stopped using the
 * buttons provided in the GUI.
 */

public class AssemblySilosGUI extends Application {

    private static SiloNetwork network;
    private int ROWS;
    private int COLS;
    private boolean isPaused = false;
    private File inputFile;
    private Parser.InputFileData fileData;
    private HBox root;
    private HBox buttonBox;

    /**
     * main launches the javaFX
     */

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method sets up the primary stage and initializes the layout,
     * including creating and populating the GridPane with silos, input and
     * output streams, and control buttons.
     */
    @Override
    public void start(Stage primaryStage) {
        Parameters params = getParameters();
        List<String> args = params.getRaw();

        if (args.isEmpty()) {
            Platform.exit();
            return;
        }

        String inputFilePath = args.get(0);
        inputFile = new File(inputFilePath);

        parseInputFile();

        root = new HBox();
        root.setStyle("-fx-background-color: black;");
        GridPane gridPane = createGridPane();

        Button startButton = new Button("Start");
        styleButton(startButton);

        Button pauseButton = new Button("Pause");
        styleButton(pauseButton);

        Button stopButton = new Button("Stop");
        styleButton(stopButton);

        configureStartButton(startButton, pauseButton);
        configurePauseButton(pauseButton);
        configureStopButton(stopButton);

        buttonBox = createButtonBox(startButton, pauseButton, stopButton);
        HBox streamsBox = populateStreams(gridPane);
        VBox sideDisplay = createSideDisplay(streamsBox,buttonBox);

        populateGridPaneWithSilos(gridPane);

        root.getChildren().addAll(sideDisplay, gridPane);

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        root.prefWidthProperty().bind(primaryStage.widthProperty());
        root.prefHeightProperty().bind(primaryStage.heightProperty());


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setWidth(screenSize.getWidth());
        primaryStage.setHeight(screenSize.getHeight());
        primaryStage.setTitle("Assembly Silos");
        Image windowIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon.png")));
        primaryStage.getIcons().add(windowIcon);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            network.stopThreads();
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * The parseInputFile method reads input data from a file, creates a new
     * SiloNetwork instance, and sets the number of rows and columns based
     * on the input file.
     */

    private void parseInputFile() {
        Parser inputParser = new Parser();
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
    }

    /**
     * The populateGridPaneWithSilos method adds the silos to the GridPane.
     */

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

    /**
     * The populateStreams method adds input and output streams
     * to an HBox, which is used to display their graphics.
     */

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

    /**
     * The createSideDisplay, createButtonBox, and createStopButton, createPauseButton,
     * and createStartButton methods create and style the buttons for controlling the
     * simulation.
     */

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

    private void configureStartButton(Button startButton, Button pauseButton) {
        startButton.setOnAction(e -> {
            network.startSilos();
            network.startInputStreams();
            pauseButton.setText("Pause");
            isPaused = false;
        });
    }

    private void configurePauseButton(Button pauseButton) {
        pauseButton.setOnAction(event -> {
            if (!isPaused) {
                network.pauseSilos();
                pauseButton.setText("Step");
                isPaused = true;
            } else {
                network.stepSilos();
            }
        });
    }

    private void configureStopButton(Button stopButton) {
        stopButton.setOnAction(event -> {
            reset();
        });
    }

    /**
     * The styleButton method applies a consistent style to the buttons.
     */

    private void styleButton(Button button) {
        button.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));
        button.setStyle("-fx-background-color: black; -fx-text-fill: white; ;-fx-border-color: white; -fx-border-width: 2px; -fx-min-width: 50; -fx-min-height: 50;");

        // Add hover effect
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #505050; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-min-width: 50; -fx-min-height: 50;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-min-width: 50; -fx-min-height: 50;"));

        // Add click effect
        button.setOnMousePressed(e -> button.setStyle("-fx-background-color: #303030; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-min-width: 50; -fx-min-height: 50;"));
        button.setOnMouseReleased(e -> button.setStyle("-fx-background-color: #505050; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-min-width: 50; -fx-min-height: 50;"));
    }


    /**
     * The createGridPane method initializes the GridPane with a black background color.
     */

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: black;");
        return gridPane;
    }

    /**
     * The reset method stops the simulation and resets the layout, allowing
     * the user to restart the simulation with the same input file.
     */

    public void reset() {
        network.stopSilos();
        network.stopStreams();

        parseInputFile();

        GridPane gridPane = createGridPane();
        HBox streamsBox = populateStreams(gridPane);
        VBox sideDisplay = createSideDisplay(streamsBox, buttonBox);
        populateGridPaneWithSilos(gridPane);
        root.getChildren().clear();
        root.getChildren().addAll(sideDisplay, gridPane);
    }
}
