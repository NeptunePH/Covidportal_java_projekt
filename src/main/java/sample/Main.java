package main.java.sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        logger.info("Pokrenuta aplikacija.");
        Parent root =
                FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        primaryStage.setTitle("Pretraga");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
        mainStage = primaryStage;
    }

    public static Stage getMainStage(){
        return mainStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}