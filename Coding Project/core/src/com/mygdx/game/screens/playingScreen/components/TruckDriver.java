package com.mygdx.game.screens.playingScreen.components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.Iterator;

public class TruckDriver {
    public int fuel;
    int added_fuel = 0;
    public int truckX = 0; // x-coordinate on the map segment
    public int truckY = 321; // y-coordinate on the map segment
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
        // Switch statement to handle key presses
        switch(keycode) {
            case Input.Keys.W:
            case Input.Keys.UP:
                // Move the truck upwards & decrease fuel as we move
                boolean collidesUp = collisionDetection(truckX, truckY + 32);
                if (!collidesUp) {
                    truckY += 32;
                    fuel -= 1;
                    System.out.println("Truck moved up 32");
                }
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                // Move the truck downwards & decrease fuel as we move
                boolean collidesDown = collisionDetection(truckX, truckY - 32);
                if (!collidesDown) {
                    truckY -= 32;
                    fuel -= 1;
                    System.out.println("Truck moved down 32");
                }
                break;
            case Input.Keys.A:
            case Input.Keys.LEFT:
                // Move the truck left & decrease fuel as we move
                boolean collidesLeft = collisionDetection(truckX - 32, truckY);
                if (!collidesLeft) {
                    truckX -= 32;
                    fuel -= 1;
                    System.out.println("Truck moved left 32");
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                // Move the truck right & decrease fuel as we move
                boolean collidesRight = collisionDetection(truckX + 32, truckY);
                if (!collidesRight) {
                    truckX += 32;
                    fuel -= 1;
                    System.out.println("Truck moved right 32");
                }
                break;
            default:
                break;
        }

        return false;
    }




//    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
}
