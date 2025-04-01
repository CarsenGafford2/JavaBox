package com.example.rpg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene primaryScene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryScene = new Scene(loadFXML("primary"), 490, 490);
        primaryStage.setScene(primaryScene);
        primaryStage.requestFocus();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Game");
        primaryStage.getIcons().add(new Image(getClass().getResource("res/npc/guy0.png").toString()));
        primaryStage.show();
    }

    static void setRoot(String fxml) throws IOException {
        primaryScene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}