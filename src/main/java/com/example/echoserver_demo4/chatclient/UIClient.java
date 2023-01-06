package com.example.echoserver_demo4.chatclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UIClient extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UIClient.class.getResource("/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 350);
        stage.setTitle("Client_chat");
        stage.setScene(scene);
        stage.show();

        Controller controller = fxmlLoader.getController();
    }

    public static void main(String[] args) {
        launch();
    }
}