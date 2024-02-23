package com.mygdx.game.screens.playingScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.TestGame;
import com.mygdx.game.screens.playingScreen.components.TruckDriver;
import com.mygdx.game.screens.playingScreen.components.MyInputProcessor;

public class PlayingScreen implements Screen {

    final TestGame game;
    Pixmap pixmapOrig;
    Pixmap pixmapScaled;
    final int mapWidth;
    final int mapHeight;
    OrthographicCamera camera;
    public Texture backgroundMap;
    //final Texture[][] mapSegments;
    final Texture truckTexture;
    final TruckDriver truck;
    final MyInputProcessor inputProcessor;
    private String chosenFlavor;
    private int numberOfIceCreams;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private int earnedMoney;

    private int counter = 0;
    public PlayingScreen(final TestGame game, String chosenFlavor, int numberOfIceCreams) {
        /*

        this.game = game;
        this.chosenFlavor = chosenFlavor;
        this.numberOfIceCreams = numberOfIceCreams;
        font = new BitmapFont();
        font.setColor(Color.WHITE); // Set font color
        font.getData().setScale(1.5f); // Scale the font size
        truck = new TruckDriver(4, 4);
        pixmapOrig = new Pixmap(Gdx.files.internal("map.png"));  // Dimensions: 1060x x 710px
        mapWidth = 1060 / 4;
        mapHeight = 710 / 4;
        pixmapScaled = new Pixmap(1024, 576, pixmapOrig.getFormat());
        // Creating map segments
        mapSegments = new Texture[truck.rows][truck.cols];
        for (int r = 0; r < truck.rows; r++) {
            for (int c = 0; c < truck.cols; c++) {
                pixmapScaled.drawPixmap(pixmapOrig,
                        c * mapWidth, 533 - (r * mapHeight), mapWidth, mapHeight,
                        0, 0, pixmapScaled.getWidth(), pixmapScaled.getHeight());
                mapSegments[r][c] = new Texture(pixmapScaled);
            }
        }
//        pixmapOrig.dispose();
//        pixmapScaled.dispose();

        backgroundMap = mapSegments[truck.truckRow][truck.truckCol];
        truckTexture = new Texture(Gdx.files.internal("iscream_truck.png"));

        inputProcessor = new MyInputProcessor(truck);
        Gdx.input.setInputProcessor(inputProcessor);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 576);

         */
        this.game = game;
        this.chosenFlavor = chosenFlavor;
        this.numberOfIceCreams = numberOfIceCreams;
        this.shapeRenderer = new ShapeRenderer();
        this.earnedMoney = 0;
        font = new BitmapFont();
        font.setColor(Color.WHITE); // Set font color
        font.getData().setScale(1.5f); // Scale the font size
        truck = new TruckDriver(4, 4);
        pixmapOrig = new Pixmap(Gdx.files.internal("map.png"));  // Load the original map pixmap
        mapWidth = pixmapOrig.getWidth();
        mapHeight = pixmapOrig.getHeight();
        pixmapScaled = new Pixmap(mapWidth, mapHeight, pixmapOrig.getFormat()); // Create a scaled pixmap
        pixmapScaled.drawPixmap(pixmapOrig, 0, 0, mapWidth, mapHeight, 0, 0, mapWidth, mapHeight); // Copy the original pixmap to the scaled pixmap
        backgroundMap = new Texture(pixmapScaled); // Create the texture for the entire map
        truckTexture = new Texture(Gdx.files.internal("iscream_truck.png"));

        inputProcessor = new MyInputProcessor(truck);
        Gdx.input.setInputProcessor(inputProcessor);


        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        /*
        ScreenUtils.clear(0, 0, 0.2f, 1);

        //camera.position.set(truck.truckX + truckTexture.getWidth() / 2, truck.truckY + truckTexture.getHeight() / 2, 0);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        backgroundMap = mapSegments[truck.truckRow][truck.truckCol];

        game.batch.begin();

        game.batch.draw(backgroundMap, 0, 0);
        game.batch.draw(truckTexture, truck.truckX, truck.truckY);

        // Draw the flavor and number of ice creams
        String flavorText = "Flavor: " + chosenFlavor;
        String numIceCreamsText = "Number of Ice Creams: " + numberOfIceCreams;
        font.draw(game.batch, flavorText, 20, Gdx.graphics.getHeight() - 20); // Position the text on the screen
        font.draw(game.batch, numIceCreamsText, 20, Gdx.graphics.getHeight() - 50); // Adjust position as needed

        game.batch.end();

         */
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        String gas = "Amount of gas left: " + truck.fuel;
        String money = "Earnings: $" + earnedMoney;

        game.batch.begin();

        game.batch.draw(backgroundMap, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Render the entire map to fit the screen
        //game.batch.draw(truckTexture, truck.truckX, truck.truckY);


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
        /*
        backgroundMap.dispose();
        pixmapOrig.dispose();
        for (int r = 0; r < truck.rows; r++) {
            for (int c = 0; c < truck.cols; c++) {
                mapSegments[r][c].dispose();
            }
        }
        truckTexture.dispose();
        font.dispose();

         */
    }
}
