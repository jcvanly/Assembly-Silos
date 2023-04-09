package JavaFx;

import javafx.beans.binding.Bindings;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import java.util.concurrent.Callable;

/**
 * A visual representation of a Silo
 */
public class Silo extends Pane {
    private Rectangle outerRectangle;
    private Rectangle[] innerSquares;
    private Label[] variableLabels;
    private TextArea codeArea;

    /**
     * Creates a Silo object
     * @param parentPane the pane to add the Silo to
     */
    public Silo(Pane parentPane) {
        createOuterRectangle(parentPane);
        createInnerSquaresAndLabels();
        createCodeArea();
    }

    /**
     * Sets the text of the ACC variable
     * @param value the value to set the ACC variable to
     */
    public void setAccVariable(int value) {
        variableLabels[0].setText(""+value);
    }

    /**
     * Sets the text of the BAK variable
     * @param value the value to set the BAK variable to
     */
    public void setBakVariable(int value) {
        variableLabels[1].setText("<" + value + ">");
    }

    /**
     * Sets the text of the LAST variable
     * @param value the value to set the LAST variable to
     */
    public void setLastVariable(String value) {
        variableLabels[2].setText(value);
    }

    /**
     * Sets the text of the MODE variable
     * @param value the value to set the MODE variable to
     */
    public void setModeVariable(String value) {
        variableLabels[3].setText(value);
    }

    /**
     * Creates the main rectangle of the Silo
     * @param parentPane the pane to add the Silo to
     */
    private void createOuterRectangle(Pane parentPane) {
        outerRectangle = new Rectangle();
        outerRectangle.setFill(Color.BLACK);
        outerRectangle.setStroke(Color.WHITE);
        outerRectangle.setStrokeWidth(5);

        outerRectangle.widthProperty().bind(Bindings.min(parentPane.widthProperty(), parentPane.heightProperty()));
        outerRectangle.heightProperty().bind(Bindings.min(parentPane.widthProperty(), parentPane.heightProperty()));
        getChildren().add(outerRectangle);

        layoutXProperty().bind(parentPane.widthProperty().subtract(outerRectangle.widthProperty()).divide(2));
        layoutYProperty().bind(parentPane.heightProperty().subtract(outerRectangle.heightProperty()).divide(2));
    }

    /**
     * Creates the inner squares and labels of the Silo
     */
    private void createInnerSquaresAndLabels() {
        innerSquares = new Rectangle[4];
        Label[] innerSquareLabels = new Label[4];
        variableLabels = new Label[4];
        String[] labelTexts = {"ACC", "BAK", "LAST", "MODE"};
        String[] defaultVariableTexts = {"0", "<0>", "N/A", "IDLE"};

        for (int i = 0; i < innerSquares.length; i++) {
            innerSquares[i] = new Rectangle();
            innerSquares[i].setFill(Color.BLACK);
            innerSquares[i].setStroke(Color.WHITE);
            innerSquares[i].setStrokeWidth(3);

            innerSquares[i].widthProperty().bind(outerRectangle.widthProperty().divide(4));
            innerSquares[i].heightProperty().bind(outerRectangle.heightProperty().divide(4));

            innerSquares[i].layoutXProperty().bind(outerRectangle.widthProperty().subtract(innerSquares[i].widthProperty()));
            innerSquares[i].layoutYProperty().bind(outerRectangle.heightProperty().multiply(i).divide(4));

            getChildren().add(innerSquares[i]);

            innerSquareLabels[i] = new Label(labelTexts[i]);
            innerSquareLabels[i].setTextFill(Color.web("#888888"));

            innerSquareLabels[i].layoutXProperty().bind(
                    innerSquares[i].layoutXProperty().add((innerSquares[i].widthProperty().subtract(innerSquareLabels[i].widthProperty())).divide(2)));
            innerSquareLabels[i].layoutYProperty().bind(
                    innerSquares[i].layoutYProperty().add((innerSquares[i].heightProperty().subtract(innerSquareLabels[i].heightProperty())).divide(2)));

            final int index = i;
            innerSquareLabels[i].fontProperty().bind(Bindings.createObjectBinding(new Callable<>() {
                @Override
                public Font call() {
                    return Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"),
                            innerSquares[index].widthProperty().divide(4).doubleValue());
                }
            }, innerSquares[i].widthProperty()));

            getChildren().add(innerSquareLabels[i]);

            variableLabels[i] = new Label(defaultVariableTexts[i]);
            variableLabels[i].setTextFill(Color.WHITE);

            variableLabels[i].layoutXProperty().bind(innerSquares[i].layoutXProperty().add((innerSquares[i].widthProperty().subtract(variableLabels[i].widthProperty())).divide(2)));
            variableLabels[i].layoutYProperty().bind(innerSquareLabels[i].layoutYProperty().add(innerSquareLabels[i].heightProperty()));

            variableLabels[i].fontProperty().bind(Bindings.createObjectBinding(new Callable<>() {
                @Override
                public Font call() {
                    return Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"),
                            innerSquares[index].widthProperty().divide(4).doubleValue());
                }
            }, innerSquares[i].widthProperty()));
            getChildren().add(variableLabels[i]);
        }
    }

    /**
     * Creates the code area of the Silo
     */
    private void createCodeArea() {
        codeArea = new TextArea();
        codeArea.setStyle("-fx-control-inner-background: black; -fx-text-fill: white; -fx-focus-color: transparent;");
        codeArea.setWrapText(true);
        codeArea.prefWidthProperty().bind(outerRectangle.widthProperty().subtract(innerSquares[0].widthProperty()));
        codeArea.prefHeightProperty().bind(outerRectangle.heightProperty());
        codeArea.setLayoutX(0);
        codeArea.setLayoutY(0);

        codeArea.fontProperty().bind(variableLabels[0].fontProperty());

        getChildren().add(codeArea);

        for (Rectangle innerSquare : innerSquares) {
            codeArea.layoutXProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.doubleValue() < innerSquare.getWidth() + 10) {
                    codeArea.setLayoutX(innerSquare.getWidth() + 10);
                }
            });
            codeArea.layoutYProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.doubleValue() < innerSquare.getHeight() + 10) {
                    codeArea.setLayoutY(innerSquare.getHeight() + 10);
                }
            });
        }
    }
}