package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.screens.playingScreen.PlayingScreen;

import java.util.Map;

public class WelcomeScreen implements Screen {
    private TestGame game;
    private Stage stage;
    private TextButton playButton, chocolateButton, vanillaButton;
    private TextField usernameField, passwordField, iceCreamCountField;
    private Label titleLabel;
    private BitmapFont font;
    private String chosenFlavor = "";
    public static int numberOfIceCreams = 5;
    private TextButton easyButton, mediumButton, hardButton;
    private int gameTimeInSeconds = 120;

    public WelcomeScreen(TestGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont();
        font.getData().setScale(1.5f);

        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;

        TextFieldStyle textFieldStyle = new TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;

        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.CYAN;

        titleLabel = new Label("Ice Cream Run", labelStyle);
        titleLabel.setPosition(Gdx.graphics.getWidth() / 2 - titleLabel.getWidth() / 2, Gdx.graphics.getHeight() - titleLabel.getHeight() - 20);

        usernameField = new TextField("", textFieldStyle);
        usernameField.setMessageText("Username");
        usernameField.setPosition(Gdx.graphics.getWidth() / 2 - usernameField.getWidth() / 2, Gdx.graphics.getHeight() / 2 + usernameField.getHeight() * 2);

        passwordField = new TextField("", textFieldStyle);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setPosition(Gdx.graphics.getWidth() / 2 - passwordField.getWidth() / 2, Gdx.graphics.getHeight() / 2 + passwordField.getHeight());

        iceCreamCountField = new TextField("", textFieldStyle);
        iceCreamCountField.setMessageText("# of Ice Creams");
        iceCreamCountField.setPosition(Gdx.graphics.getWidth() / 2 - iceCreamCountField.getWidth() / 2, Gdx.graphics.getHeight() / 2 - iceCreamCountField.getHeight());
        iceCreamCountField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    numberOfIceCreams = Integer.parseInt(iceCreamCountField.getText());
                } catch (NumberFormatException e) {
                    numberOfIceCreams = 0; // Handle invalid input
                }
            }
        });


        chocolateButton = new TextButton("Chocolate", buttonStyle);
        chocolateButton.setPosition(Gdx.graphics.getWidth() / 3 - chocolateButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 2 * chocolateButton.getHeight());
        chocolateButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Add logic for Chocolate button click
                chosenFlavor = "Chocolate";
            }
        });

        vanillaButton = new TextButton("Vanilla", buttonStyle);
        vanillaButton.setPosition(2 * Gdx.graphics.getWidth() / 3 - vanillaButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 2 * vanillaButton.getHeight());
        vanillaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Add logic for Vanilla button click.
                chosenFlavor = "Vanilla";
            }
        });

        playButton = new TextButton("Play", buttonStyle);
        playButton.setPosition(Gdx.graphics.getWidth() / 2 - playButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 3 * playButton.getHeight());
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                // You can add your logic here for handling the username and password
                game.setScreen(new PlayingScreen(game, chosenFlavor, numberOfIceCreams, gameTimeInSeconds));  // THIS WILL LEAD US TO VERSION 1 OF GAMEPLAY
                //MapViewer.main(); // THIS WILL LEAD US TO OPENSTREEMAPS.
            }
        });

        // game difficulty options
        easyButton = new TextButton("Easy", buttonStyle);
        mediumButton = new TextButton("Medium", buttonStyle);
        hardButton = new TextButton("Hard", buttonStyle);
        easyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameTimeInSeconds = 120; // Easy: 30 seconds
            }
        });
        mediumButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameTimeInSeconds = 60; // Medium: 60 seconds
            }
        });
        hardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameTimeInSeconds = 30; // Hard: 2 minutes
            }
        });
        positionButtons();


        stage.addActor(titleLabel);
        stage.addActor(usernameField);
        stage.addActor(passwordField);
        stage.addActor(iceCreamCountField);
        stage.addActor(chocolateButton);
        stage.addActor(vanillaButton);
        stage.addActor(playButton);
        stage.addActor(easyButton);
        stage.addActor(mediumButton);
        stage.addActor(hardButton);
    }

    private void positionButtons() {
        // Assuming each button has been initialized (e.g., in your show() method)
        int buttonWidth = 200; // Width of each button, adjust as needed
        int spacing = 20; // Space between buttons
        // Calculate the starting X position to center the buttons as a group
        float totalWidth = 3 * buttonWidth + 2 * spacing; // Total width of all buttons and spaces between them
        float startX = (Gdx.graphics.getWidth() - totalWidth) / 2; // Starting X position to center the group
        // Set the positions of the buttons
        easyButton.setPosition(startX, easyButton.getY());
        mediumButton.setPosition(startX + buttonWidth + spacing, mediumButton.getY());
        hardButton.setPosition(startX + 2 * (buttonWidth + spacing), hardButton.getY());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Update your stage's viewport when the screen size changes
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // This method is called when the game is paused, usually when it loses focus
        // Implement any game pausing logic here
    }

    @Override
    public void resume() {
        // This method is called when the game resumes from a paused state
        // Implement any game resuming logic here
    }

    @Override
    public void hide() {
        // This method is called when the screen is no longer the current screen
        // Dispose of or hide resources that are not needed while this screen is inactive
    }

    @Override
    public void dispose() {
        // This method is called when the screen is destroyed
        // Dispose of all resources (textures, stages, etc.) to avoid memory leaks
        stage.dispose();
        // Add any additional resource disposals here
    }
}