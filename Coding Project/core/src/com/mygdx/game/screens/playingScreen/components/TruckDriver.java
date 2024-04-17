package com.mygdx.game.screens.playingScreen.components;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.mygdx.game.ScoreScreen;

import java.util.*;

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

    public int fastestRouteFuelUsage;

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

        this.fastestRouteFuelUsage = findShortestPath(generator);

    }

    class Node {
        int x, y;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public double distanceTo(Node other) {
            int xDistance = 5;
            int yDistance = 4;

            Integer xDifference = Math.abs(other.x - this.x) / xDistance;
            Integer yDifference = Math.abs(other.y - this.y) / yDistance;

            return (xDifference + yDifference) * 4.5;
        }
    }

    public static Map<Integer, Map<Integer, Double>> createWeightedGraph(Node[] nodes) {
        Map<Integer, Map<Integer, Double>> graph = new HashMap<>();
        for (int i = 0; i < nodes.length; i++) {
            Map<Integer, Double> weights = new HashMap<>();
            for (int j = 0; j < nodes.length; j++) {
                if (i != j) {
                    double distance = nodes[i].distanceTo(nodes[j]);
                    weights.put(j, distance);
                }
            }
            graph.put(i, weights);
        }
        return graph;
    }



    static class NodeDistance {
        int node;
        double distance;

        public NodeDistance(int node, double distance) {
            this.node = node;
            this.distance = distance;
        }
    }



    public static List<Integer> nearestNeighbor(Map<Integer, Map<Integer, Double>> graph, int startNode) {
        List<Integer> path = new ArrayList<>();
        Set<Integer> unvisited = new HashSet<>(graph.keySet());

        // Start from the given start node
        int current = startNode;
        path.add(current);
        unvisited.remove(current);

        while (!unvisited.isEmpty()) {
            double minDistance = Double.MAX_VALUE;
            int nearestNeighbor = -1;
            for (int neighbor : graph.get(current).keySet()) {
                if (unvisited.contains(neighbor) && graph.get(current).get(neighbor) < minDistance) {
                    minDistance = graph.get(current).get(neighbor);
                    nearestNeighbor = neighbor;
                }
            }
            if (nearestNeighbor != -1) {
                path.add(nearestNeighbor);
                unvisited.remove(nearestNeighbor);
                current = nearestNeighbor;
            } else {
                // If there's no unvisited neighbor, go back to the start node
                path.add(startNode);
                current = startNode;
            }
        }

        return path;
    }


    private int findShortestPath(TiledArrayGenerator gen){
        int index = 1;
        Node[] nodes = new Node[5];
        nodes[0] = new Node(0, 10);
        for(Integer store: gen.checked){
            Tuple<Integer, Integer> r = gen.storeLocations.get(store);
            nodes[index] = new Node(r.x, r.y);
            index++;
        }

        System.out.println(nodes[0] == null);
        System.out.println(nodes[1] == null);
        System.out.println(nodes[2] == null);
        System.out.println(nodes[3] == null);
        System.out.println(nodes[4] == null);

        Map<Integer, Map<Integer, Double>> weightedGraph = createWeightedGraph(nodes);
        List<Integer> shortestPath = nearestNeighbor(weightedGraph, 0);
        int startNode = 0;
        //Map<Integer, Double> shortestDistances = dijkstra(weightedGraph, startNode);
        /*
        for (Map.Entry<Integer, Double> entry : shortestDistances.entrySet()) {
            int node = entry.getKey();
            double distance = entry.getValue();
            System.out.println("Shortest distance from node " + startNode + " to node " + node + ": " + distance);
        }

         */
        System.out.println("Shortest path to discover all nodes starting from node " + startNode + ": ");
        int totalWeight = 0;
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            int currentNode = shortestPath.get(i);
            int nextNode = shortestPath.get(i + 1);
            double weight = weightedGraph.get(currentNode).get(nextNode);
            totalWeight += weight;
            System.out.println("Node " + currentNode + " -> Node " + nextNode + ", Weight: " + weight);
        }
        System.out.println("Total Weight of Shortest Path: " + totalWeight);

        return totalWeight;

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