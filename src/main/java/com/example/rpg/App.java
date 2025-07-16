package com.example.rpg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class for JavaBox.
 * Initializes and launches the JavaFX application.
 * 
 * @author Carsen Gafford
 * @version alpha v0.1.2
 */
public class App extends Application {

    private static Scene primaryScene;

    /**
     * Start the JavaFX application.
     * @param primaryStage the primary stage for this application
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryScene = new Scene(loadFXML("primary"), 500, 500);
        primaryStage.setScene(primaryScene);
        primaryStage.requestFocus();
        primaryStage.setResizable(true);
        primaryStage.setTitle("JavaBox");
        primaryStage.getIcons().add(new Image(getClass().getResource("res/npc/guy0.png").toString()));
        primaryStage.show();
    }

    /**
     * Set the root of the primary scene to a new FXML layout.
     * @param fxml the name of the FXML file (without extension)
     * @throws IOException if the FXML file cannot be loaded
     */
    static void setRoot(String fxml) throws IOException {
        primaryScene.setRoot(loadFXML(fxml));
    }

    /**
     * Load an FXML file and return its root node.
     * @param fxml the name of the FXML file (without extension)
     * @return the root node of the loaded FXML
     * @throws IOException if the FXML file cannot be loaded
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Main entry point for the application.
     * @param args command-line arguments (not used at the moment)
     */
    public static void main(String[] args) {
        launch();
    }

}