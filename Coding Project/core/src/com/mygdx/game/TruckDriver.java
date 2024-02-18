package com.mygdx.game;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

public class TruckDriver {
    int fuel = 20;
    int added_fuel = 0;
    int truckX = 50;
    int truckY = 100;

    KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            // Check if "W" key is pressed
            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyChar() == 'W') {
                // Move the truck upwards on W & decrease fuel as we move
                truckX = 100;
                truckY = 150;
                fuel = fuel--;
                System.out.println("Truck moved to (200, 200)");
            }

            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyChar() == 'D') {
                // Move the truck straight on D
                truckX = 100;
                truckY = 100;
                fuel = fuel--;
                System.out.println("Truck moved to (200, 200)");
            }

            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyChar() == 'S') {
                // Move the truck to Downwards with S
                truckX = 100;
                truckY = 50;
                fuel = fuel--;
                System.out.println("Truck moved to (200, 200)");
            }
            return false;
        }
    };
//    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
}
