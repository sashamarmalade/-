package Client.sample;

import Client.sample.Method.Method;
import DataTransfer.LoginData;
import DataTransfer.UserMsg;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;


public class Main extends Application {

    public static Method Logic = new Method();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Visual/Login.fxml"));
        primaryStage.setTitle("PollTest");
        primaryStage.resizableProperty().set(false);
        primaryStage.setOnCloseRequest(we -> {

            Logic.getServer().OutData(new UserMsg("Exit"));
            System.exit(0);
        });
        primaryStage.setScene(new Scene(root,400,400));
        primaryStage.show();



    }

    public static void main(String[] args) {
        launch(args);
    }
}
