package com.superduckinvaders.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.superduckinvaders.game.assets.Assets;

public class DuckGame extends Game {
	
	public static float MasterVol;
	public static float SfxVol;
	public static float MusicVol;
	public static String levelsComplete;
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
    private LevelSelectScreen levelSelectScreen =null;
    
    /**
     * Stores the Screen displayed when a level has been won
     */
    private WinScreen winScreen = null;
    /**
     * Stores the Screen displayed when the player has lost the level
     */
    private LoseScreen loseScreen = null;
    /**
     * Stores the current round that is being rendered using the gameScreen
     */
    private Round roundOne,roundTwo,roundThree,roundFour,roundFive,roundSix,roundSeven,roundEight;

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
    	
    	String extRoot = Gdx.files.getExternalStoragePath();
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
    	    	handle.writeString("levelsComplete=00000000\nMaster=1.0f\nSFX=1.0f\nMusic=1.0f", false);
 
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
    }
    
    public static void saveSettings(){
    	FileHandle handle;
    	handle = Gdx.files.external("Saves/Settings.ini");
    	handle.writeString("levelsComplete="+levelsComplete+"\nMaster="+Float.toString(MasterVol)+"\nSFX="+Float.toString(SfxVol)+"\nMusic="+Float.toString(MusicVol),false);
    }
    
    public static void newGame(){
    	FileHandle handle;
    	
    	handle = Gdx.files.external("Saves/Settings.ini");
		//creates defualt settings file
    	handle.writeString("levelsComplete=00000000\nMaster="+Float.toString(MasterVol)+"\nSFX="+Float.toString(SfxVol)+"\nMusic="+Float.toString(MusicVol),false);
    }
    
    public void showSettingsScreen(){
    	if (settingsScreen != null) {
            settingsScreen.dispose();
        }

        setScreen(settingsScreen = new SettingsScreen(this));
        }
}
