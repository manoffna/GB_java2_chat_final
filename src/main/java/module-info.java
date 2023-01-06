module com.example.echoserver_demo4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.echoserver_demo4 to javafx.fxml;
    exports com.example.echoserver_demo4;
    exports com.example.echoserver_demo4.chatserver;
    opens com.example.echoserver_demo4.chatserver to javafx.fxml;
    exports com.example.echoserver_demo4.chatclient;
    opens com.example.echoserver_demo4.chatclient to javafx.fxml;
}