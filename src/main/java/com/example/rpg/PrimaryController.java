package com.example.rpg;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyCode;

public class PrimaryController {
    @FXML
    private GridPane gridPane;

    private int[][] map;
    private int x = 0;
    private int y = 0;

    private String grass = getClass().getResource("res/grass.png").toString();
    private String rock = getClass().getResource("res/rock.png").toString();
    private String guy = getClass().getResource("res/guy.png").toString();

    private Image grassImg = new Image(grass);
    private Image guyImg = new Image(guy);
    private Image rockImg = new Image(rock);


    public void initialize() {
        map = new int[][] {
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0},
                {0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0}
        };
    
        gridPane.setFocusTraversable(true);
        gridPane.requestFocus();

        Timer t = new Timer();
        Npc john = new Npc(3, 3, "John", map[0].length, map.length);
        drawNpc(john);
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                map[john.getxPos()][john.getyPos()] = 0;
                john.move();
                drawNpc(john);
            }
        }, 0, 1000);
    
        gridPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.UP) {
                        y = Math.max(0, y - 1);
                    } else if (event.getCode() == KeyCode.DOWN) {
                        y = Math.min(map.length - 1, y + 1);
                    } else if (event.getCode() == KeyCode.LEFT) {
                        x = Math.max(0, x - 1);
                    } else if (event.getCode() == KeyCode.RIGHT) {
                        x = Math.min(map[0].length - 1, x + 1);
                    }
                    renderMap();
                });
            }
        });
    
        renderMap();
    }
    

    private void drawNpc(Npc guy) {
        map[guy.getxPos()][guy.getyPos()] = 2;
        renderMap();
    }
    
    private void renderMap() {
        // Ensure everything is done on the JavaFX Application Thread
        Platform.runLater(() -> {
            gridPane.getChildren().clear();  // Always clear before updating
    
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    int mapRow = y + row;
                    int mapCol = x + col;
    
                    if (mapRow >= map.length || mapCol >= map[0].length) {
                        continue;
                    }
    
                    Image image;
                    if (map[mapRow][mapCol] == 0) {
                        image = grassImg;
                    } else if (map[mapRow][mapCol] == 1) {
                        image = rockImg;
                    } else if (map[mapRow][mapCol] == 2) {
                        image = guyImg;
                    } else {
                        image = grassImg;
                    }
    
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);
                    imageView.setPreserveRatio(true);
    
                    gridPane.add(imageView, col, row);
                }
            }
        });
    }
}    