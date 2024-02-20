package com.mygdx.game.screens.playingScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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
    final Texture[][] mapSegments;
    final Texture truckTexture;
    final TruckDriver truck;
    final MyInputProcessor inputProcessor;
    public PlayingScreen(final TestGame game) {
        this.game = game;
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
        truckTexture = new Texture(Gdx.files.internal("better_truck.png"));

        inputProcessor = new MyInputProcessor(truck);
        Gdx.input.setInputProcessor(inputProcessor);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 576);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        System.out.printf("%d, %d%n", truck.truckRow, truck.truckCol);
        backgroundMap = mapSegments[truck.truckRow][truck.truckCol];

        game.batch.begin();

        game.batch.draw(backgroundMap, 0, 0);
        game.batch.draw(truckTexture, truck.truckX, truck.truckY);
        game.batch.end();



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
        backgroundMap.dispose();
        pixmapOrig.dispose();
        for (int r = 0; r < truck.rows; r++) {
            for (int c = 0; c < truck.cols; c++) {
                mapSegments[r][c].dispose();
            }
        }
        truckTexture.dispose();
    }
}
