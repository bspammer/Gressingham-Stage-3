package com.superduckinvaders.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

public class SettingsScreen implements Screen {

		private int masterVol;

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
	    public SettingsScreen(DuckGame parent) {
	        this.parent = parent;
	        
	    }

	    /**
	     * Shows this GameScreen. Called by libGDX to set up the graphics.
	     */
	    @Override
	    public void show() {
	        masterVol=(int) (DuckGame.MasterVol*100);

	        stage = new Stage(new ScreenViewport());
	        Gdx.input.setInputProcessor(stage);

	        
	        Image logoImage = new Image(Assets.logo);
	        logoImage.setPosition((stage.getWidth() - logoImage.getPrefWidth()) / 2, (stage.getHeight() - logoImage.getPrefHeight()/2)/2 + 160);

	        
	        Label.LabelStyle white = new Label.LabelStyle(Assets.font, Color.WHITE);
	        Label levelSelectLabel = new Label(Integer.toString(masterVol), white);
	        levelSelectLabel.setPosition((stage.getWidth() - levelSelectLabel.getPrefWidth()) / 2, (stage.getHeight() - levelSelectLabel.getPrefHeight())/2-60);
	        levelSelectLabel.setTouchable(Touchable.disabled);
	        
	        Drawable downButton = new TextureRegionDrawable(Assets.downButton);
	        Drawable upButton = new TextureRegionDrawable(Assets.upButton);
	        
	        Button masterUp = new Button(new Button.ButtonStyle(upButton, upButton, upButton));
	        masterUp.setPosition((stage.getWidth() - masterUp.getPrefWidth()) / 2 +40,  (stage.getHeight() - masterUp.getPrefHeight())/2-60);
	        masterUp.addListener(new ClickListener() {

	            public void clicked(InputEvent event, float x, float y) {
	            	Assets.buttonPress.play(DuckGame.MasterVol);
	            	if (masterVol >= 100){
	            		
	            	}else{
	            		masterVol+=5;
	            	}
	            	applySettings();
	            	levelSelectLabel.setText(Integer.toString(masterVol));

	            	
	            	stage.draw();
	            }
	        });
	        

	        
	        
	        
	        Button masterDown = new Button(new Button.ButtonStyle(downButton, downButton, downButton));
	        masterDown.setPosition((stage.getWidth() - masterDown.getPrefWidth()) / 2 -40,  (stage.getHeight() - masterDown.getPrefHeight())/2-60);
	        masterDown.addListener(new ClickListener() {

	            public void clicked(InputEvent event, float x, float y) {
	            	Assets.buttonPress.play(DuckGame.MasterVol);
	            	if(masterVol<=0){
	            		
	            	}else{
	            		masterVol-=5;
	            	}
	            	applySettings();

	            	levelSelectLabel.setText(Integer.toString(masterVol));
	            	stage.draw();
	            }
	        });
	        
	        stage.addActor(logoImage);
	        stage.addActor(masterUp);
	        stage.addActor(masterDown);
	        stage.addActor(levelSelectLabel);
	        
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
	        update();
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
	     * Checks for esc keypress and returns to main menu
	     */
	    public void update(){
	    	if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
	    		parent.showStartScreen();
	    		
	    	}
	    	
	    	stage.draw();
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
	    
	    
	    public void applySettings(){
	    	DuckGame.MasterVol=(float)masterVol/100;
	    	DuckGame.saveSettings();
	    }
	}










