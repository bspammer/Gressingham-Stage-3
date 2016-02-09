package com.superduckinvaders.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer.Random;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.superduckinvaders.game.assets.Assets;
import com.superduckinvaders.game.entity.Entity;
import com.superduckinvaders.game.entity.Player;
import com.superduckinvaders.game.entity.item.Powerup;

import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Screen for interaction with the game.
 */
public class GameScreen implements Screen {

	/**
	 * Draw map gridlines for debug purposes.
	 */
	private boolean gridlines=false;

	/**
	 * The game camera.
	 */
	private OrthographicCamera camera;

	/**
	 * The renderer for the tile map.
	 */
	private OrthogonalTiledMapRenderer mapRenderer;

	/**
	 * The sprite batches for rendering.
	 */
	private SpriteBatch spriteBatch, uiBatch;

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
	 * Initialises this GameScreen for the specified round.
	 *
	 * @param round the round to be displayed
	 */
	public GameScreen(Round round) {
		this.round = round;
		this.prevWindowWidth = Gdx.graphics.getWidth();
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
		return camera.unproject(new Vector3(x, y, 0));
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

		camera = new OrthographicCamera(DuckGame.GAME_WIDTH, DuckGame.GAME_HEIGHT);
		camera.zoom -= 0.5;

		spriteBatch = new SpriteBatch();
		uiBatch = new SpriteBatch();

		mapRenderer = new OrthogonalTiledMapRenderer(round.getMap(), spriteBatch);

		
	
	}

	/**
	 * Main game loop.
	 *
	 * @param delta how much time has passed since the last update
	 */
	@Override
	public void render(float delta) {
		round.update(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Centre the camera on the player.
		camera.position.set((int) round.getPlayer().getX() + round.getPlayer().getWidth() / 2, (int) round.getPlayer().getY() + round.getPlayer().getHeight() / 2, 0);
		camera.update();

		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		// Render base and collision layers.
		mapRenderer.setView(camera);
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

		// Render overhang layer (draws over the player).
		if (round.getOverhangLayer() != null) {
			mapRenderer.renderTileLayer(round.getOverhangLayer());
		}
		
		spriteBatch.end();

		//draw main player UI elements
		uiBatch.begin();
		drawPlayerObjectiveAndScore();

		drawPlayerStaminaBar();

		drawPlayerHearts();

		if (Player.minimapOn) {
			drawMinimap();
		}
		uiBatch.end();

		//draw custom powerup icon timers
		drawPlayerPowerupTimers();

		if (gridlines){
			TiledMap map = round.getMap();
			ShapeRenderer sr = new ShapeRenderer();
			int tileWidth = map.getProperties().get("tilewidth", Integer.class), tileHeight = map.getProperties().get("tileheight", Integer.class);
			int mapWidth = map.getProperties().get("width", Integer.class) * tileWidth, mapHeight = map.getProperties().get("height", Integer.class) * tileHeight;
			sr.setProjectionMatrix(camera.combined);
			sr.begin(ShapeType.Line);
			for(int y = 0; y < mapWidth; y += tileWidth)
				sr.line(y, 0, y, mapHeight);
			for(int y = 0; y < mapHeight; y += tileHeight)
				sr.line(0, y, mapWidth, y);
			sr.end();
		}
	}

	private void drawMinimap() {
		Player player = round.getPlayer();
		MapLayers layers = round.getMap().getLayers();
		
		
		int playerX = (int) player.getX()/round.getTileWidth();
		int playerY = (int) player.getY()/round.getTileHeight();

		// Odd numbers so player is centred
		int minimapWidth = 51;
		int minimapHeight = 51;
		int minimapScale = 4;
		int minimapX = Gdx.graphics.getWidth() - minimapWidth*minimapScale - 5;
		int minimapY = Gdx.graphics.getHeight() - minimapHeight*minimapScale - 5;
		int minimapXOffset = playerX - minimapWidth/2 - 1;
		int minimapYOffset = playerY - minimapHeight/2 - 1;

		if (playerX < minimapWidth/2) {
			minimapXOffset = 0;
		}
		if (playerY < minimapHeight/2) {
			minimapYOffset = 0;
		}
		if (playerX > round.getMapWidth()/round.getTileWidth() - minimapWidth/2) {
			minimapXOffset = round.getMapWidth()/round.getTileWidth() - minimapWidth;
		}
		if (playerY > round.getMapHeight()/round.getTileHeight() - minimapHeight/2) {
			minimapYOffset = round.getMapHeight()/round.getTileHeight() - minimapHeight;
		}
		Pixmap minimapData = new Pixmap(minimapWidth*minimapScale, minimapHeight*minimapScale, Pixmap.Format.RGBA8888);

		
		for (int i=0; i<minimapWidth; i++) {
			for (int j=0; j<minimapHeight; j++) {
				int cellColor = 0x7DC847FF;
				TiledMapTileLayer waterLayer = (TiledMapTileLayer) layers.get("Water");
				TiledMapTileLayer baseLayer = (TiledMapTileLayer) layers.get("Base");
				TiledMapTileLayer collisionLayer = (TiledMapTileLayer) layers.get("Collision");
				TiledMapTileLayer obstaclesLayer = round.getObstaclesLayer();
				TiledMapTileLayer overhangLayer = (TiledMapTileLayer) layers.get("Overhang");
				
				if(Integer.parseInt(round.getMap().getProperties().get("ObjectiveX").toString())==i+minimapXOffset+1
						&& Integer.parseInt(round.getMap().getProperties().get("ObjectiveY").toString())==j+minimapYOffset){
					cellColor = 0xFF0000FF;
				}
				else if (playerX - minimapXOffset == i && playerY - minimapYOffset == j) {
					cellColor = 0xFFFFFFFF;
				} else if (overhangLayer.getCell(i+minimapXOffset, j+minimapYOffset) != null){
					int cellID=overhangLayer.getCell(i+minimapXOffset, j+minimapYOffset).getTile().getId();
					if (cellID ==9 ||cellID ==10 ||cellID ==29 ||cellID ==30){
						cellColor = 0x386D38FF;
					}
				}else if (waterLayer.getCell(i+minimapXOffset, j+minimapYOffset) != null) {
					cellColor = 0x6983E8FF;
				} else if (collisionLayer.getCell(i+minimapXOffset, j+minimapYOffset) != null) {
					cellColor = 0xA2693EFF;
				} else if (obstaclesLayer.getCell(i+minimapXOffset, j+minimapYOffset) != null) {
					int cellID=obstaclesLayer.getCell(i+minimapXOffset, j+minimapYOffset).getTile().getId();
					if (cellID==8){
						cellColor = 0x42AA52FF;
					}
					else if (cellID ==49 || cellID ==50){
						cellColor = 0x4E3A21FF;
					}
					else{
					cellColor = 0x108239FF;
					}
				}else if (baseLayer.getCell(i+minimapXOffset, j+minimapYOffset) != null) {
					int cellID=baseLayer.getCell(i+minimapXOffset, j+minimapYOffset).getTile().getId();
					//if the cell is not one of the grass cells then it must be a path cell
					if (cellID!=1 && cellID!=2 && cellID!=11 && cellID!=12 && cellID!=13 && cellID!=21 && cellID!=22 && cellID!=26 && cellID!=31 && cellID!=33 && cellID!=51 && cellID!=52 && cellID!=53 && cellID!=228 ){
						cellColor = 0xE8B969FF;
					}
					
					
				}
				
				
				for (int k=0; k<minimapScale; k++) {
					for (int l=0; l<minimapScale; l++) {
						minimapData.drawPixel(i*minimapScale+k, minimapHeight*minimapScale-(j*minimapScale+l), cellColor);
					}
				}
			}
		}

		//draws border for the minimap
		uiBatch.draw(Assets.horizontalBorder,minimapX-1, minimapY-2);
		uiBatch.draw(Assets.horizontalBorder,minimapX-1, minimapY+minimapHeight*minimapScale-1);
		uiBatch.draw(Assets.verticalBorder,minimapX-2,minimapY-1);
		uiBatch.draw(Assets.verticalBorder,minimapX+minimapWidth*minimapScale,minimapY-1);
		
		
		Texture minimapTexture = new Texture(minimapData);
		uiBatch.draw(minimapTexture, minimapX, minimapY, minimapWidth*minimapScale, minimapHeight*minimapScale);
		
		// Need to flush because we're about to dispose the texture
		uiBatch.flush();
		
		
		
		
		
		minimapData.dispose();
		minimapTexture.dispose();
	}

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

	
	
	
	private void drawPlayerHearts() {
		int x = 0;
		while(x < round.getPlayer().getMaximumHealth()) {
			if(x+2 <= round.getPlayer().getCurrentHealth())
				uiBatch.draw(Assets.heartFull, x * 18 + 10, 10);
			else if(x+1 <= round.getPlayer().getCurrentHealth())
				uiBatch.draw(Assets.heartHalf, x * 18 + 10, 10);
			else
				uiBatch.draw(Assets.heartEmpty, x * 18 + 10, 10);
			x += 2;
		}
	}

	private void drawPlayerStaminaBar() {
		// Draw stamina bar (for flight);
		int staminaBarWidth = Assets.staminaEmpty.getRegionWidth();
		int staminaBarHeight = Assets.staminaEmpty.getRegionHeight();

		//calculate offset for positioning of stamina bar
		int resizeOffset = (Gdx.graphics.getWidth() - prevWindowWidth ) == 0 ? staminaBarWidth : (staminaBarWidth + (Gdx.graphics.getWidth() - prevWindowWidth));

		int staminaBarX = Gdx.graphics.getWidth() - resizeOffset - 10;
		int staminaBarY = 10;
		// TODO Stamina bar does not draw in correct position on window resize
		uiBatch.draw(Assets.staminaEmpty, staminaBarX, staminaBarY, staminaBarWidth, staminaBarHeight);
		if (round.getPlayer().getFlyingTimer() > 0) {
			Assets.staminaFull.setRegionWidth((int) Math.max(0, Math.min(staminaBarWidth, round.getPlayer().getFlyingTimer() / Player.PLAYER_FLIGHT_COOLDOWN * staminaBarWidth)));
		} else {
			Assets.staminaFull.setRegionWidth(0);
		}
		uiBatch.draw(Assets.staminaFull, staminaBarX, staminaBarY);
	}

	private void drawPlayerObjectiveAndScore() {
		Assets.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		Assets.font.draw(uiBatch, "Objective: " + round.getObjective().getObjectiveString(), 10, 100);
		Assets.font.draw(uiBatch, "Score: " + round.getPlayer().getScore(), 10,80);
		Assets.font.draw(uiBatch, Gdx.graphics.getFramesPerSecond() + " FPS", 10, 60);
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
		uiBatch.dispose();
	}

}
