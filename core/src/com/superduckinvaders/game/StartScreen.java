package com.superduckinvaders.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.superduckinvaders.game.assets.Assets;

public class StartScreen implements Screen {

    /**
     * The DuckGame this StartScreen belongs to.
     */
    private DuckGame parent;

    
    /**
     * Stage for containing the button.
     */
    private Stage stage;

    /**
     * Initialises this StartScreen.
     * @param parent the game the screen is associated with
     */
    public StartScreen(DuckGame parent) {
        this.parent = parent;
        Assets.load();
    }

    /**
     * Shows this GameScreen. Called by libGDX to set up the graphics.
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Image logoImage = new Image(Assets.logo);
        logoImage.setPosition((stage.getWidth() - logoImage.getPrefWidth()) / 2, (stage.getHeight() - logoImage.getPrefHeight()/2)/2 + 100);

        Drawable button = new TextureRegionDrawable(Assets.button);

        Button playButton = new Button(new Button.ButtonStyle(button, button, button));
        playButton.setPosition((stage.getWidth() - playButton.getPrefWidth()) / 2,  (stage.getHeight() - playButton.getPrefHeight())/2);
        playButton.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                parent.showGameScreen(new Round(parent, Assets.levelOneMap));
            }
        });

        Label.LabelStyle white = new Label.LabelStyle(Assets.font, Color.WHITE);

        Label playLabel = new Label("Click here to play", white);
        playLabel.setPosition((stage.getWidth() - playLabel.getPrefWidth()) / 2, (stage.getHeight() - playLabel.getPrefHeight())/2);
        playLabel.setTouchable(Touchable.disabled);
        

        stage.addActor(logoImage);
        stage.addActor(playButton);
        stage.addActor(playLabel);
    }

    /**
     * Main screen loop.
     *
     * @param delta how much time has passed since the last update
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    /**
     * We now have the technology to resize the game.
     * Gressingham4lyf
     */
    @Override
    public void resize(int width, int height) {
    	show();
    }

    /**
     * Not used.
     */
    @Override
    public void pause() {
    }

    /**
     * Not used.
     */
    @Override
    public void resume() {
    }

    /**
     * Not used.
     */
    @Override
    public void hide() {
    }

    /**
     * Called to dispose libGDX objects used by this StartScreen.
     */
    @Override
    public void dispose() {
    }
}
