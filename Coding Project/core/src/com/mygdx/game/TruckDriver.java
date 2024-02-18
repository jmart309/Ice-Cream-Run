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
                // Move the truck to (200, 200)
                truckX = 100;
                truckY = 150;
                System.out.println("Truck moved to (200, 200)");
            }

            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyChar() == 'D') {
                // Move the truck to (200, 200)
                truckX = 100;
                truckY = 100;
                System.out.println("Truck moved to (200, 200)");
            }

            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyChar() == 'S') {
                // Move the truck to (200, 200)
                truckX = 100;
                truckY = 50;
                System.out.println("Truck moved to (200, 200)");
            }
            return false;
        }
    };
//    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
}
