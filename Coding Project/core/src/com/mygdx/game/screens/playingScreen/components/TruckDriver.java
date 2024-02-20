package com.mygdx.game.screens.playingScreen.components;

import com.badlogic.gdx.Input;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;

public class TruckDriver {
    int fuel = 20;
    int added_fuel = 0;
    public int truckX = 50; // x-coordinate on the map segment
    public int truckY = 100; // y-coordinate on the map segment
    public int truckRow; // row in the mapGrid
    public int truckCol; // column in the mapGrid
    public boolean[][] mapGrid; // true: truck can go there, false: truck cannot go there
    // (0, 0) on the mapGrid is the bottom left segment of the map
    public int rows; // num rows in the mapGrid
    public int cols;

    public TruckDriver(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        mapGrid = new boolean[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                mapGrid[r][c] = false;
            }
        }
        truckRow = 2;
        truckCol = 0;
        mapGrid[truckRow][truckCol] = true; // Starting position

    }

    public boolean dispatchKeyEvent(int keycode) {
        // Check if "W" key is pressed
        if (keycode == Input.Keys.W) {
            // Move the truck upwards on W & decrease fuel as we move
            truckX = 100;
            truckY = 150;
            fuel = fuel--;
            System.out.println("Truck moved to (200, 200)");
        }

        if (keycode == Input.Keys.D) {
            // Move the truck straight on D
            truckX = 100;
            truckY = 100;
            fuel = fuel--;
            System.out.println("Truck moved to (200, 200)");
        }

        if (keycode == Input.Keys.S) {
            // Move the truck to Downwards with S
            truckX = 100;
            truckY = 50;
            fuel = fuel--;
            System.out.println("Truck moved to (200, 200)");
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
