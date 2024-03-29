package com.mygdx.game.screens.playingScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.TestGame;
import com.mygdx.game.WelcomeScreen;
import com.mygdx.game.screens.playingScreen.components.TiledArrayGenerator;
import com.mygdx.game.screens.playingScreen.components.TruckDriver;
import com.mygdx.game.screens.playingScreen.components.MyInputProcessor;


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
    ;
    private int earnedMoney;
    private int counter = 0;
    private float timeLeft;
    // Background map
    public TiledMap map = new TmxMapLoader().load("newBackground.tmx"); // 21 rows x 39 cols
    public TiledArrayGenerator storeGenerator = new TiledArrayGenerator(map);
    public OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map, 1);



    public PlayingScreen(final TestGame game, String chosenFlavor, int numberOfIceCreams, int gameTimeInSeconds) {
        this.game = game;
        this.timeLeft = gameTimeInSeconds;
        this.chosenFlavor = chosenFlavor;
        this.numberOfIceCreams = numberOfIceCreams;
        this.shapeRenderer = new ShapeRenderer();
        this.earnedMoney = 0;
        font = new BitmapFont();
        font.setColor(Color.WHITE); // Set font color
        font.getData().setScale(1.5f); // Scale the font size
        truckTexture = new Texture(Gdx.files.internal("iscream_truck.png"));
        //float truckWidth = truckTexture.getWidth() / 3;
        //float truckHeight = truckTexture.getHeight() / 3;

        float truckWidth = 31;
        float truckHeight = 31;

        /* Generating store locations */
        storeGenerator.generateStores();


        truck = new TruckDriver(game,4, 4, (TiledMapTileLayer) map.getLayers().get("ground"), truckHeight, truckWidth);

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
        //camera.position.set(truck.truckX + truckTexture.getWidth() / 2, truck.truckY + truckTexture.getHeight() / 2, 0);
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        String gas = "Amount of gas left: " + truck.fuel;
        String money = "Earnings: $" + earnedMoney;

        renderer.render(); // Renders the background map

        /* Batch drawing starts */
        game.batch.begin();
        float truckWidth = 31;
        float truckHeight = 31;
        game.batch.draw(truckTexture, truck.truckX, truck.truckY, truckWidth, truckHeight);
        //

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


        // game over
        if (timeLeft > 0) {
            // Subtract the time since the last frame
            timeLeft -= delta;

            // Continue with game rendering and logic
            ScreenUtils.clear(0, 0, 0.2f, 1);
            camera.update();
            game.batch.setProjectionMatrix(camera.combined);

            // Existing game rendering code...
            renderer.render();
            game.batch.begin();
            // Draw game elements here...
            game.batch.end();

            // Update game state, handle inputs, etc.

            // Display the remaining time
            game.batch.begin();
            font.draw(game.batch, "Time left: " + Math.round(timeLeft) + "s", 20, Gdx.graphics.getHeight() - 140);
            game.batch.end();

        } else {
            // Time is up - transition to game over or score summary screen

            // This is a placeholder, you'd want to replace GameOverScreen with whatever screen you have for game over
            // For instance, you could create a new Screen that shows the score and a message, and allows restarting
            // Ensure you have such a screen or adjust this to your game's flow
            game.setScreen(new GameOverScreen(game, earnedMoney)); // Assuming you pass the score to the game over screen

            // Dispose of the current screen's resources if necessary
            this.dispose();
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

    public static class GameOverScreen implements Screen {

        private TestGame game;
        private SpriteBatch batch;
        private BitmapFont font;
        private Stage stage;
        private int score;

        public GameOverScreen(TestGame game, int score) {
            this.game = game;
            this.score = score;
            batch = new SpriteBatch();
            font = new BitmapFont();
            stage = new Stage();
            Gdx.input.setInputProcessor(stage);

            createUI();
        }

        private void createUI() {
            Label.LabelStyle labelStyle = new Label.LabelStyle();
            labelStyle.font = font;
            labelStyle.fontColor = Color.WHITE;

            Label gameOverLabel = new Label("Game Over!", labelStyle);
            gameOverLabel.setPosition(Gdx.graphics.getWidth() / 2 - gameOverLabel.getWidth() / 2, Gdx.graphics.getHeight() - 100);

            Label scoreLabel = new Label("Score: " + score, labelStyle);
            scoreLabel.setPosition(Gdx.graphics.getWidth() / 2 - scoreLabel.getWidth() / 2, Gdx.graphics.getHeight() - 150);

            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font = font;
            textButtonStyle.fontColor = Color.WHITE;

            TextButton restartButton = new TextButton("Restart", textButtonStyle);
            restartButton.setPosition(Gdx.graphics.getWidth() / 2 - restartButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - restartButton.getHeight());
            restartButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    // Restart the game by setting the screen to a new instance of PlayingScreen
                    game.setScreen(new WelcomeScreen(game));
                }
            });

            stage.addActor(gameOverLabel);
            stage.addActor(scoreLabel);
            stage.addActor(restartButton);
        }

        @Override
        public void show() {

        }

        @Override
        public void render(float delta) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            stage.draw();
        }

        @Override
        public void resize(int width, int height) {
            stage.getViewport().update(width, height, true);
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
            stage.dispose();
            batch.dispose();
            font.dispose();
        }
    }
}
