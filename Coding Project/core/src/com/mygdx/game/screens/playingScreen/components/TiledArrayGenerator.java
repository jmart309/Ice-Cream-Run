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
    public Texture grassTiles = new Texture(Gdx.files.internal("sheet.png"));
    public Texture storeTiles = new Texture(Gdx.files.internal("stores/stores.png"));
    public Texture trashTiles = new Texture(Gdx.files.internal("stores/trash.png"));
    public TextureRegion[][] floorTS = TextureRegion.split(floorTiles, 32, 32);
    public TextureRegion[][] grassTS = TextureRegion.split(grassTiles, 32, 32);
    public TextureRegion[][] storeTS = TextureRegion.split(storeTiles, 32, 32);
    public TextureRegion[][] trashTS = TextureRegion.split(trashTiles, 32, 32);
    public TextureRegion[][] iceCreamStore = new TextureRegion[2][3];
    public Cell sellHere = new Cell();
    public ArrayList<Tuple<Integer, Integer>> storeLocations = new ArrayList<>();

    TiledMapTileLayer connectToMid(int x, int y, int mid, int direction, Cell cell, TiledMapTileLayer layer) {
        for (int i = 1; i < Math.abs(y - mid); i++) {
            layer.setCell(x, y + (direction * i), cell);
        }
        return layer;
    }

    public TiledArrayGenerator(TiledMap map) {
        // Window size: 1248 x 672
        int width = 40;
        int height = 22;
        int horizontalMainRoad = 10;
        int verticalMainRoad = 19;
        sellHere.setTile(new StaticTiledMapTile(trashTS[0][1]));

        StaticTiledMapTile roadTile = new StaticTiledMapTile(floorTS[59][21]);
        roadTile.getProperties().put("road", true);

//        map = new TiledMap();
        this.map = map;
//        MapLayers layers = map.getLayers();
//        TiledMapTileLayer layer = new TiledMapTileLayer(width, height, 32, 32);
//
//        // Initializes to grass tiles
//        for (int x = 0; x < width; x++) {
//            Cell grassCell = new Cell();
//            grassCell.setTile(new StaticTiledMapTile(grassTS[6][6]));
//            for (int y = 0; y < height; y++) {
//                layer.setCell(x, y, grassCell);
//            }
//        }
//
//        Cell roadCell = new Cell();
//        roadCell.setTile(roadTile);
//        // Creates the main roads
//        for (int x = 0; x < width; x++) {
//            layer.setCell(x, horizontalMainRoad, roadCell);
//        }
//        for (int y = 0; y < height; y++) {
//            layer.setCell(verticalMainRoad, y, roadCell);
//        }
//
//        // Creates the roads on the top and bottom
//        int direction = 1;
//        int prevLength, leftBound;
//        double scalingFactor;
//        for (int i = 0; i < 2; i++) { // once for above and below the horizontal main road
//            prevLength = width;
//            leftBound = 0;
//            for (int j = 0; j < 3; j++) { // 3 horizontal roads above and below
////                scalingFactor = 0.7 + Math.random() * 0.2;
////                int nextLength = (int) (prevLength * scalingFactor);
//                int nextLength = prevLength - 8;
//                leftBound += 3 + (int) (Math.random() * 2); // new leftbound
//                layer = connectToMid(leftBound, direction * (3 * (j + 1)) + horizontalMainRoad,
//                        horizontalMainRoad, direction * -1, roadCell, layer);
//                for (int k = 0; k < nextLength; k++) {
//                    layer.setCell(leftBound + k, direction * (3 * (j + 1)) + horizontalMainRoad, roadCell);
//                }
//                layer = connectToMid(leftBound + nextLength - 1, direction * (3 * (j + 1)) + horizontalMainRoad,
//                        horizontalMainRoad, direction * -1, roadCell, layer);
//                prevLength = nextLength;
//
//            }
//            direction *= -1;
//        }
//        layer.setName("ground");
//        layers.add(layer);
        addStoresToList();
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
        System.out.println(storeLocations.size());
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 3; y++) {
                iceCreamStore[x][y] = storeTS[3 - x][9 + y];
            }
        }
    }

    public void generateStores() {
        TiledMapTileLayer groundLayer = (TiledMapTileLayer) map.getLayers().get("ground");

        HashSet<Integer> checked = new HashSet<>();
        while (checked.size() < 4) {
            int newShop = (int) (Math.random() * 19);
            if (checked.contains(newShop)) {
                continue;
            }
            placeStore(storeLocations.get(newShop).x, storeLocations.get(newShop).y);
            groundLayer.getCell(storeLocations.get(newShop).x, storeLocations.get(newShop).y).getTile().getProperties().put("sellHere", false);
            checked.add(newShop);
        }
    }

    public void placeStore(int x, int y) {
        TiledMapTileLayer storeLayer = (TiledMapTileLayer) map.getLayers().get("stores");
        storeLayer.setCell(x, y, sellHere);
        y += 1;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {
                storeLayer.getCell(x + col, y + row).setTile(new StaticTiledMapTile(iceCreamStore[row][col]));
            }
        }
    }


}
