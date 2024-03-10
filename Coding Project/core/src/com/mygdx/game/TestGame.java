package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TestGame extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        // Set the WelcomeScreen as the initial screen
        this.setScreen(new WelcomeScreen(this));
    }

    @Override
    public void render() {
        super.render(); // Important to call the render method of the parent class
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        // Don't forget to dispose of other assets as needed
    }

    // Other methods if needed
}
