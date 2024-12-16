package com.example.rpg;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class PrimaryController {
    @FXML
    private GridPane gridPane;

    private int[][] map;
    private int x = 0;
    private int y = 2;

    public void initialize() {
        map = new int[][] {
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 1, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        String grass = getClass().getResource("res/grass.png").toString();
        String rock = getClass().getResource("res/rock.png").toString();

        Image grassImg = new Image(grass);
        Image rockImg = new Image(rock);

        gridPane.setFocusTraversable(true);
        gridPane.requestFocus();

        gridPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                y = Math.max(0, y - 1);
            } else if (event.getCode() == KeyCode.DOWN) {
                y = Math.min(map.length - 1, y + 1);
            } else if (event.getCode() == KeyCode.LEFT) {
                x = Math.max(0, x - 1);
            } else if (event.getCode() == KeyCode.RIGHT) {
                x = Math.min(map[0].length - 1, x + 1);
            }

            renderMap(grassImg, rockImg);
        });

        renderMap(grassImg, rockImg);
    }

    private void renderMap(Image grassImg, Image rockImg) {
        gridPane.getChildren().clear();

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                int mapRow = y + row;
                int mapCol = x + col;

                if (mapRow >= map.length || mapCol >= map[0].length) continue;

                Image image = (map[mapRow][mapCol] == 0) ? grassImg : rockImg;

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);

                gridPane.add(imageView, col, row);
            }
        }
    }
}