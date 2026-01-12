package com.example.rpg;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * Handles creation and layout of tile selection buttons.
 * @author Carsen Gafford
 * @version alpha v0.2.0
 */
public final class TileButtonController {

    private TileButtonController() {}

    /**
     * Builds tile buttons and attaches them to the provided AnchorPane.
     *
     * @param tilePane AnchorPane to hold buttons
     * @param textureManager Texture source
     * @param inputHandler Input handler to update selected tile
     */
    public static void setup(
            AnchorPane tilePane,
            TextureManager textureManager,
            InputHandler inputHandler
    ) {
        int buttonSize = 50;
        int padding = 0;
        int columns = 4;

        tilePane.getChildren().clear();

        for (int i = 0; i < textureManager.imageArray.length; i++) {

            ImageView imageView = new ImageView(textureManager.imageArray[i]);
            imageView.setFitWidth(buttonSize);
            imageView.setFitHeight(buttonSize);
            imageView.setPreserveRatio(true);

            Button btn = new Button();
            btn.setGraphic(imageView);
            btn.setPrefSize(buttonSize, buttonSize);
            btn.setMinSize(buttonSize, buttonSize);
            btn.setMaxSize(buttonSize, buttonSize);

            int row = i / columns;
            int col = i % columns;

            btn.setLayoutX(col * (buttonSize + padding));
            btn.setLayoutY(row * (buttonSize + padding));

            final int tileIndex = i + 2;

            btn.setOnAction(e ->
                inputHandler.setNumberKeypressed(tileIndex)
            );

            tilePane.getChildren().add(btn);
        }
    }
}
