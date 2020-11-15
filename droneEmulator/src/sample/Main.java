package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.SocketException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Drone Emulator");
        primaryStage.setScene(new Scene(root, 950, 600));
        primaryStage.setResizable(false);
        primaryStage.show();

        Udp thread = new Udp();
        thread.start();
    }

    public static void main(String[] args) throws SocketException {
        launch(args);
    }
}
