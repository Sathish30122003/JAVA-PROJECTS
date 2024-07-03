module com.example.chatting {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.chatting to javafx.fxml;
    exports com.example.chatting;
}