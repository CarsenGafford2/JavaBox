package com.example.rpg;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class console extends AnchorPane {

    private static console instance;

    public console() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("console.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this); // Explicitly set the controller
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        instance = this;
    }

    @FXML
    private TextFlow textFlow;

    @FXML
    public void initialize() {
        log("Console initialized.");
    }

    public void log(String message) {
        Platform.runLater(() -> {
            textFlow.getChildren().add(new Text(message + "\n"));
        });
    }

    public static console getInstance() {
        return instance;
    }
}
