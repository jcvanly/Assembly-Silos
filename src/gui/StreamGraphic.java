package gui;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import network.Stream;

public class StreamGraphic extends VBox {

    private final Label streamLabel;
    private final TextArea streamTextArea;
    private final Stream stream;

    public StreamGraphic(Stream stream) {
        this.stream = stream;
        streamLabel = new Label("");
        streamTextArea = new TextArea();
        streamTextArea.setEditable(false);
        streamTextArea.setWrapText(true);
        streamTextArea.setPrefHeight(500);
        streamTextArea.setPrefWidth(50);

        streamTextArea.setStyle("-fx-control-inner-background: black; -fx-text-fill: white; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-border-color: white;");
        streamLabel.setStyle("-fx-text-fill: white;");
        streamTextArea.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));
        streamLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Silo_Font.TTF"), 16));

        getChildren().addAll(streamLabel, streamTextArea);
    }

    public void setStreamLabel(String streamLabel) {
        this.streamLabel.setText(streamLabel);
    }

    public void updateGraphic() {
        StringBuilder streamText = new StringBuilder();
        for (int i = 0; i < stream.getValues().size(); i++) {
            streamText.append(stream.getValues().get(i)).append("\n");
        }
        streamTextArea.setText(streamText.toString());
    }
}
