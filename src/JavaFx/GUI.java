package JavaFx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Objects;

public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        double windowWidth = 800;
        double windowHeight = 600;

        // Create a white rectangle covering 1/4 of the pane
        Rectangle whiteRect = new Rectangle(0, 0, windowWidth / 4, windowHeight);
        whiteRect.setFill(Color.WHITE);

        // Create a black rectangle covering 3/4 of the pane
        Rectangle blackRect = new Rectangle(windowWidth / 4, 0, (windowWidth * 3) / 4, windowHeight);
        blackRect.setFill(Color.BLACK);

        // Create an HBox for the buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setPrefWidth(windowWidth / 4);
        buttonBox.setAlignment(Pos.CENTER);

        // Create four buttons and add them to the HBox
        Button button1 = new Button("Button 1");
        Button button2 = new Button("Button 2");
        Button button3 = new Button("Button 3");
        Button button4 = new Button("Button 4");
        button1.setPrefHeight(40);
        button2.setPrefHeight(40);
        button3.setPrefHeight(40);
        button4.setPrefHeight(40);
        button1.setTranslateY(550);
        button2.setTranslateY(550);
        button3.setTranslateY(550);
        button4.setTranslateY(550);

        buttonBox.getChildren().addAll(button1, button2, button3, button4);

        // Create a GridPane for the grid of cubes
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Set grid size
        int gridSizeX = 4;
        int gridSizeY = 4;

        // Calculate the maximum square size to fit the black background
        double maxSquareSizeX = ((windowWidth * 3) / 4 - (gridSizeX - 1) * 10) / gridSizeX;
        double maxSquareSizeY = (windowHeight - (gridSizeY - 1) * 10) / gridSizeY;
        double maxSquareSize = Math.min(maxSquareSizeX, maxSquareSizeY);

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Silo.png")));


        for (int i = 0; i < gridSizeY; i++) {
            for (int j = 0; j < gridSizeX; j++) {
                // Create a VBox for the labels
                VBox labelBox = new VBox();
                labelBox.setAlignment(Pos.BASELINE_RIGHT); // Set alignment to right side
                labelBox.setSpacing(5); // Set spacing between labels

                // Create four labels and add them to the VBox
                Label label1 = new Label("ACC");
                Label label2 = new Label("BAK");
                Label label3 = new Label("LAST");
                Label label4 = new Label("MODE");
                label1.setTextFill(Color.WHITE);
                label2.setTextFill(Color.WHITE);
                label3.setTextFill(Color.WHITE);
                label4.setTextFill(Color.WHITE);
                labelBox.getChildren().addAll(label1, label2, label3, label4);

                // Create gray square with the VBox and ImageView as its children
                StackPane squareWithLabels = new StackPane();

                // Create ImageView and set its properties
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(maxSquareSize);
                imageView.setFitHeight(maxSquareSize);

                squareWithLabels.getChildren().addAll(new Rectangle(maxSquareSize, maxSquareSize, Color.GRAY), imageView, labelBox);
                gridPane.add(squareWithLabels, j, i);
            }
        }

        // Calculate the position of the gridPane to center it on the black background
        double gridPaneWidth = (gridSizeX * maxSquareSize) + ((gridSizeX - 1) * 10);
        double gridPaneHeight = (gridSizeY * maxSquareSize) + ((gridSizeY - 1) * 10);
        double gridPanePosX = (windowWidth / 4) + (((windowWidth * 3) / 4) - gridPaneWidth) / 2;
        double gridPanePosY = (windowHeight - gridPaneHeight) / 2;
        gridPane.setLayoutX(gridPanePosX);
        gridPane.setLayoutY(gridPanePosY);
        pane.getChildren().addAll(whiteRect, blackRect, buttonBox, gridPane);

        Scene scene = new Scene(pane, windowWidth, windowHeight);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Color Split Pane with Buttons and Scaled Cubes");
        primaryStage.show();

        // Get the StackPane at row 1, column 2
        StackPane squareToUpdate = (StackPane) gridPane.getChildren().get(5);
        // Get the VBox that contains the label
        VBox labelBoxToUpdate = (VBox) squareToUpdate.getChildren().get(2);
        // Get the label itself
        Label labelToUpdate = (Label) labelBoxToUpdate.getChildren().get(1);
        // Update the text of the label
        labelToUpdate.setText("UPDATED LABEL");

    }

    public static void main(String[] args) {
        launch(args);
    }
}