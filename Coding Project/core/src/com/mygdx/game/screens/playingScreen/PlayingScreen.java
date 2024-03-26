package com.mygdx.game.screens.playingScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.TestGame;
import com.mygdx.game.screens.playingScreen.components.TiledArrayGenerator;
import com.mygdx.game.screens.playingScreen.components.TruckDriver;
import com.mygdx.game.screens.playingScreen.components.MyInputProcessor;

import java.util.Iterator;

public class PlayingScreen implements Screen {
    final TestGame game;
    OrthographicCamera camera;
    private BitmapFont font;
    final MyInputProcessor inputProcessor;

    final Texture truckTexture;
    final TruckDriver truck;
    private ShapeRenderer shapeRenderer;

    // Game variables
    private String chosenFlavor;
    private int numberOfIceCreams;
    private int earnedMoney;
    private int counter = 0;

    // Background map
//    public TiledMap map = new TmxMapLoader().load("testBackground.tmx"); // 18 rows x 32 cols
    public TiledArrayGenerator map = new TiledArrayGenerator();
    public OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map.map, 1);



    public PlayingScreen(final TestGame game, String chosenFlavor, int numberOfIceCreams) {

        this.game = game;
        this.chosenFlavor = chosenFlavor;
        this.numberOfIceCreams = numberOfIceCreams;
        this.shapeRenderer = new ShapeRenderer();
        this.earnedMoney = 0;
        font = new BitmapFont();
        font.setColor(Color.WHITE); // Set font color
        font.getData().setScale(1.5f); // Scale the font size
        truck = new TruckDriver(4, 4);
        truckTexture = new Texture(Gdx.files.internal("iscream_truck.png"));

        inputProcessor = new MyInputProcessor(truck);
        Gdx.input.setInputProcessor(inputProcessor);


        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderer.setView(camera);
        TiledMapTileLayer layer = (TiledMapTileLayer) map.map.getLayers().get("ground");
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 32; j++) {
                Cell cell = layer.getCell(j, i);
                TiledMapTile tile = cell.getTile();

                System.out.print(tile.getId() + " ");
            }
            System.out.println();
        }
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //camera.position.set(truck.truckX + truckTexture.getWidth() / 2, truck.truckY + truckTexture.getHeight() / 2, 0);
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        String gas = "Amount of gas left: " + truck.fuel;
        String money = "Earnings: $" + earnedMoney;

        renderer.render(); // Renders the background map

        /* Batch drawing starts */
        game.batch.begin();
        float truckWidth = truckTexture.getWidth() / 3;
        float truckHeight = truckTexture.getHeight() / 3;
        game.batch.draw(truckTexture, truck.truckX, truck.truckY, truckWidth, truckHeight);


        // Draw the flavor and number of ice creams
        String flavorText = "Flavor: " + chosenFlavor;
        String numIceCreamsText = "Number of Ice Creams: " + numberOfIceCreams;
        //String gas = "Amount of gas left: " + truck.fuel;
        font.draw(game.batch, flavorText, 20, Gdx.graphics.getHeight() - 20); // Position the text on the screen
        font.draw(game.batch, numIceCreamsText, 20, Gdx.graphics.getHeight() - 50); // Adjust position as needed
        font.draw(game.batch, gas, 20, Gdx.graphics.getHeight() - 80);
        font.draw(game.batch, money, 20, Gdx.graphics.getHeight() - 110);

        game.batch.end();
        /* Batch drawing ends */


        //set location for box so user knows where to deliver ice cream.
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED); // Set the color of the box
        shapeRenderer.rect(550,220, 50, 50); // Draw the rectangle
        shapeRenderer.end();


        //logic for seeing if the truck made it to destination to bring ice cream
        if (truck.truckX > 500 && truck.truckX < 600 && truck.truckY > 200 && truck.truckY < 300 && counter == 0) {
            // Execute the event when the truck reaches the target coordinates
            // For example, display a message
            counter++;
            earnedMoney += 10;
            System.out.println("Truck reached target coordinates!");
        }
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.map.dispose();
        map.floorTiles.dispose();
        map.storeTiles.dispose();

        truckTexture.dispose();
        font.dispose();
    }
}
