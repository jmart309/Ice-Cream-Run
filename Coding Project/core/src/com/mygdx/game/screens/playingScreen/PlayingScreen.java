package com.mygdx.game.screens.playingScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.ScoreScreen;
import com.mygdx.game.WelcomeScreen;
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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapLayer;
import java.util.Iterator;
import com.mygdx.game.screens.playingScreen.components.Tuple;
import com.mygdx.game.screens.playingScreen.components.TiledArrayGenerator;

import java.util.Iterator;
import com.mygdx.game.screens.playingScreen.components.Tuple;
import com.mygdx.game.screens.playingScreen.components.TiledArrayGenerator;



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
    private float timeLeft;

    private boolean gameOver = false;

    // Background map
    public TiledMap map = new TmxMapLoader().load("newBackground.tmx"); // 21 rows x 39 cols
    public TiledArrayGenerator storeGenerator = new TiledArrayGenerator(map);
    public OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map, 1);



    public PlayingScreen(final TestGame game, String chosenFlavor, int numberOfIceCreams, int gameTimeInSeconds, String vehicleType) {
        this.game = game;
        this.timeLeft = gameTimeInSeconds;
        this.chosenFlavor = chosenFlavor;
        this.numberOfIceCreams = numberOfIceCreams;
        this.shapeRenderer = new ShapeRenderer();
        this.earnedMoney = 0;
        font = new BitmapFont();
        font.setColor(Color.WHITE); // Set font color
        font.getData().setScale(1.5f); // Scale the font size
        if (vehicleType == "Fast Car"){
            truckTexture = new Texture(Gdx.files.internal("fastCar.png"));
        } else if (vehicleType == "Motorcycle"){
            truckTexture = new Texture(Gdx.files.internal("motorcycle.png"));
        } else {
                truckTexture = new Texture(Gdx.files.internal("iscream_truck.png"));
        }
        //float truckWidth = truckTexture.getWidth() / 3;
        //float truckHeight = truckTexture.getHeight() / 3;

        float truckWidth = 31;
        float truckHeight = 31;

        /* Generating store locations */
        storeGenerator.generateStores();


        truck = new TruckDriver(game, storeGenerator, (TiledMapTileLayer) map.getLayers().get("ground"), truckHeight, truckWidth,numberOfIceCreams );

        inputProcessor = new MyInputProcessor(truck);
        Gdx.input.setInputProcessor(inputProcessor);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderer.setView(camera);
//        TiledMapTileLayer layer = (TiledMapTileLayer) map.map.getLayers().get("ground");
//        for (int i = 0; i < 18; i++) {
//            for (int j = 0; j < 32; j++) {
//                Cell cell = layer.getCell(j, i);
//                TiledMapTile tile = cell.getTile();
//                System.out.print(tile.getId() + " ");
//            }
//            System.out.println();
//        }
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // Update camera and batch
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


        // Render the background map
        renderer.setView(camera);
        renderer.render();

        String gas = "Amount of gas left: " + truck.fuel;
        String money = "Earnings: $" + truck.moneyEarned;


        // Begin batch drawing
        game.batch.begin();

        // Draw the truck
        game.batch.draw(truckTexture, truck.truckX, truck.truckY, 31, 31);

        // Draw flavor, number of ice creams, gas, and earnings
        font.draw(game.batch, "Flavor: " + chosenFlavor, 20, Gdx.graphics.getHeight() - 20);
        font.draw(game.batch, "Number of Ice Creams: " + numberOfIceCreams, 20, Gdx.graphics.getHeight() - 50);
        font.draw(game.batch, "Amount of gas left: " + truck.fuel, 20, Gdx.graphics.getHeight() - 80);
        font.draw(game.batch, "Earnings: $" + truck.moneyEarned, 20, Gdx.graphics.getHeight() - 110);

        // End batch drawing
        game.batch.end();


        // Check if the truck reached target coordinates
        if (truck.truckX > 500 && truck.truckX < 600 && truck.truckY > 200 && truck.truckY < 300 && counter == 0) {
            counter++;
            earnedMoney += 10;
            System.out.println("Truck reached target coordinates!");
        }

        gameOver = truck.checkGameOver();

        // Update the timer
        if (timeLeft > 0 && gameOver == false && truck.fuel > 0 ) {
            timeLeft -= delta;
            // Draw the remaining time
            game.batch.begin();
            font.draw(game.batch, "Time left: " + Math.round(timeLeft) + "s", 20, Gdx.graphics.getHeight() - 140);
            game.batch.end();
        } else {
            // Time is up - transition to the score screen
            game.setScreen(new ScoreScreen(game, truck.fuel, truck.moneyEarned,(int) timeLeft, truck.fastestRouteFuelUsage));
            //(int) timeLeft)
            dispose();
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
        map.dispose();
//        map.floorTiles.dispose();
//        map.storeTiles.dispose();
        truckTexture.dispose();
        font.dispose();
    }
}