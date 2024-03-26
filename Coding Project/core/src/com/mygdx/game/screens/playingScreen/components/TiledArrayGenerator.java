package com.mygdx.game.screens.playingScreen.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;


public class TiledArrayGenerator extends Gdx {
    public TiledMap map;
    public Texture floorTiles;
    public Texture storeTiles;

    TiledMapTileLayer connectToMid(int x, int y, int mid, int direction, Cell cell, TiledMapTileLayer layer) {
        for (int i = 1; i < Math.abs(y - mid); i++) {
            layer.setCell(x, y + (direction * i), cell);
        }
        return layer;
    }

    public TiledArrayGenerator() {
        // Window size: 1280 x 704
        int width = 40;
        int height = 22;
        int horizontalMainRoad = 10;
        int verticalMainRoad = 19;

        // TODO: Error could be here
        //      camera = new OrthographicCamera();
        //		camera.setToOrtho(false, (w / h) * 320, 320);
        //		camera.update();

        // grabs the tileset image and splits up the tiles into a 2d array
        floorTiles = new Texture(Gdx.files.internal("floors.png"));
        TextureRegion[][] floorTS = TextureRegion.split(floorTiles, 32, 32);
        storeTiles = new Texture(Gdx.files.internal("sheet.png"));
        TextureRegion[][] storeTS = TextureRegion.split(storeTiles, 32, 32);
        StaticTiledMapTile roadTile = new StaticTiledMapTile(floorTS[59][21]);
        roadTile.getProperties().put("blocked", true);

        map = new TiledMap();
        MapLayers layers = map.getLayers();
        TiledMapTileLayer layer = new TiledMapTileLayer(width, height, 32, 32);
        // Initializes to grass tiles
        for (int x = 0; x < width; x++) {
            Cell grassCell = new Cell();
            grassCell.setTile(new StaticTiledMapTile(storeTS[6][6]));
            for (int y = 0; y < height; y++) {
                layer.setCell(x, y, grassCell);
            }
        }
        Cell roadCell = new Cell();
        roadCell.setTile(roadTile);
        // Creates the main roads
        for (int x = 0; x < width; x++) {
            layer.setCell(x, horizontalMainRoad, roadCell);
        }
        for (int y = 0; y < height; y++) {
            layer.setCell(verticalMainRoad, y, roadCell);
        }
        // Creates the roads on the top and bottom
        int direction = 1;
        int minLength, maxLength, prevLength, leftBound;
        double scalingFactor;
        for (int i = 0; i < 2; i++) { // once for above and below the horizontal main road
            prevLength = width;
            leftBound = 0;
            for (int j = 0; j < 3; j++) { // 3 horizontal roads above and below
//                scalingFactor = 0.7 + Math.random() * 0.2;
//                int nextLength = (int) (prevLength * scalingFactor);
                int nextLength = prevLength - 8;
                leftBound += 3 + (int) (Math.random() * 2); // new leftbound
                layer = connectToMid(leftBound, direction * (3 * (j + 1)) + horizontalMainRoad,
                        horizontalMainRoad, direction * -1, roadCell, layer);
                for (int k = 0; k < nextLength; k++) {
                    layer.setCell(leftBound + k, direction * (3 * (j + 1)) + horizontalMainRoad, roadCell);
                }
                layer = connectToMid(leftBound + nextLength - 1, direction * (3 * (j + 1)) + horizontalMainRoad,
                        horizontalMainRoad, direction * -1, roadCell, layer);
                prevLength = nextLength;

            }
            direction *= -1;
        }
        layer.setName("ground");
        layers.add(layer);
    }

}
