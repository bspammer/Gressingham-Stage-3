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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.superduckinvaders.game.assets.Assets;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
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
        
        
    }

    /**
     * Shows this GameScreen. Called by libGDX to set up the graphics.
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        
        Image logoImage = new Image(Assets.logo);
        logoImage.setPosition((stage.getWidth() - logoImage.getPrefWidth()) / 2, (stage.getHeight() - logoImage.getPrefHeight()/2)/2 + 160);

        Drawable button = new TextureRegionDrawable(Assets.button);

        Button levelSelectButton = new Button(new Button.ButtonStyle(button, button, button));
        levelSelectButton.setPosition((stage.getWidth() - levelSelectButton.getPrefWidth()) / 2,  (stage.getHeight() - levelSelectButton.getPrefHeight())/2);
        levelSelectButton.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
            	Assets.buttonPress.play(DuckGame.MasterVol);
            	DuckGame.loadSettings();
                parent.showLevelSelectScreen();
            }
        });

        Label.LabelStyle white = new Label.LabelStyle(Assets.font, Color.WHITE);

        Label levelSelectLabel = new Label("Level Select", white);
        levelSelectLabel.setPosition((stage.getWidth() - levelSelectLabel.getPrefWidth()) / 2, (stage.getHeight() - levelSelectLabel.getPrefHeight())/2);
        levelSelectLabel.setTouchable(Touchable.disabled);
        
        
      //button for level one
        Button playButtonOne = new Button(new Button.ButtonStyle(button, button, button));
        playButtonOne.setPosition((((stage.getWidth() - playButtonOne.getPrefWidth())) /2) ,  (stage.getHeight() - playButtonOne.getPrefHeight())/2 +60);
        playButtonOne.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
            	Assets.buttonPress.play(DuckGame.MasterVol);
            	DuckGame.newGame();
                parent.showGameScreen(new Round(parent, Assets.levelOneMap));
            }
        });

        //Label for level one
        Label playLabelOne = new Label("New Game", white);
        playLabelOne.setPosition((((stage.getWidth() - playLabelOne.getPrefWidth())) /2) , (stage.getHeight() - playLabelOne.getPrefHeight())/2 +60);
        playLabelOne.setTouchable(Touchable.disabled);
        

      //button for settings
        Button settingsButton = new Button(new Button.ButtonStyle(button, button, button));
        settingsButton.setPosition((((stage.getWidth() - playButtonOne.getPrefWidth())) /2) ,  (stage.getHeight() - playButtonOne.getPrefHeight())/2 -60);
        settingsButton.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
            	Assets.buttonPress.play(DuckGame.MasterVol);
            	DuckGame.newGame();
                parent.showSettingsScreen();
            }
        });

        //Label for level one
        Label settingsLabel = new Label("Settings", white);
        settingsLabel.setPosition((((stage.getWidth() - playLabelOne.getPrefWidth())) /2) , (stage.getHeight() - playLabelOne.getPrefHeight())/2 -60);
        settingsLabel.setTouchable(Touchable.disabled);
        
        stage.addActor(logoImage);
        stage.addActor(levelSelectButton);
        stage.addActor(levelSelectLabel);
        stage.addActor(playButtonOne);
        stage.addActor(playLabelOne);
        stage.addActor(settingsButton);
        stage.addActor(settingsLabel);
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
