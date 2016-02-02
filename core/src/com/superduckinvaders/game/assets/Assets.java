package com.superduckinvaders.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

/**
 * Responsible for loading game assets.
 */
public class Assets {

	
	public static TextureRegion downButton;

	public static TextureRegion upButton;
    /**
     *  Player texture sets for normal, flying and swimming.
     */
    public static TextureSet playerNormal, playerFlying, playerSwimming;

    /**
     *  Bad guy texture set.
     */
    public static TextureSet badGuyNormal;

    /**
     *  Texture for Projectile.
     */
    public static TextureRegion projectile;
    
    /**
     *  Textures for Hearts
     */
    public static TextureRegion heartFull, heartHalf, heartEmpty;

    /**
     *  Textures for stamina.
     */
    public static TextureRegion staminaFull, staminaEmpty;

    /**
     * Texture for powerup backboard.
     */
    public static Texture powerupBackboard;

    /**
     *  Animation for explosion.
     */
    public static Animation explosionAnimation;

    /**
     *  Tile map for level one.
     */
    public static TiledMap levelOneMap,levelTwoMap,levelThreeMap,levelFourMap,levelFiveMap,levelSixMap,levelSevenMap,levelEightMap;

    /**
     *  The font for the UI.
     */
    public static BitmapFont font;

    /**
     * The texture for the button.
     */
    public static TextureRegion button;

    /**
     *  Textures for floor items.
     */
    public static TextureRegion floorItemGun, floorItemSpeed, floorItemInvulnerable, floorItemScore, floorItemFireRate;

    /**
     *  Texture for objective flag.
     */
    public static TextureRegion flag;

    /**
     *  Texture for the game logo.
     */
    public static TextureRegion logo;
  
    /**
     * music for the game
     */
    public static Sound music;
    
    /**
     * texture for buttonLocked
     */
    public static TextureRegion buttonLocked;
    /**
     * button noise
     */
    public static Sound buttonPress;
    
    /**
     * collectable noise
     */
    public static Sound collect;
    
    /**
     * gun pickup noise
     */
    public static Sound gunPickup;
    
    /**
     * gunshot noise
     */
    public static Sound shot;
    
    /**
     * levelComplete noise
     */
    public static Sound levelComplete;
    
    /**
     * gameOver noise
     */
    public static Sound gameOver;
    /**
     * Responsible for loading maps.
     */
    private static TmxMapLoader mapLoader = new TmxMapLoader();

    /**
     * Loads all assets.
     */
    public static void load() {
        loadPlayerTextureSets();
        loadBadGuyTextureSet();
        loadFloorItems();

        projectile = new TextureRegion(loadTexture("textures/projectile.png"));

        explosionAnimation = loadAnimation("textures/explosion.png", 2, 32, 0.3f);

        music = loadSound("sound/levelMusic.mp3");
        buttonPress = loadSound("sound/button.mp3");
        collect = loadSound("sound/collect.mp3");
        gunPickup = loadSound("sound/gun.mp3");
        shot = loadSound("sound/shot.mp3");
        levelComplete = loadSound("sound/levelComplete.mp3");
        gameOver = loadSound("sound/gameOver.mp3");
        
        //load multiple levels
        levelOneMap = loadTiledMap("maps/map.tmx");
        levelTwoMap = loadTiledMap("maps/map.tmx");
        levelThreeMap = loadTiledMap("maps/map.tmx");
        levelFourMap = loadTiledMap("maps/map.tmx");
        levelFiveMap = loadTiledMap("maps/map.tmx");
        levelSixMap = loadTiledMap("maps/map.tmx");
        levelSevenMap = loadTiledMap("maps/map.tmx");
        levelEightMap = loadTiledMap("maps/map.tmx");

        font = loadFont("font/gamefont.fnt", "font/gamefont.png");

        Texture hearts = loadTexture("textures/hearts.png");
        heartFull = new TextureRegion(hearts, 0, 0, 32, 28);
        heartHalf = new TextureRegion(hearts, 32, 0, 32, 28);
        heartEmpty = new TextureRegion(hearts, 64, 0, 32, 28);

        Texture stamina = loadTexture("textures/stamina.png");
        staminaFull = new TextureRegion(stamina, 0, 0, stamina.getWidth(), stamina.getHeight()/2);
        staminaEmpty = new TextureRegion(stamina, 0, stamina.getHeight()/2, stamina.getWidth(), stamina.getHeight()/2);

        powerupBackboard = new Texture("textures/powerupBackboard.png");
        button = new TextureRegion(loadTexture("textures/button.png"));
        buttonLocked = new TextureRegion(loadTexture("textures/buttonLocked.png"));
        downButton = new TextureRegion(loadTexture("textures/down.png"));
        upButton = new TextureRegion(loadTexture("textures/down.png"));
        upButton.flip(true, false);

        flag = new TextureRegion(loadTexture("textures/flag.png"));
        logo = new TextureRegion(loadTexture("textures/logo.png"));
    }

    /**
     * Loads assets relating to the player in the normal state.
     * If you change the player texture size, be sure to change the values here.
     */
    private static void loadPlayerTextureSets() {
        // Load idle texture map.
        Texture playerIdle = loadTexture("textures/player_idle.png");

        // Cut idle textures from texture map.
        TextureRegion front = new TextureRegion(playerIdle, 0, 0, 14, 18);
        TextureRegion back = new TextureRegion(playerIdle, 14, 0, 14, 18);
        TextureRegion left = new TextureRegion(playerIdle, 28, 0, 14, 18);
        TextureRegion right = new TextureRegion(playerIdle, 42, 0, 14, 18);

        //Cut idle swim textures from the map.
        
        // Load walking animations.
        Animation walkingFront = loadAnimation("textures/player_walking_front.png", 4, 12, 0.2f);
        Animation walkingBack = loadAnimation("textures/player_walking_back.png", 4, 12, 0.2f);
        Animation walkingLeft = loadAnimation("textures/player_walking_left.png", 4, 14, 0.2f);
        Animation walkingRight = loadAnimation("textures/player_walking_right.png", 4, 14, 0.2f);
        
        //Load swimming animations.
        Animation swimmingFront = loadAnimation("textures/player_swimming_front.png", 2, 14, 0.2f);
        Animation swimmingBack = loadAnimation("textures/player_swimming_back.png", 2, 14, 0.2f);
        Animation swimmingLeft = loadAnimation("textures/player_swimming_left.png", 2, 14, 0.2f);
        Animation swimmingRight = loadAnimation("textures/player_swimming_right.png", 2, 14, 0.2f);
        Texture swimmingIdle = new Texture("textures/player_swimming_idle.png");
        TextureRegion swimmingIdleFront = new TextureRegion(swimmingIdle, 0*14, 0, 14, 17);
        TextureRegion swimmingIdleBack = new TextureRegion(swimmingIdle, 1*14, 0, 14, 17);
        TextureRegion swimmingIdleLeft = new TextureRegion(swimmingIdle, 2*14, 0, 14, 17);
        TextureRegion swimmingIdleRight = new TextureRegion(swimmingIdle, 3*14, 0, 14, 17);

        // Load flying animations.
        Animation flyingFront = loadAnimation("textures/player_flying_front.png", 2, 18, 0.2f);
        Animation flyingBack = loadAnimation("textures/player_flying_back.png", 2, 18, 0.2f);
        Animation flyingLeft = loadAnimation("textures/player_flying_left.png", 2, 21, 0.2f);
        Animation flyingRight = loadAnimation("textures/player_flying_right.png", 2, 21, 0.2f);

        playerNormal = new TextureSet(front, back, left, right, walkingFront, walkingBack, walkingLeft, walkingRight);
        playerFlying = new TextureSet(front, back, left, right, flyingFront, flyingBack, flyingLeft, flyingRight);
        playerSwimming = new TextureSet(swimmingIdleFront, swimmingIdleBack, swimmingIdleLeft, swimmingIdleRight,
        									swimmingFront, swimmingBack, swimmingLeft, swimmingRight);
    }

    /**
     * Loads the textures from the bad guy textures file.
     */
    private static void loadBadGuyTextureSet() {
        // Load idle texture map.
        Texture badGuyIdle = loadTexture("textures/badguy_idle.png");

        // Cut idle textures from texture map.
        TextureRegion front = new TextureRegion(badGuyIdle, 0, 0, 21, 24);
        TextureRegion back = new TextureRegion(badGuyIdle, 21, 0, 21, 24);
        TextureRegion left = new TextureRegion(badGuyIdle, 42, 0, 21, 24);
        TextureRegion right = new TextureRegion(badGuyIdle, 63, 0, 21, 24);

        // Load walking animations.
        Animation walkingFront = loadAnimation("textures/badguy_walking_front.png", 4, 21, 0.2f);
        Animation walkingBack = loadAnimation("textures/badguy_walking_back.png", 4, 21, 0.2f);
        Animation walkingLeft = loadAnimation("textures/badguy_walking_left.png", 4, 16, 0.2f);
        Animation walkingRight = loadAnimation("textures/badguy_walking_right.png", 4, 16, 0.2f);

        badGuyNormal = new TextureSet(front, back, left, right, walkingFront, walkingBack, walkingLeft, walkingRight);
    }

    /**
     * Loads the texture from the floor items file.
     */
    public static void loadFloorItems() {
        Texture floorItems = loadTexture("textures/floor_items.png");

        floorItemGun = new TextureRegion(floorItems, 0, 0, 15, 15);
        floorItemSpeed = new TextureRegion(floorItems, 15, 0, 15, 15);
        floorItemInvulnerable = new TextureRegion(floorItems, 30, 0, 15, 15);
        floorItemScore = new TextureRegion(floorItems, 45, 0, 15, 15);
        floorItemFireRate = new TextureRegion(floorItems, 60, 0, 15, 15);
    }

    /**
     * Loads the texture from the specified file.
     *
     * @param file the file to load from
     * @return the texture
     */
    public static Texture loadTexture(String file) {
        return new Texture(Gdx.files.internal(file));
    }

    /**
     * Loads the tile map from the specifed file.
     *
     * @param file the file to load from
     * @return the tile map
     */
    public static TiledMap loadTiledMap(String file) {
        return mapLoader.load(file);
    }

    /**
     * Loads the animation from the specified file.
     *
     * @param file          the file to load from
     * @param count         how many frames are in the file
     * @param frameWidth    how wide each frame is in the file
     * @param frameDuration how long each frame should be shown for in seconds
     * @return the animation
     */
    public static Animation loadAnimation(String file, int count, int frameWidth, float frameDuration) {
        Texture texture = loadTexture(file);
        Array<TextureRegion> keyFrames = new Array<TextureRegion>();

        for (int i = 0; i < count; i++) {
            keyFrames.add(new TextureRegion(texture, i * frameWidth, 0, frameWidth, texture.getHeight()));
        }

        return new Animation(frameDuration, keyFrames);
    }

    /**
     * Loads the bitmap font from the specified files.
     *
     * @param fontFile  the file containing information about the glyphs stored on the image file
     * @param imageFile the file containing the actual glyphs
     * @return the bitmap font
     */
    public static BitmapFont loadFont(String fontFile, String imageFile) {
        return new BitmapFont(Gdx.files.internal(fontFile), Gdx.files.internal(imageFile), false);
    }

    /**
     * Loads sound files based on a given path
     * @param path the path of the file
     * @return the gdx sound object
     */
    public static Sound loadSound(String path){
    	return Gdx.audio.newSound(Gdx.files.internal(path));
    }
}
