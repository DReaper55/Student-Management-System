package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import sample.scenes.HomeScene;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        new HomeScene().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
