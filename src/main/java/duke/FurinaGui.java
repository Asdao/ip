package duke;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/** JavaFX interface that lets users interact with Furina through typed commands. */
public class FurinaGui extends Application {
    private final CommandHandler handler = new CommandHandler();

    private TextArea conversation;

    private TextField commandInput;

    /** Builds and displays the chatbot window. */
    @Override
    public void start(Stage stage) {
        conversation = new TextArea();
        conversation.setEditable(false);
        conversation.setWrapText(true);
        conversation.setText("Hello! I'm Furina.\nWhat can I do for you?\n");
        commandInput = new TextField();
        commandInput.setPromptText("Enter a command, e.g. todo read book");
        Button sendButton = new Button("Send");
        sendButton.setDefaultButton(true);
        sendButton.setOnAction(event -> sendCommand());
        HBox inputRow = new HBox(8, new Label("Command:"), commandInput, sendButton);
        inputRow.setPadding(new Insets(10));
        HBox.setHgrow(commandInput, javafx.scene.layout.Priority.ALWAYS);
        BorderPane root = new BorderPane(conversation, null, null, inputRow, null);
        root.setPadding(new Insets(10));
        stage.setTitle("Furina");
        stage.setScene(new Scene(root, 600, 450));
        stage.show();
        commandInput.requestFocus();
    }

    private void sendCommand() {
        String command = commandInput.getText().trim();
        if (command.isBlank()) {
            return;
        }
        String response = handler.handleCommand(command).replace("\n", "\n        ");
        conversation.appendText("\nYou: " + command + "\nFurina: " + response + "\n");
        commandInput.clear();
        if (command.equals("bye")) {
            commandInput.setDisable(true);
        }
    }
}
