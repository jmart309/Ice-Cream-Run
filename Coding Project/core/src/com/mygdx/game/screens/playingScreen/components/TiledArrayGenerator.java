package com.mygdx.game.screens.playingScreen.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

import java.util.ArrayList;
import java.util.HashSet;


public class TiledArrayGenerator extends Gdx {
    public TiledMap map;
    // grabs the tileset image and splits up the tiles into a 2d array
    public Texture floorTiles = new Texture(Gdx.files.internal("floors.png"));
    public Texture numberTiles = new Texture(Gdx.files.internal("numbers.png"));
    public Texture storeTiles = new Texture(Gdx.files.internal("stores/stores.png"));
    public Texture trashTiles = new Texture(Gdx.files.internal("stores/trash.png"));
    public TextureRegion[][] floorTS = TextureRegion.split(floorTiles, 32, 32);
    public TextureRegion[][] numberTS = TextureRegion.split(numberTiles, 32, 32);
    public TextureRegion[][] storeTS = TextureRegion.split(storeTiles, 32, 32);
    public TextureRegion[][] trashTS = TextureRegion.split(trashTiles, 32, 32);
    public TextureRegion[][] iceCreamStore = new TextureRegion[2][3];
    public ArrayList<Tuple<Integer, Integer>> storeLocations = new ArrayList<>();
    public Graph graph = new Graph();
    public HashSet<Integer> checked;

    TiledMapTileLayer connectToMid(int x, int y, int mid, int direction, Cell cell, TiledMapTileLayer layer) {
        for (int i = 1; i < Math.abs(y - mid); i++) {
            layer.setCell(x, y + (direction * i), cell);
        }
        return layer;
    }

    public TiledArrayGenerator(TiledMap map) {
        StaticTiledMapTile roadTile = new StaticTiledMapTile(floorTS[59][21]);
        roadTile.getProperties().put("road", true);
        checked = new HashSet<>();

        this.map = map;
        addStoresToList();
        placeFuelUsage();
    }

    private void addStoresToList() {
        storeLocations.add(new Tuple<>(6, 2));
        storeLocations.add(new Tuple<>(16, 2));
        storeLocations.add(new Tuple<>(21, 2));
        storeLocations.add(new Tuple<>(31, 2));
        storeLocations.add(new Tuple<>(10, 6));
        storeLocations.add(new Tuple<>(16, 6));
        storeLocations.add(new Tuple<>(26, 6));
        storeLocations.add(new Tuple<>(30, 6));
        storeLocations.add(new Tuple<>(5, 10));
        storeLocations.add(new Tuple<>(11, 10));
        storeLocations.add(new Tuple<>(20, 10));
        storeLocations.add(new Tuple<>(26, 10));
        storeLocations.add(new Tuple<>(5, 14));
        storeLocations.add(new Tuple<>(10, 14));
        storeLocations.add(new Tuple<>(16, 14));
        storeLocations.add(new Tuple<>(25, 14));
        storeLocations.add(new Tuple<>(31, 14));
        storeLocations.add(new Tuple<>(5, 18));
        storeLocations.add(new Tuple<>(20, 18));
        // Sets up the tiles for an ice cream store into a 2D array
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 3; y++) {
                iceCreamStore[x][y] = storeTS[3 - x][9 + y];
            }
        }
    }

    public int getCost(int x, int y, int direction) {
        x = (x - 4) / 5;
        y = (y - 2) / 4;
        switch (direction) {
            // UP
            case 0:
                return graph.nodeSystem[x][y].upCost;
            // RIGHT
            case 1:
                return graph.nodeSystem[x][y].rightCost;
            // DOWN
            case 2:
                return graph.nodeSystem[x][y].downCost;
            //LEFT
            case 3:
                return graph.nodeSystem[x][y].leftCost;
            default:
                return 0;
        }
    }

    public void generateStores() {
        TiledMapTileLayer groundLayer = (TiledMapTileLayer) map.getLayers().get("ground");

        while (checked.size() < 4) {
            int newShop = (int) (Math.random() * 19);
            if (checked.contains(newShop)) {
                continue;
            }
            Cell cell = new Cell();
            cell.setTile(new StaticTiledMapTile(floorTS[59][21]));
            cell.getTile().getProperties().put("road", true);
            cell.getTile().getProperties().put("sellHere", false);
            placeStore(storeLocations.get(newShop).x, storeLocations.get(newShop).y);
            groundLayer.setCell(storeLocations.get(newShop).x, storeLocations.get(newShop).y, cell);
            checked.add(newShop);
        }
    }

    public void placeStore(int x, int y) {
        TiledMapTileLayer storeLayer = (TiledMapTileLayer) map.getLayers().get("stores");
        Cell cell = new Cell();
        cell.setTile(new StaticTiledMapTile(trashTS[0][1]));
        storeLayer.setCell(x, y, cell);
        y += 1;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {
                storeLayer.getCell(x + col, y + row).setTile(new StaticTiledMapTile(iceCreamStore[row][col]));
            }
        }
    }

    public void placeFuelUsage() {
        TiledMapTileLayer numberLayer = new TiledMapTileLayer(38, 20, 32, 32);
        ArrayList<StaticTiledMapTile> staticNumTiles = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                staticNumTiles.add(new StaticTiledMapTile(numberTS[i][j]));
            }
        }
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 5; y++) {
                Cell rightCell = new Cell();
                rightCell.setTile(staticNumTiles.get(graph.nodeSystem[x][y].rightCost - 1));
                numberLayer.setCell((x * 5 + 7), (y * 4 + 2), rightCell);
                Cell upCell = new Cell();
                upCell.setTile(staticNumTiles.get(graph.nodeSystem[x][y].upCost - 1));
                numberLayer.setCell((x * 5 + 4), (y * 4 + 4), upCell);
            }
        }
        map.getLayers().add(numberLayer);
    }

    public void sellAtLocation(int x, int y) {
        TiledMapTileLayer storeLayer = (TiledMapTileLayer) map.getLayers().get("stores");
        storeLayer.getCell(x, y).setTile(new StaticTiledMapTile(trashTS[0][2]));
    }


}
