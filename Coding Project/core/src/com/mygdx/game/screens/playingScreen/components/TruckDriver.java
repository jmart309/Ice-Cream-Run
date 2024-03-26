package com.mygdx.game.screens.playingScreen.components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.Iterator;

public class TruckDriver {
    public int fuel;
    int added_fuel = 0;
    public int truckX = 0; // x-coordinate on the map segment
    public int truckY = 0; // y-coordinate on the map segment
    public int truckRow; // row in the mapGrid
    public int truckCol; // column in the mapGrid
    public boolean[][] mapGrid; // true: truck can go there, false: truck cannot go there
    // (0, 0) on the mapGrid is the bottom left segment of the map
    public int rows; // num rows in the mapGrid
    public int cols;

    float truckHeight;
    float truckWidth;

    TiledMapTileLayer collisionLayer;

    public TruckDriver(int rows, int cols, TiledMapTileLayer collisionLayer, float truckHeight, float truckWidth) {
        this.rows = rows;
        this.cols = cols;
        this.truckHeight = truckHeight;
        this.truckWidth = truckWidth;
        this.collisionLayer = collisionLayer;
        mapGrid = new boolean[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                mapGrid[r][c] = false;
            }
        }
        truckRow = 2;
        truckCol = 0;
        mapGrid[truckRow][truckCol] = true; // Starting position
        this.fuel =  100;

    }


    private boolean collisionDetection(int truckX, int truckY){
        float tileWidth = collisionLayer.getTileWidth();
        float tileHeight = collisionLayer.getTileHeight();
        boolean collisionX = false;
        //boolean collisionY = false;
        System.out.println((int) (truckX / tileWidth) +  ", " +  (int) (truckY / tileHeight));
        Iterator<String> keysIterator = collisionLayer.getCell(4, 16).getTile().getProperties().getKeys();

        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            System.out.println(key);
        }

        collisionX = collisionLayer.getCell((int) (truckX / tileWidth), (int) ((truckY + truckHeight) / tileHeight))
                .getTile().getProperties().containsKey("blocked");

        if(collisionX == false){
            collisionX = collisionLayer.getCell((int) (truckX / tileWidth), (int) ((truckY) / tileHeight))
                    .getTile().getProperties().containsKey("blocked");
        }

        if(collisionX == false){
            collisionX = collisionLayer.getCell((int) ((truckX + truckWidth) / tileWidth), (int) ((truckY + truckHeight) / tileHeight))
                    .getTile().getProperties().containsKey("blocked");
        }

        if(collisionX == false){
            collisionX = collisionLayer.getCell((int) ((truckX + truckWidth) / tileWidth), (int) ((truckY) / tileHeight))
                    .getTile().getProperties().containsKey("blocked");
        }
        return collisionX;


    }

    public boolean dispatchKeyEvent(int keycode) {
        // Check if "W" key is pressed
        if (keycode == Input.Keys.W) {
            // Move the truck upwards on W & decrease fuel as we move
            boolean collides = collisionDetection(truckX , truckY + 32);
            if(collides == false){
                truckY += 32;
                fuel -= 1;
            }
            System.out.println("Truck moved up 30");
        }

        if (keycode == Input.Keys.D) {
            // Move the truck straight on D
            boolean collides = collisionDetection(truckX + 32 , truckY);
            if (collides == false) {
                truckX += 32;
                fuel -= 1;
            }
            System.out.println("Truck moved right 30");
        }

        if (keycode == Input.Keys.S) {
            // Move the truck to Downwards with S
            boolean collides = collisionDetection(truckX , truckY - 32);
            if (collides == false) {
                truckY -= 32;
                fuel -= 1;
            }
            System.out.println("Truck moved down 30");
        }

        if (keycode == Input.Keys.A){
            boolean collides = collisionDetection(truckX - 32, truckY);
            if (collides == false) {
                truckX -= 32;
                fuel -= 1;
            }
            System.out.println("Truck moves left 30");
        }
        return true;
    }

    public boolean changeMapSegment(int keycode) {
        if (keycode == Input.Keys.UP) {
            System.out.println("UP");
            if (truckRow < 3) truckRow++;
            else return false;
        }
        if (keycode == Input.Keys.DOWN) {
            System.out.println("DOWN");
            if (truckRow > 0) truckRow--;
            else return false;
        }
        if (keycode == Input.Keys.LEFT) {
            System.out.println("LEFT");
            if (truckCol > 0) truckCol--;
            else return false;
        }
        if (keycode == Input.Keys.RIGHT) {
            System.out.println("RIGHT");
            if (truckCol < 3) truckCol++;
            else return false;
        }
        return true;
    }



//    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
}
