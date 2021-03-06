package com.superduckinvaders.game;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.superduckinvaders.game.assets.Assets;
/**
 * Screen which displays the different levels
 */

public class LevelSelectScreen implements Screen {

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
	public LevelSelectScreen(DuckGame parent) {
		this.parent = parent;
		
	}

	/**
	 * Shows this GameScreen. Called by libGDX to set up the graphics.
	 */
	@Override
	public void show() {
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		DuckGame.loadSettings();

		//Formatting for buttons and labels
		Drawable button = new TextureRegionDrawable(Assets.button);
		Drawable buttonLocked = new TextureRegionDrawable(Assets.buttonLocked);
		Label.LabelStyle white = new Label.LabelStyle(Assets.font, Color.WHITE);

		Drawable buttonBack = new TextureRegionDrawable(Assets.backButton);

		Label backLabel = new Label("Escape", white);
		backLabel.setPosition(85, Gdx.graphics.getHeight()-85);
		backLabel.setTouchable(Touchable.disabled);

		Button backButton = new Button(new Button.ButtonStyle(buttonBack, buttonBack, buttonBack));
		backButton.setPosition(40, Gdx.graphics.getHeight()-100);
		backButton.addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				DuckGame.playSoundEffect(Assets.buttonPress, 1);
				parent.showStartScreen();
			}
		});


		//button for level one
		Button playButtonOne = new Button(new Button.ButtonStyle(button, button, button));
		playButtonOne.setPosition((((stage.getWidth() - playButtonOne.getPrefWidth())) /2) -200,  (stage.getHeight() - playButtonOne.getPrefHeight())/2 +160);
		playButtonOne.addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				DuckGame.playSoundEffect(Assets.buttonPress, 1);
				parent.showGameScreen(new Round(parent, Assets.levelOneMap));
			}
		});

		//Label for level one
		Label playLabelOne = new Label("Level One", white);
		playLabelOne.setPosition((((stage.getWidth() - playLabelOne.getPrefWidth())) /2) -200, (stage.getHeight() - playLabelOne.getPrefHeight())/2 +160);
		playLabelOne.setTouchable(Touchable.disabled);



		//button for level two
		Button playButtonTwo;
		if (DuckGame.levelsComplete.substring(0, 1).equals("0")){
			playButtonTwo = new Button(new Button.ButtonStyle(buttonLocked, buttonLocked, buttonLocked));
			playButtonTwo.setPosition((((stage.getWidth() - playButtonTwo.getPrefWidth())) /2) -200,  (stage.getHeight() - playButtonTwo.getPrefHeight())/2+100);
			playButtonTwo.addListener(new ClickListener() {

				public void clicked(InputEvent event, float x, float y) {
					DuckGame.playSoundEffect(Assets.buttonPress, 1);
				}
			});
		}
		else {
			playButtonTwo = new Button(new Button.ButtonStyle(button, button, button));
			playButtonTwo.setPosition((((stage.getWidth() - playButtonTwo.getPrefWidth())) /2) -200,  (stage.getHeight() - playButtonTwo.getPrefHeight())/2+100);
			playButtonTwo.addListener(new ClickListener() {

				public void clicked(InputEvent event, float x, float y) {
					DuckGame.playSoundEffect(Assets.buttonPress, 1);
					parent.showGameScreen(new Round(parent, Assets.levelTwoMap));
				}
			});
		}



		//Label for level two
		Label playLabelTwo = new Label("Level Two", white);
		playLabelTwo.setPosition((((stage.getWidth() - playLabelTwo.getPrefWidth())) /2) -200, (stage.getHeight() - playLabelTwo.getPrefHeight())/2+100);
		playLabelTwo.setTouchable(Touchable.disabled);

		//button for level three
		Button playButtonThree;
		if (DuckGame.levelsComplete.substring(1, 2).equals("0")){
			playButtonThree = new Button(new Button.ButtonStyle(buttonLocked, buttonLocked, buttonLocked));
			playButtonThree.setPosition((((stage.getWidth() - playButtonThree.getPrefWidth())) /2) -200,  (stage.getHeight() - playButtonThree.getPrefHeight())/2+40);
			playButtonThree.addListener(new ClickListener() {

				public void clicked(InputEvent event, float x, float y) {
					DuckGame.playSoundEffect(Assets.buttonPress, 1);
				}
			});
		}
		else {
			playButtonThree = new Button(new Button.ButtonStyle(button, button, button));
			playButtonThree.setPosition((((stage.getWidth() - playButtonThree.getPrefWidth())) /2) -200,  (stage.getHeight() - playButtonThree.getPrefHeight())/2+40);
			playButtonThree.addListener(new ClickListener() {

				public void clicked(InputEvent event, float x, float y) {
					DuckGame.playSoundEffect(Assets.buttonPress, 1);
					parent.showGameScreen(new Round(parent, Assets.levelThreeMap));
				}
			});
		}

		//Label for level three
		Label playLabelThree = new Label("Level Three", white);
		playLabelThree.setPosition((((stage.getWidth() - playLabelThree.getPrefWidth())) /2) -200, (stage.getHeight() - playLabelThree.getPrefHeight())/2+40);
		playLabelThree.setTouchable(Touchable.disabled);


		//button for level four
		Button playButtonFour;
		if (DuckGame.levelsComplete.substring(2, 3).equals("0")){
			playButtonFour = new Button(new Button.ButtonStyle(buttonLocked, buttonLocked, buttonLocked));
			playButtonFour.setPosition((((stage.getWidth() - playButtonFour.getPrefWidth())) /2) -200,  (stage.getHeight() - playButtonFour.getPrefHeight())/2+-20);
			playButtonFour.addListener(new ClickListener() {

				public void clicked(InputEvent event, float x, float y) {
					DuckGame.playSoundEffect(Assets.buttonPress, 1);
				}
			});
		}
		else {
			playButtonFour = new Button(new Button.ButtonStyle(button, button, button));
			playButtonFour.setPosition((((stage.getWidth() - playButtonFour.getPrefWidth())) /2) -200,  (stage.getHeight() - playButtonFour.getPrefHeight())/2+-20);
			playButtonFour.addListener(new ClickListener() {

				public void clicked(InputEvent event, float x, float y) {
					DuckGame.playSoundEffect(Assets.buttonPress, 1);
					parent.showGameScreen(new Round(parent, Assets.levelFourMap));
				}
			});
		}

		//Label for level four
		Label playLabelFour = new Label("Level Four", white);
		playLabelFour.setPosition((((stage.getWidth() - playLabelFour.getPrefWidth())) /2) -200, (stage.getHeight() - playLabelFour.getPrefHeight())/2-20);
		playLabelFour.setTouchable(Touchable.disabled);



		//button for level Five
		Button playButtonFive;
		if (DuckGame.levelsComplete.substring(3, 4).equals("0")){
			playButtonFive = new Button(new Button.ButtonStyle(buttonLocked, buttonLocked, buttonLocked));
			playButtonFive.setPosition((((stage.getWidth() - playButtonFive.getPrefWidth())) /2) +200,  (stage.getHeight() - playButtonFive.getPrefHeight())/2+160);
			playButtonFive.addListener(new ClickListener() {

				public void clicked(InputEvent event, float x, float y) {
					DuckGame.playSoundEffect(Assets.buttonPress, 1);
				}
			});
		}
		else {
			playButtonFive = new Button(new Button.ButtonStyle(button, button, button));
			playButtonFive.setPosition((((stage.getWidth() - playButtonFive.getPrefWidth())) /2) +200,  (stage.getHeight() - playButtonFive.getPrefHeight())/2+160);
			playButtonFive.addListener(new ClickListener() {

				public void clicked(InputEvent event, float x, float y) {
					DuckGame.playSoundEffect(Assets.buttonPress, 1);
					parent.showGameScreen(new Round(parent, Assets.levelFiveMap));
				}
			});
		}

		//Label for level Five
		Label playLabelFive = new Label("Level Five", white);
		playLabelFive.setPosition((((stage.getWidth() - playLabelFive.getPrefWidth())) /2) +200, (stage.getHeight() - playLabelFive.getPrefHeight())/2+160);
		playLabelFive.setTouchable(Touchable.disabled);


		//button for level Six
		Button playButtonSix;
		if (DuckGame.levelsComplete.substring(4, 5).equals("0")){
			playButtonSix = new Button(new Button.ButtonStyle(buttonLocked, buttonLocked, buttonLocked));
			playButtonSix.setPosition((((stage.getWidth() - playButtonSix.getPrefWidth())) /2) +200,  (stage.getHeight() - playButtonSix.getPrefHeight())/2+100);
			playButtonSix.addListener(new ClickListener() {

				public void clicked(InputEvent event, float x, float y) {
					DuckGame.playSoundEffect(Assets.buttonPress, 1);
				}
			});
		}
		else {
			playButtonSix = new Button(new Button.ButtonStyle(button, button, button));
			playButtonSix.setPosition((((stage.getWidth() - playButtonSix.getPrefWidth())) /2) +200,  (stage.getHeight() - playButtonSix.getPrefHeight())/2+100);
			playButtonSix.addListener(new ClickListener() {

				public void clicked(InputEvent event, float x, float y) {
					DuckGame.playSoundEffect(Assets.buttonPress, 1);
					parent.showGameScreen(new Round(parent, Assets.levelSixMap));
				}
			});
		}

		//Label for level Six
		Label playLabelSix = new Label("Level Six", white);
		playLabelSix.setPosition((((stage.getWidth() - playLabelSix.getPrefWidth())) /2) +200, (stage.getHeight() - playLabelSix.getPrefHeight())/2+100);
		playLabelSix.setTouchable(Touchable.disabled);


		//button for level Seven
		Button playButtonSeven;
		if (DuckGame.levelsComplete.substring(5, 6).equals("0")){
			playButtonSeven = new Button(new Button.ButtonStyle(buttonLocked, buttonLocked, buttonLocked));
			playButtonSeven.setPosition((((stage.getWidth() - playButtonSeven.getPrefWidth())) /2) +200,  (stage.getHeight() - playButtonSeven.getPrefHeight())/2+40);
			playButtonSeven.addListener(new ClickListener() {

				public void clicked(InputEvent event, float x, float y) {
					DuckGame.playSoundEffect(Assets.buttonPress, 1);
				}
			});
		}
		else {
			playButtonSeven = new Button(new Button.ButtonStyle(button, button, button));
			playButtonSeven.setPosition((((stage.getWidth() - playButtonSeven.getPrefWidth())) /2) +200,  (stage.getHeight() - playButtonSeven.getPrefHeight())/2+40);
			playButtonSeven.addListener(new ClickListener() {

				public void clicked(InputEvent event, float x, float y) {
					DuckGame.playSoundEffect(Assets.buttonPress, 1);
					parent.showGameScreen(new Round(parent, Assets.levelSevenMap));
				}
			});
		}

		//Label for level Seven
		Label playLabelSeven = new Label("Level Seven", white);
		playLabelSeven.setPosition((((stage.getWidth() - playLabelSeven.getPrefWidth())) /2) +200, (stage.getHeight() - playLabelSeven.getPrefHeight())/2+40);
		playLabelSeven.setTouchable(Touchable.disabled);


		//button for level Eight
		Button playButtonEight;
		if (DuckGame.levelsComplete.substring(6,7).equals("0")){
			playButtonEight = new Button(new Button.ButtonStyle(buttonLocked, buttonLocked, buttonLocked));
			playButtonEight.setPosition((((stage.getWidth() - playButtonEight.getPrefWidth())) /2) +200,  (stage.getHeight() - playButtonEight.getPrefHeight())/2-20);
			playButtonEight.addListener(new ClickListener() {

				public void clicked(InputEvent event, float x, float y) {
					DuckGame.playSoundEffect(Assets.buttonPress, 1);
				}
			});
		}
		else {
			playButtonEight = new Button(new Button.ButtonStyle(button, button, button));
			playButtonEight.setPosition((((stage.getWidth() - playButtonEight.getPrefWidth())) /2) +200,  (stage.getHeight() - playButtonEight.getPrefHeight())/2-20);
			playButtonEight.addListener(new ClickListener() {

				public void clicked(InputEvent event, float x, float y) {
					DuckGame.playSoundEffect(Assets.buttonPress, 1);
					parent.showGameScreen(new Round(parent, Assets.levelEightMap));
				}
			});
		}

		//Label for level Eight
		Label playLabelEight = new Label("Level Eight", white);
		playLabelEight.setPosition((((stage.getWidth() - playLabelEight.getPrefWidth())) /2) +200, (stage.getHeight() - playLabelEight.getPrefHeight())/2-20);
		playLabelEight.setTouchable(Touchable.disabled);


		stage.addActor(backButton);
		stage.addActor(backLabel);

		stage.addActor(playButtonOne);
		stage.addActor(playLabelOne);
		stage.addActor(playButtonTwo);
		stage.addActor(playLabelTwo);
		stage.addActor(playButtonThree);
		stage.addActor(playLabelThree);
		stage.addActor(playButtonFour);
		stage.addActor(playLabelFour);
		stage.addActor(playButtonFive);
		stage.addActor(playLabelFive);
		stage.addActor(playButtonSix);
		stage.addActor(playLabelSix);
		stage.addActor(playButtonSeven);
		stage.addActor(playLabelSeven);
		stage.addActor(playButtonEight);
		stage.addActor(playLabelEight);

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


