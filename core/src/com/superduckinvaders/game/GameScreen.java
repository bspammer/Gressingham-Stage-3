package com.superduckinvaders.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.superduckinvaders.game.assets.Assets;
import com.superduckinvaders.game.entity.Entity;
import com.superduckinvaders.game.entity.Player;
import com.superduckinvaders.game.entity.item.Powerup;
import com.superduckinvaders.game.objective.AnimatedText;

/**
 * Screen for interaction with the game.
 */
public class GameScreen implements Screen {

	/**
	 * Draw map gridlines for debug purposes.
	 */
	private boolean gridlines = false;

	/**
	 * The game camera.
	 */
	private OrthographicCamera gameCam;

	/**
	 * The renderer for the tile map.
	 */
	private OrthogonalTiledMapRenderer mapRenderer;

	/**
	 * The sprite batches for rendering.
	 */
	private SpriteBatch spriteBatch;

	/**
	 * The Round this GameScreen renders.
	 */
	private Round round;

	/**
	 * The shader program used to render the radial buffs
	 */
	private ShaderProgram shader = new ShaderProgram(Gdx.files.internal("shaders/powerupShader.vsh"), Gdx.files.internal("shaders/powerupShader.fsh"));

	/**
	 * Stores current window width.
	 * Useful for positioning UI elements.
	 */
	private int prevWindowWidth;

	/**
	 * Stores current window height.
	 * Useful for positioning UI elements.
	 */
	private int prevWindowHeight;
	
	/**
	 * Minimap for the current screen
	 */
	private Minimap miniMap;
	
	/**
	 * List of AnimatedText objects to draw
	 */
	private ArrayList<AnimatedText> animatedTextToDraw = new ArrayList<AnimatedText>();

	/**
	 * Initialises this GameScreen for the specified round.
	 *
	 * @param round the round to be displayed
	 */
	public GameScreen(Round round) {
		this.round = round;
		//Must match the desktoplauncher dimensions
		this.prevWindowWidth = 1280;
		this.prevWindowHeight = 720;		
		
		DuckGame.playMusic(Assets.music);
	}

	/**
	 * Converts screen coordinates to world coordinates.
	 *
	 * @param x the x coordinate on screen
	 * @param y the y coordinate on screen
	 * @return a Vector3 containing the world coordinates (x and y)
	 */
	public Vector3 unproject(int x, int y) {
		return gameCam.unproject(new Vector3(x, y, 0));
	}

	/**
	 * @return the Round currently on this GameScreen
	 */
	public Round getRound() {
		return round;
	}

	/**
	 * Shows this GameScreen. Called by libGDX to set up the graphics.
	 */
	@Override
	public void show() {
		Gdx.input.setInputProcessor(null);

		gameCam = new OrthographicCamera(DuckGame.GAME_WIDTH, DuckGame.GAME_HEIGHT);
		gameCam.zoom -= 0.5;

		spriteBatch = new SpriteBatch();
		
		mapRenderer = new OrthogonalTiledMapRenderer(round.getMap(), spriteBatch);
		this.miniMap = new Minimap(round, spriteBatch);
	}
	
	/**
	 * Creates and adds an AnimatedText object to the list of AnimatedTexts to be drawn
	 * @param text The text for the AnimatedText
	 * @param x The x position for the AnimatedText
	 * @param y The initial y position (will change over its lifetime) for the AnimatedText
	 */
	public void addAnimatedText(String text, float x, float y, Color color) {
		AnimatedText animatedText = new AnimatedText(text, x, y, color);
		boolean added = false;
		for (int i=0; i<animatedTextToDraw.size(); i++) {
			// Overwrite null entries
			if (animatedTextToDraw.get(i) == null) {
				animatedTextToDraw.set(i, animatedText);
				added = true;
				break;
			}
		}
		if (!added) {
			animatedTextToDraw.add(animatedText);
		}
	}

	/**
	 * Main game drawing loop.
	 *
	 * @param delta how much time has passed since the last update
	 */
	@Override
	public void render(float delta) {
		//update the round game logic.
		round.update(delta);
		
		//clear the screen with absolute transparency.
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		handlePlayerCamera();

		//set batch to draw what gameCam sees.
		spriteBatch.setProjectionMatrix(gameCam.combined);
		spriteBatch.begin();
		
		// Render base and collision layers.
		mapRenderer.setView(gameCam);
		mapRenderer.renderTileLayer(round.getBaseLayer());
		mapRenderer.renderTileLayer(round.getCollisionLayer());

		// Render randomly-chosen obstacles layer.
		if (round.getObstaclesLayer() != null) {
			mapRenderer.renderTileLayer(round.getObstaclesLayer());
		}

		//Render water layer
		mapRenderer.renderTileLayer((TiledMapTileLayer) round.getMap().getLayers().get("Water"));

		// Draw all entities.
		for (Entity entity : round.getEntities()) {
			entity.render(spriteBatch);
		}
		
		// Draw animated text
		drawAnimatedText(delta);

		// Render overhang layer (draws over the player).
		if (round.getOverhangLayer() != null) {
			mapRenderer.renderTileLayer(round.getOverhangLayer());
		}
		
		//set batch to draw UI
		Matrix4 uiMatrix = gameCam.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, DuckGame.GAME_WIDTH, DuckGame.GAME_HEIGHT);
		spriteBatch.setProjectionMatrix(uiMatrix);
		
		//draw main UI elements
		drawPlayerObjectiveAndScore();
		drawPlayerStaminaBar();
		drawPlayerHearts();
		if (Player.minimapOn) {
			miniMap.drawMinimap(prevWindowWidth,prevWindowHeight);
		}

		spriteBatch.end();

		//draw custom powerup icon timers
		drawPlayerPowerupTimers();

		//debugging purposes
		if (gridlines) {
			drawGridlines();
		}
	}

	/**
	 * Handles camera following the player and clamps the camera to the edges of the game maps.
	 */
	private void handlePlayerCamera() {
		// Centre the camera on the player.
		double cameraX = round.getPlayer().getX() + round.getPlayer().getWidth() / 2;
		double cameraY = round.getPlayer().getY() + round.getPlayer().getHeight() / 2;
		float cameraWidth = gameCam.viewportWidth;
		float cameraHeight = gameCam.viewportHeight;
		// Clamp camera position to edges of the map
		if (cameraX - cameraWidth/4 < 0) {
			cameraX = cameraWidth/4;
		}
		if (cameraX + cameraWidth/4 > round.getMapWidth()) {
			cameraX = round.getMapWidth() - cameraWidth/4;
		}
		if (cameraY - cameraHeight/4 < 0) {
			cameraY = cameraHeight/4;
		}
		if (cameraY + cameraHeight/4 > round.getMapHeight()) {
			cameraY = round.getMapHeight() - cameraHeight/4;
		}
		gameCam.position.set((int) cameraX, (int) cameraY, 0);
		gameCam.update();
	}

	/**
	 * Draws the Tiled gridlines for debugging purposes.
	 */
	private void drawGridlines() {
		TiledMap map = round.getMap();
		ShapeRenderer sr = new ShapeRenderer();
		int tileWidth = map.getProperties().get("tilewidth", Integer.class), tileHeight = map.getProperties().get("tileheight", Integer.class);
		int mapWidth = map.getProperties().get("width", Integer.class) * tileWidth, mapHeight = map.getProperties().get("height", Integer.class) * tileHeight;
		sr.setProjectionMatrix(gameCam.combined);
		sr.begin(ShapeType.Line);
		for(int y = 0; y < mapWidth; y += tileWidth)
			sr.line(y, 0, y, mapHeight);
		for(int y = 0; y < mapHeight; y += tileHeight)
			sr.line(0, y, mapWidth, y);
		sr.end();
	}
	

	/**
	 * Draws the radial countdown timers for the player acquired powerups.
	 */
	private void drawPlayerPowerupTimers() {
		SpriteBatch powerupBatch = new SpriteBatch();
		ShaderProgram.pedantic = false;
		if (shader.getLog().length() > 0)
			shader.getLog();
		powerupBatch.setShader(shader);
		powerupBatch.begin();

		//Draw powerup buffs
		int powerupCount = 0;
		for (Player.Powerup powerup : Player.Powerup.values()) {
			if (round.getPlayer().powerupIsActive(powerup)) {
				TextureRegion powerupTexture = new TextureRegion(Player.Powerup.getTextureForPowerup(powerup));
				int powerupDrawScale = 3;
				float powerupWidth = powerupTexture.getRegionWidth();
				float powerupHeight = powerupTexture.getRegionHeight();
				float powerupX = Gdx.graphics.getWidth() - 62 - powerupWidth*powerupDrawScale*powerupCount*1.2f;
				float powerupY = 49;

				double proportionTimeLeft = round.getPlayer().getPowerupTimeRemaining(powerup)/Powerup.getMaxPowerupTime(powerup);
				shader.setUniformf("u_centre", powerupX + 2 + (powerupWidth*powerupDrawScale)/2, powerupY - 1 + (powerupHeight*powerupDrawScale)/2);
				shader.setUniformf("u_powerupWidth", powerupWidth*powerupDrawScale);
				//Starts at 0 and increases to 2*pi
				shader.setUniformf("u_currentAngle", (float) (2*Math.PI - (proportionTimeLeft*2*Math.PI)));
				powerupBatch.draw(powerupTexture, powerupX, powerupY, powerupWidth*powerupDrawScale, powerupHeight*powerupDrawScale);
				powerupBatch.flush();
				powerupCount += 1;
			}
		}
		powerupBatch.end();
		powerupBatch.dispose();
	}



	/**
	 * Draws the players health as three hearts.
	 */
	private void drawPlayerHearts() {
		int x = 0;
		while(x < round.getPlayer().getMaximumHealth()) {
			if(x+2 <= round.getPlayer().getCurrentHealth())
				spriteBatch.draw(Assets.heartFull, x * 18 + 10, 10);
			else if(x+1 <= round.getPlayer().getCurrentHealth())
				spriteBatch.draw(Assets.heartHalf, x * 18 + 10, 10);
			else
				spriteBatch.draw(Assets.heartEmpty, x * 18 + 10, 10);
			x += 2;
		}
	}

	/**
	 * Draws the players stamina bar.
	 */
	private void drawPlayerStaminaBar() {
		// Draw stamina bar (for flight);
		int staminaBarWidth = Assets.staminaEmpty.getRegionWidth();
		int staminaBarHeight = Assets.staminaEmpty.getRegionHeight();

		//calculate offset for positioning of stamina bar
		int resizeOffset = (Gdx.graphics.getWidth() - prevWindowWidth ) == 0 ? staminaBarWidth : (staminaBarWidth + (Gdx.graphics.getWidth() - prevWindowWidth));

		int staminaBarX = Gdx.graphics.getWidth() - resizeOffset - 85;
		int staminaBarY = 10;
		// TODO Stamina bar does not draw in correct position on window resize
		spriteBatch.draw(Assets.staminaEmpty, staminaBarX, staminaBarY, staminaBarWidth, staminaBarHeight);
		if (round.getPlayer().getFlyingTimer() > 0) {
			Assets.staminaFull.setRegionWidth((int) Math.max(0, Math.min(staminaBarWidth, round.getPlayer().getFlyingTimer() / Player.PLAYER_FLIGHT_COOLDOWN * staminaBarWidth)));
		} else {
			Assets.staminaFull.setRegionWidth(0);
		}
		spriteBatch.draw(Assets.staminaFull, staminaBarX, staminaBarY);
	}

	/**
	 * Draws the players current objective and round score.
	 */
	private void drawPlayerObjectiveAndScore() {
		Assets.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		Assets.font.draw(spriteBatch, "Objective: " + round.getObjective().getObjectiveString(), 10, 100);
		Assets.font.draw(spriteBatch, "Score: " + round.getPlayer().getScore(), 10,80);
		Assets.font.draw(spriteBatch, Gdx.graphics.getFramesPerSecond() + " FPS", 10, 60);
	}
	
	/**
	 * Draw each individual animated text to the SpriteBatch
	 * @param delta Time since last render
	 */
	private void drawAnimatedText(float delta) {
		for (int i=0; i<animatedTextToDraw.size(); i++) {
			AnimatedText animatedText = animatedTextToDraw.get(i);
			if (animatedText == null) continue;
			boolean timerNotFinished = animatedText.draw(spriteBatch, delta);
			// If timer on text is finished set it to null to allow it to be picked up by the garbage collector
			if (!timerNotFinished) animatedTextToDraw.set(i, null);
		}
	}

	/**
	 * Not used.
	 */
	@Override
	public void resize(int width, int height) {
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
	 * Called to dispose libGDX objects used by this GameScreen.
	 */
	@Override
	public void dispose() {
		Assets.music.stop();
		mapRenderer.dispose();
		spriteBatch.dispose();
	}

}
