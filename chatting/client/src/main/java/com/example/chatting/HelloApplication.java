package com.example.chatting;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class HelloApplication extends Application {

    private PrintWriter cout;
    private BufferedReader cin;
    private ListView<Message> chatListView; // Declare chatListView here
    private ObservableList<Message> messages = FXCollections.observableArrayList();
    private TextField inputField;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        chatListView = new ListView<>();
        chatListView.setItems(messages);
        chatListView.setCellFactory(listView -> new MessageCell());

        inputField = new TextField();
        inputField.setAlignment(Pos.TOP_RIGHT); // Align text to the right
        inputField.setEditable(true); // Allow editing
        root.getChildren().addAll(chatListView, inputField);

        try {
            Socket socket = new Socket("localhost", 5000);
            cout = new PrintWriter(socket.getOutputStream(), true);
            cin = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ThreadClient threadClient = new ThreadClient();
            new Thread(threadClient).start(); // start thread to receive message

            String name = "empty"; // Define name variable here
            AtomicReference<String> finalName = new AtomicReference<>(name); // Make name effectively final
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Join Chat");
            dialog.setHeaderText("Please enter your name to join the chat:");
            dialog.setContentText("Name:");

            dialog.showAndWait().ifPresent(finalName::set);
            name = finalName.get();
            cout.println(name + ": has joined chat-room.");

            String finalName1 = name;
            inputField.setOnAction(event -> {
                String message = finalName1 + " : " + inputField.getText();
                messages.add(new Message("you", inputField.getText()));

                cout.println(message);
                inputField.clear();
            });
            primaryStage.setOnCloseRequest(event -> {
                cout.println("logout");
                Platform.exit();
            });

            primaryStage.setTitle("Chat Client");
            primaryStage.setScene(new Scene(root, 400, 600)); // Increased height for chat area
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    static class CustomListCell extends ListCell<String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setBackground(null);
            } else {
                setText(item);

                // Change the background color of the cell if it meets certain criteria
                if (item.contains("you")) {
                    setBackground(new Background(new BackgroundFill(Color.VIOLET, CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        }
    }

    /**
     * Thread for clients
     */
    class ThreadClient implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    String message = cin.readLine();
                    if (message != null) {
                        Platform.runLater(() -> {
                            // Append received message to chat area
                            // Make sure to handle UI updates on the JavaFX Application Thread
                            String[] parts = message.split(":");
                            String sender = parts[0].trim();
                            String content = parts[1].trim();
                            messages.add(new Message(sender, content));
                        });
                    }
                }
            } catch (SocketException e) {
                Platform.runLater(() -> {
                    messages.add(new Message("System", "You left the chat-room"));
                });
            } catch (IOException exception) {
                Platform.runLater(() -> {
                    messages.add(new Message("System", exception.toString()));
                });
            } finally {
                try {
                    cin.close();
                } catch (IOException exception) {
                    Platform.runLater(() -> {
                        messages.add(new Message("System", exception.toString()));
                    });
                }
            }
        }
    }

    /**
     * Message class to represent chat messages
     */
    static class Message {
        private String sender;
        private String content;

        public Message(String sender, String content) {
            this.sender = sender;
            this.content = content;
        }

        public String getSender() {
            return sender;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return sender + ": " + content;
        }
    }

    /**
     * Custom ListCell to display chat messages
     */
    class MessageCell extends ListCell<Message> {
        private Label senderLabel = new Label();
        private Label messageLabel = new Label();
        private HBox content = new HBox();

        public MessageCell() {
            senderLabel.setStyle("-fx-font-weight: bold;");
            messageLabel.setStyle("-fx-font-weight: bold;");
            System.out.println(senderLabel.getText());
            senderLabel.setTextFill(Color.WHITE); // Set text fill color to violet
            messageLabel.setTextFill(Color.WHITE);
            content.getChildren().addAll(senderLabel, messageLabel);

            content.setSpacing(10);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        @Override
        protected void updateItem(Message item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
                setText(null);
            } else {
                senderLabel.setText(item.getSender() + ":");
                messageLabel.setText(item.getContent());
                if (item.getSender().equals("you")) {

                    content.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    content.setBackground(new Background(new BackgroundFill(Color.DARKGREY, CornerRadii.EMPTY, Insets.EMPTY)));
                }
                setGraphic(content);
            }
        }
    }
}
