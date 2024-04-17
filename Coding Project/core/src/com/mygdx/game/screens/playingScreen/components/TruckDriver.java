package com.mygdx.game.screens.playingScreen.components;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.mygdx.game.ScoreScreen;

import java.util.HashSet;
import java.util.Random;

//import static com.mygdx.game.WelcomeScreen.numberOfIceCreams;

public class TruckDriver {
    private Game game;
    public int fuel;
    int added_fuel = 0;
    public int truckX = 0; // x-coordinate on the map segment
    public int truckY = 320; // y-coordinate on the map segment
    public int moneyEarned = 0;
    public Tuple<Integer, Integer> prevNode;
    public Tuple<Integer, Integer> currentNode;
    private HashSet<Integer> xNodes;
    private HashSet<Integer> yNodes;
    private TiledArrayGenerator generator;

    float truckHeight;
    float truckWidth;
    private int totalfuel;
    private int totalIce;
    TiledMapTileLayer collisionLayer;
    private Random random;

    public TruckDriver(Game game, TiledArrayGenerator generator, TiledMapTileLayer collisionLayer, float truckHeight, float truckWidth, int numberOfIceCreams) {
        this.game = game;
        this.truckHeight = truckHeight;
        this.truckWidth = truckWidth;
        this.generator = generator;
        this.collisionLayer = collisionLayer;
        this.fuel =  100;
        totalfuel = fuel;
        totalIce = numberOfIceCreams;
        random = new Random();
        xNodes = new HashSet<>();
        yNodes = new HashSet<>();
        for (int i = 0; i < 7; i++) {
            xNodes.add(4 + i*5);
        }
        for (int i = 0; i < 5; i++) {
            yNodes.add(2 + i*4);
        }

    }


    private boolean collisionDetection(int x, int y){
        float tileWidth = collisionLayer.getTileWidth();
        float tileHeight = collisionLayer.getTileHeight();
        boolean collisionX = false;
        //boolean collisionY = false;

        System.out.println((int) (x / tileWidth) +  ", " +  (int) (y / tileHeight));
        /*
        Iterator<String> keysIterator = collisionLayer.getCell(4, 16).getTile().getProperties().getKeys();

        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            System.out.println(key);
        }

         */

        collisionX = collisionLayer.getCell((int) (x / tileWidth), (int) ((y + truckHeight) / tileHeight))
                .getTile().getProperties().containsKey("road");

        if(collisionX == false){
            collisionX = collisionLayer.getCell((int) (x / tileWidth), (int) ((y) / tileHeight))
                    .getTile().getProperties().containsKey("road");
        }

        if(collisionX == false){
            collisionX = collisionLayer.getCell((int) ((x + truckWidth) / tileWidth), (int) ((y + truckHeight) / tileHeight))
                    .getTile().getProperties().containsKey("road");
        }

        if(collisionX == false){
            collisionX = collisionLayer.getCell((int) ((x + truckWidth) / tileWidth), (int) ((y) / tileHeight))
                    .getTile().getProperties().containsKey("road");
        }
        return collisionX;


    }

    public void checkFuelAndChangeScreen() {
        if (fuel <= 0) {
            //game.setScreen(new ScoreScreen(game, totalfuel, totalIce - totalfuel)); // Show score screen with total fuel used -for now
        }
    }

    public boolean checkGameOver(){
        if(moneyEarned == 400){
            return true;
        }
        return false;
    }


    private boolean checkSell(int x, int y){
        float tileWidth = collisionLayer.getTileWidth();
        float tileHeight = collisionLayer.getTileHeight();
        boolean location = false;
        boolean sell = false;


        location = collisionLayer.getCell((int) (x / tileWidth), (int) ((y + truckHeight) / tileHeight))
                .getTile().getProperties().containsKey("sellHere");

        if (location == true){
            sell = (boolean) collisionLayer.getCell((int) (x / tileWidth), (int) ((y + truckHeight) / tileHeight))
                    .getTile().getProperties().get("sellHere");

            System.out.println("The value of green tile is " + sell);
            if(sell){
                return false;
            }
            collisionLayer.getCell((int) (x / tileWidth), (int) ((y + truckHeight) / tileHeight))
                    .getTile().getProperties().put("sellHere", true);

        }



        System.out.println(location);


        return location;

    }

    public boolean updateNodes() {
        int tileWidth = collisionLayer.getTileWidth();
        int tileHeight = collisionLayer.getTileHeight();
        // Checks that the truck has moved onto a crossroad (AKA node)
        if (xNodes.contains(truckX / tileWidth) && yNodes.contains(truckY / tileHeight)) {
            prevNode = currentNode;
            currentNode = new Tuple<>(truckX / tileWidth, truckY / tileHeight);
            if (prevNode != null)
                System.out.println("Prev Node X:" + prevNode.x + "Prev Node Y:" + prevNode.y);
            if (currentNode != null)
                System.out.println("Curr Node X:" + currentNode.x + "Curr Node Y:" + currentNode.y);
            return true;
        }
        return false;
    }

    public boolean compareTuple(Tuple<Integer,Integer> one, Tuple<Integer,Integer> two) {
        return one.x.equals(two.x) && one.y.equals(two.y);
    }

    public boolean dispatchKeyEvent(int keycode) {
        boolean onNode = false;
        // Switch statement to handle key presses
        switch(keycode) {
            case Input.Keys.W:
            case Input.Keys.UP:
                // Move the truck upwards & decrease fuel as we move
                boolean collidesUp = collisionDetection(truckX, truckY + 32);
                if (collidesUp) {
                    truckY += 32;
                    // Update the last and most recent node, and the fuel usage
                    if (updateNodes() && prevNode != null && !compareTuple(prevNode, currentNode)) {
                        System.out.println("Updating fuel");
                        fuel -= generator.getCost(prevNode.x, prevNode.y, generator.graph.UP);
                    }
                    checkFuelAndChangeScreen();
                }
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                // Move the truck downwards & decrease fuel as we move
                boolean collidesDown = collisionDetection(truckX, truckY - 32);
                if (collidesDown) {
                    truckY -= 32;
                    // Update the last and most recent node, and the fuel usage
                    if (updateNodes() && prevNode != null && !compareTuple(prevNode, currentNode)) {
                        System.out.println("Updating fuel");
                        fuel -= generator.getCost(prevNode.x, prevNode.y, generator.graph.DOWN);
                    }
                    checkFuelAndChangeScreen();
                }
                break;
            case Input.Keys.A:
            case Input.Keys.LEFT:
                if (truckX - 32 < 0) return false;
                // Move the truck left & decrease fuel as we move
                boolean collidesLeft = collisionDetection(truckX - 32, truckY);
                if (collidesLeft) {
                    truckX -= 32;
                    // Update the last and most recent node, and the fuel usage
                    if (updateNodes() && prevNode != null && !compareTuple(prevNode, currentNode)) {
                        System.out.println("Updating fuel");
                        fuel -= generator.getCost(prevNode.x, prevNode.y, generator.graph.LEFT);
                    }
                    checkFuelAndChangeScreen();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                if (truckX + 32 >= 1248) return false;
                // Move the truck right & decrease fuel as we move
                boolean collidesRight = collisionDetection(truckX + 32, truckY);
                if (collidesRight) {
                    truckX += 32;
                    // Update the last and most recent node, and the fuel usage
                    if (updateNodes() && prevNode != null && !compareTuple(prevNode, currentNode)) {
                        System.out.println("Updating fuel");
                        fuel -= generator.getCost(prevNode.x, prevNode.y, generator.graph.RIGHT);
                    }
                    checkFuelAndChangeScreen();
                }
                break;
            case Input.Keys.SPACE:
                System.out.println("space bar pressed");
                boolean location = checkSell(this.truckX, this.truckY);
                if(location){
                    generator.sellAtLocation(this.truckX / 32, this.truckY / 32);
                    moneyEarned += 100;
                    System.out.println("adding score");
                }
                default:
                break;
        }

        return false;
    }




//    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
}