package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import network.Stream;

/**
 * Luke McDougall, Jack Vanlyssel, Spoorthi Menta
 *
 * This class is used to create a visual representation of a Stream object.
 * The streams are used to show silos either receiving input or sending output
 * to other silos. The stream is just a number, accompanied by an arrow graphic
 * that is generated in siloGraphic.
 */

public class StreamGraphic extends VBox {

    private final Label streamLabel;
    // A Label component used for displaying the stream's title.
    private final Label streamHeaderLabel;
    // A Label component used as a header for the streamTextArea.
    private final TextArea streamTextArea;
    // A TextArea component that displays the values of the stream.
    private final Stream stream;
    // A reference to the associated Stream object.

    /**
     * Represents a visual representation of a Stream object, which can be either an input or output stream
     */
    public StreamGraphic(Stream stream) {
        this.stream = stream;
        streamLabel = new Label("");
        streamHeaderLabel = new Label("");
        streamTextArea = new TextArea();
        streamTextArea.setEditable(false);
        streamTextArea.setWrapText(true);
        streamTextArea.setPrefHeight(500);
        streamTextArea.setPrefWidth(50);

        streamTextArea.setStyle("-fx-control-inner-background: black; -fx-text-fill: white; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-border-color: white;");
        streamLabel.setStyle("-fx-text-fill: white;");
        streamLabel.setAlignment(Pos.CENTER);
        streamHeaderLabel.setStyle("-fx-text-fill: white;");
        streamTextArea.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));
        streamLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));
        streamHeaderLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));

        getChildren().addAll(streamHeaderLabel, streamTextArea);
    }

    /**
     * This method sets the text of the streamLabel and streamHeaderLabel
     */
    public void setStreamLabel(String streamLabel) {
        this.streamLabel.setText(streamLabel);
        streamHeaderLabel.setText(streamLabel);
    }

    /**
     * The getStreamLabel method returns the streamLabel object
     */
    public Label getStreamLabel() {
        return streamLabel;
    }

    /**
     * Updates the text in the streamTextArea based on the values in the associated Stream object
     */
    public void updateGraphic() {
        StringBuilder streamText = new StringBuilder();
        for (int i = 0; i < stream.getValues().size(); i++) {
            streamText.append(stream.getValues().get(i)).append("\n");
        }
        streamTextArea.setText(streamText.toString());
    }
}
