package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.TestGame;

public class PlayingScreen implements Screen {

    final TestGame game;
    Pixmap pixmapOrig;
    Pixmap pixmapScaled;
    OrthographicCamera camera;
    final Texture backgroundMap;
    public PlayingScreen(final TestGame game) {
        this.game = game;
        pixmapOrig = new Pixmap(Gdx.files.internal("map.png"));
        pixmapScaled = new Pixmap(1024, 576, pixmapOrig.getFormat());
        pixmapScaled.drawPixmap(pixmapOrig,
                0, 0, pixmapOrig.getWidth(), pixmapOrig.getHeight(),
                0, 0, pixmapScaled.getWidth(), pixmapScaled.getHeight());
        backgroundMap = new Texture(pixmapScaled);

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

        game.batch.begin();
        game.batch.draw(backgroundMap, 0, 0);
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
        pixmapScaled.dispose();
    }
}
