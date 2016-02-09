package com.superduckinvaders.game;


import java.io.File;
import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.superduckinvaders.game.assets.Assets;

public class DuckGame extends Game {

	/**
	 * Volume values for the settings
	 */
	public static float MasterVol,SfxVol,MusicVol;
	
	/**
	 * level progress for the settings file
	 */
	public static String levelsComplete;
	
	/**
	 * required to efficiently dispose of sound files
	 */
	public static Sound currentMusic;
	
	
	/**
	 * required to efficiently dispose of settings Screen
	 */
	private SettingsScreen settingsScreen = null;
	
	/**
	 * The width of the game window.
	 */
	public static final int GAME_WIDTH = 1200;
	
	/**
	 * The height of the game window.
	 */
	public static final int GAME_HEIGHT = 720;
	
	/**
	 * stores whether the game is in a main game state
	 */
	public boolean onGameScreen = false;
	
	/**
	 * Stores the Screen displayed at the start of the game
	 */
	private StartScreen startScreen = null;
	
	/**
	 * Stores the Screen displayed when a level has begun
	 */
	private GameScreen gameScreen = null;
	
	/**
	 * Stores the Screen displayed for selecting levels
	 */
	private LevelSelectScreen levelSelectScreen = null;
	

	/**
	 * Stores the Screen displayed when a level has been won
	 */
	private WinScreen winScreen = null;
	
	/**
	 * Stores the Screen displayed when the player has lost the level
	 */
	private LoseScreen loseScreen = null;
	
	/**
	 * Stores the Screen diplayed when the player completes the game.
	 */
	private CompleteScreen completeScreen = null;
	
	/**
	 * Stores the current round that is being rendered using the gameScreen
	 */
	@SuppressWarnings("unused")
	private Round roundOne,roundTwo,roundThree,roundFour,roundFive,roundSix,roundSeven,roundEight;
	
	/**
	 * Combined score for all completed levels.
	 */
	private static int totalScore;

	/**
	 * Initialises the startScreen. Called by libGDX to set up the graphics.
	 */
	@Override
	public void create() {

		loadSettings();

		Assets.load();

		roundOne = new Round(this, Assets.levelOneMap);
		roundTwo = new Round(this, Assets.levelTwoMap);
		roundThree = new Round(this, Assets.levelThreeMap);
		roundFour = new Round(this, Assets.levelFourMap);
		roundFive = new Round(this, Assets.levelFiveMap);
		roundSix = new Round(this, Assets.levelSixMap);
		roundSeven = new Round(this, Assets.levelSevenMap);
		roundEight = new Round(this, Assets.levelEightMap);
		
		showStartScreen();
	}

	/**
	 * Sets the current screen to the startScreen.
	 */
	public void showStartScreen() {
		if (startScreen != null) {
			startScreen.dispose();
		}

		setScreen(startScreen = new StartScreen(this));
	}

	/**
	 * Sets the current screen to the levelSelectScreen
	 */
	public void showLevelSelectScreen() {
		if (levelSelectScreen != null) {
			levelSelectScreen.dispose();
		}

		setScreen(levelSelectScreen = new LevelSelectScreen(this));
	}


	/**
	 * Sets the current screen to the gameScreen.
	 * @param round The round to be displayed on the game screen
	 */
	public void showGameScreen(Round round) {
		if (gameScreen != null) {
			gameScreen.dispose();
		}
		onGameScreen = true;
		setScreen(gameScreen = new GameScreen(round));
	}

	/**
	 * Sets the current screen to the winScreen.
	 * @param score The final score the player had, to be displayed on the win screen
	 */
	public void showWinScreen(int score) {
		if (winScreen != null) {
			winScreen.dispose();
		}

		setScreen(winScreen = new WinScreen(this, score));
	}

	/**
	 * Sets the current screen to the loseScreen.
	 */
	public void showLoseScreen() {
		if (loseScreen != null) {
			loseScreen.dispose();
		}

		setScreen(loseScreen = new LoseScreen(this));
	}

	/**
	 * Returns the current round being displayed by the gameScreen
	 *
	 * @return Round being displayed by the GameScreen
	 */
	public Round getRound(){
		return gameScreen.getRound();
	}

	/**
	 * Called by libGDX to set up the graphics.
	 */
	@Override
	public void render() {
		super.render();
	}

	/**
	 * Returns the current GameScreen being displayed
	 *
	 * @return GameScreen being displayed
	 */
	public GameScreen getGameScreen() {
		return gameScreen;
	}

	/**
	 * Loads settings from an external file
	 */
	public static void loadSettings(){
		FileHandle handle;

		boolean fileExistance = Gdx.files.external("Saves/Settings.ini").exists();

		//if the file doesn't exit, make it
		if ( fileExistance == false){
			String path = Gdx.files.getExternalStoragePath() + "/Saves/Settings.ini";
			// Use relative path for Unix systems
			File f = new File(path);
			// Works for both Windows and Linux
			f.getParentFile().mkdirs(); 
			try {
				f.createNewFile();
				handle = Gdx.files.external("Saves/Settings.ini");
				//creates defualt settings file
				handle.writeString("levelsComplete=00000000\nMaster=1.0f\nSFX=1.0f\nMusic=1.0f\nScore=0", false);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		handle = Gdx.files.external("Saves/Settings.ini");
		String text = handle.readString();
		String lines[] = text.split("\\r?\\n");
		levelsComplete = lines[0].substring(15);
		MasterVol = Float.parseFloat(lines[1].substring(7));
		SfxVol = Float.parseFloat(lines[2].substring(4));
		MusicVol = Float.parseFloat(lines[3].substring(6));
		totalScore = Integer.parseInt(lines[4].substring(6));
	}

	/**
	 * saves the settings out to the settings.ini file
	 */
	public static void saveSettings(){
		FileHandle handle;
		handle = Gdx.files.external("Saves/Settings.ini");
		handle.writeString("levelsComplete="+ levelsComplete +
				"\nMaster="+Float.toString(MasterVol) + 
				"\nSFX="+Float.toString(SfxVol) + 
				"\nMusic="+Float.toString(MusicVol) + 
				"\nScore="+Integer.toString(totalScore), false);
	}

	/**
	 * creates a new save file with default values
	 */
	public static void newGame(){
		FileHandle handle;

		handle = Gdx.files.external("Saves/Settings.ini");
		//creates default settings file
		handle.writeString("levelsComplete=00000000" +
				"\nMaster="+Float.toString(MasterVol) + 
				"\nSFX="+Float.toString(SfxVol) + 
				"\nMusic="+Float.toString(MusicVol) + 
				"\nScore="+Integer.toString(totalScore), false);
	}

	/**
	 * plays music files based on volumes in settings
	 * @param music the music file to be played
	 */
	public static void playMusic(Sound music) {
		if (MusicVol != 0 && MasterVol != 0) {
			if (currentMusic != null) currentMusic.stop();
			currentMusic = music;
			currentMusic.loop(MasterVol*MusicVol);
		}
	}
	
	/**
	 * plays sfx files based on volumes in settings
	 * @param sound file to be played
	 * @param volumeMultiplier can be used to increase sound of particularly quiet files
	 */
	public static void playSoundEffect(Sound sound, float volumeMultiplier) {
		if (SfxVol != 0 && MasterVol != 0) {
			sound.play(MasterVol*SfxVol*volumeMultiplier);
		}
	}
	
	/**
	 * sets the current screen to the settings screen
	 */
	public void showSettingsScreen(){
		if (settingsScreen != null) {
			settingsScreen.dispose();
		}

		setScreen(settingsScreen = new SettingsScreen(this));
	}

	/**
	 * sets the current screen to the complete screen
	 */
	public void showCompleteScreen() {
		if (completeScreen != null) {
			completeScreen.dispose();
		}
		
		setScreen(completeScreen = new CompleteScreen(this, totalScore));
	}

	/**
	 * keeps a count of the total score
	 * @param score score to add
	 */
	public void addScoreToTotal(int score) {
		totalScore += score;
	}
}
