package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ScoreScreen implements Screen {
    private Game game;
    private int totalFuelUsed;
    private int iceCreamSold;
    private Stage stage;
    private Label scoreLabel;
    private BitmapFont font;

    public ScoreScreen(Game game, int totalFuelUsed, int IceCreamSold) {
        this.game = game;
        this.totalFuelUsed = totalFuelUsed;
        this.iceCreamSold = iceCreamSold;
        this.stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        font.getData().setScale(2);
        scoreLabel = new Label("Total Fuel Used: " + totalFuelUsed + "\nIce Creams Sold: " + iceCreamSold, // Display ice creams sold
                new Label.LabelStyle(font, font.getColor()));        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(scoreLabel);

        // Restart button
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = font;
        TextButton restartButton = new TextButton("Restart", buttonStyle);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Restart the game with the WelcomeScreen
                game.setScreen(new WelcomeScreen((TestGame) game));
            }
        });
        table.row();
        table.add(restartButton).padTop(20);

        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
        font.dispose();
    }
}