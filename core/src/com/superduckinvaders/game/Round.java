package com.superduckinvaders.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.superduckinvaders.game.ai.BossAI;
import com.superduckinvaders.game.ai.ZombieAI;
import com.superduckinvaders.game.assets.Assets;
import com.superduckinvaders.game.assets.TextureSet;
import com.superduckinvaders.game.entity.Character;
import com.superduckinvaders.game.entity.*;
import com.superduckinvaders.game.entity.item.Item;
import com.superduckinvaders.game.entity.item.Powerup;
import com.superduckinvaders.game.entity.item.Upgrade;
import com.superduckinvaders.game.objective.CollectObjective;
import com.superduckinvaders.game.objective.Objective;
import com.superduckinvaders.game.objective.SurviveObjective;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a round of the game played on one level with a single objective.
 */
public final class Round {
	
	/**
	 * Boolean representing whether or not a boss has already been spawned.
	 */
	private boolean bossFlag = true;
	
	/**
	 * Boolean indicating whether or not to spawn testing powerups next to player at start of round.
	 */
	private boolean testPowerups = true;

	/**
	 * Boolean indicating if the player is swimming or not (in water).
	 */
	public static boolean isSwimming = false;

	/**
	 * How near entities must be to the player to get updated in the game loop.
	 */
	public static final int UPDATE_DISTANCE = DuckGame.GAME_WIDTH / 2;

	/**
	 * Double indicating the chance of spawning a ranged mob.
	 */
	private static final double RANGED_MOB_SPAWNRATE = 0.1;
	
	/**
	 * Total number of mobs to spawn at start of round
	 */
	private static final int NUMBER_OF_MOBS = 200;

	/**
	 * The GameTest instance this Round belongs to.
	 */
	private DuckGame parent;

	/**
	 * The Round's map.
	 */
	private TiledMap map;

	/**
	 * Map layer containing randomly-chosen layer of predefined obstacles.
	 */
	private TiledMapTileLayer obstaclesLayer;

	/**
	 * The player.
	 */
	private Player player;

	/**
	 * Array of all entities currently in the Round.
	 */
	private List<Entity> entities;

	/**
	 * The current objective.
	 */
	private Objective objective;

	/**
	 * A counter representing the total number of mobs in the round.
	 */
	private int mobCount;

	/**
	 * Initialises a new Round with the specified map.
	 *
	 * @param parent the game the round is associated with
	 * @param map the Round's map
	 */
	public Round(DuckGame parent, TiledMap map) {
		this.parent = parent;
		this.map = map;

		// Choose which obstacles to use.
		obstaclesLayer = chooseObstacles();

		// Create array of entities firstly so we can add the game entities to it after the fact.
		entities = new ArrayList<Entity>(128);
		
		// Spawn player at map defined spawn point (default 0, 0).
		spawnPlayer(testPowerups);

		// Set up the Round objective.
		initObjective();
		
		// Initialise the mobCount and then spawn the specified number of mobs in the Round.
		this.mobCount = 0;
		spawnRandomMobs(NUMBER_OF_MOBS, 100, 100, 2000, 2000);
	}

	/**
	 * Handles the spawning of the player.
	 * Retrieves spawn point location from map file, defaults to (0, 0).
	 * 
	 * Also spawns testing powerups next to player depending on boolean parameter value.
	 * @param testing spawn testing powerups (yes/no).
	 */
	private void spawnPlayer(boolean testing) {
		// Determine starting coordinates for player (0, 0 default).
		int startX = Integer.parseInt(map.getProperties().get("StartX", "0", String.class)) * getTileWidth();
		int startY = Integer.parseInt(map.getProperties().get("StartY", "0", String.class)) * getTileHeight();
		player = new Player(this, startX, startY);
		entities.add(player);
		
		if(testing) spawnTestingPowerups(startX, startY);
	}

	/**
	 * Spawns one of each powerup next to player spawn point for testing purposes.
	 * @param playerSpawnX player spawn point x coord.
	 * @param playerSpawnY player spawn point y coord.
	 */
	private void spawnTestingPowerups(int playerSpawnX, int playerSpawnY) {
		//Development code to spawn powerups for testing.
		createUpgrade(playerSpawnX + 20, playerSpawnY, Player.Upgrade.GUN);
		createPowerup(playerSpawnX + 40, playerSpawnY, Player.Powerup.RATE_OF_FIRE);
		createPowerup(playerSpawnX + 60, playerSpawnY, Player.Powerup.INVULNERABLE);
		createPowerup(playerSpawnX + 80, playerSpawnY, Player.Powerup.SCORE_MULTIPLIER);
		createPowerup(playerSpawnX + 100, playerSpawnY, Player.Powerup.SUPER_SPEED);
		createPowerup(playerSpawnX + 120, playerSpawnY, Player.Powerup.REGENERATION);
	}
	
	private int getObjectiveType() {
		return Integer.parseInt(map.getProperties().get("ObjectiveType", "0", String.class));
	}

	/**
	 * Initialises the map's objective depending on it's specified ObjectiveType property.
	 */
	private void initObjective() {
		// read map.tmx property value to determine which objective to create
		int objectiveType = getObjectiveType();

		// create appropriate objective as defined by map.tmx file in ObjectiveType property
		switch(objectiveType) {
		case(Objective.COLLECT_OBJECTIVE):
			// set collect objective
			// determine where to spawn the objective from map properties.
			int objectiveX = Integer.parseInt(map.getProperties().get("ObjectiveX", "10", String.class)) * getTileWidth();
			int objectiveY = Integer.parseInt(map.getProperties().get("ObjectiveY", "10", String.class)) * getTileHeight();

			Item objective = new Item(this, objectiveX, objectiveY, Assets.flag);
			setObjective(new CollectObjective(this, objective));

			entities.add(objective);
			break;

		case(Objective.SURVIVE_OBJECTIVE):
			// set survival objective with time 100 seconds
			setObjective(new SurviveObjective(this, 100));
			break;
		}
	}

	/**
	 * Randomly selects and returns a set of predefined obstacles from the map.
	 *
	 * @return the map layer containing the obstacles
	 */
	private TiledMapTileLayer chooseObstacles() {
		int count = 0;

		// first count how many obstacle layers we have.
		while (map.getLayers().get(String.format("Obstacles%d", count)) != null) {
			count++;
		}

		// choose a random layer or return null if there are no layers.
		if (count == 0) {
			return null;
		} else {
			return (TiledMapTileLayer) map.getLayers().get(String.format("Obstacles%d", MathUtils.random(0, count - 1)));
		}
	}

	/**
	 * Spawns a number of random mobs the specified distance from the player.
	 * @param amount how many random mobs to spawn
	 * @param minX the minimum x distance from the player to spawn the mobs
	 * @param minY the minimum y distance from the player to spawn the mobs
	 * @param maxX the maximum x distance from the player to spawn the mobs
	 * @param maxY the maximum y distance from the player to spawn the mobs
	 */
	private void spawnRandomMobs(int amount, int minX, int minY, int maxX, int maxY) {
		for (int i=0;i<amount; i++) {
				int x = MathUtils.random(minX, maxX) * (MathUtils.randomBoolean() ? -1 : 1);
				int y = MathUtils.random(minY, maxY) * (MathUtils.randomBoolean() ? -1 : 1);
				createMob(getPlayer().getX() + x, getPlayer().getY() + y, 100, Assets.badGuyNormal, 100);
				mobCount +=  1;
			} 
		}
	
	/**
	 * Gets the current map
	 * @return this Round's map
	 */
	public TiledMap getMap() {
		return map;
	}

	/**
	 * Gets the base layer of the map
	 * @return this Round's base layer (used for calculating map width/height)
	 */
	public TiledMapTileLayer getBaseLayer() {
		return (TiledMapTileLayer) getMap().getLayers().get("Base");
	}

	/**
	 * Gets the collision layer of the map
	 * @return this Round's collision map layer
	 */
	public TiledMapTileLayer getCollisionLayer() {
		return (TiledMapTileLayer) getMap().getLayers().get("Collision");
	}

	/**
	 * Gets the obstacles layer of the map
	 * @return this Round's obstacles map layer or null if there isn't one
	 */
	public TiledMapTileLayer getObstaclesLayer() {
		return obstaclesLayer;
	}

	/**
	 * gets the overhang layer of the map
	 * @return this Round's overhang map layer (rendered over entities)
	 */
	public TiledMapTileLayer getOverhangLayer() {
		return (TiledMapTileLayer) getMap().getLayers().get("Overhang");
	}

	/**
	 * Gets the width of the map in pixels
	 * @return the width of this Round's map in pixels
	 */
	public int getMapWidth() {
		return (int) (getBaseLayer().getWidth() * getBaseLayer().getTileWidth());
	}

	/**
	 * Gets the height of the map in pixels
	 * @return the height of this Round's map in pixels
	 */
	public int getMapHeight() {
		return (int) (getBaseLayer().getHeight() * getBaseLayer().getTileHeight());
	}

	/**
	 * Gets the width of each tile
	 * @return the width of one tile in this Round's map
	 */
	public int getTileWidth() {
		return (int) getBaseLayer().getTileWidth();
	}

	/**
	 * Gets the height of each tile
	 * @return the height of one tile in this Round's map
	 */
	public int getTileHeight() {
		return (int) getBaseLayer().getTileHeight();
	}

	/**
	 * Converts screen coordinates to world coordinates.
	 *
	 * @param x the x coordinate on screen
	 * @param y the y coordinate on screen
	 * @return a Vector3 containing the world coordinates (x and y)
	 */
	public Vector3 unproject(int x, int y) {
		return parent.getGameScreen().unproject(x, y);
	}

	/**
	 * Gets the player in the round
	 * @return this Round's player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets all entities in the round
	 * @return the list of all entities currently in the Round
	 */
	public List<Entity> getEntities() {
		return entities;
	}

	/**
	 * Adds an entity to the entity list.
	 *
	 * @param newEntity new entity of any type
	 */
	public void addEntity(Entity newEntity) {
		entities.add(newEntity);
	}

	/**
	 * Gets the current objective of this Round.
	 *
	 * @return the current objective
	 */
	public Objective getObjective() {
		return objective;
	}

	/**
	 * Sets the current objective of this Round.
	 *
	 * @param objective the new objective
	 */
	public void setObjective(Objective objective) {
		this.objective = objective;
	}

	/**
	 * Creates a new projectile and adds it to the list of entities.
	 *
	 * @param x               the initial x coordinate
	 * @param y               the initial y coordinate
	 * @param targetX         the target x coordinate
	 * @param targetY         the target y coordinate
	 * @param speed           how fast the projectile moves
	 * @param velocityXOffset the offset to the initial X velocity
	 * @param velocityYOffset the offset to the initial Y velocity
	 * @param damage          how much damage the projectile deals
	 * @param owner           the owner of the projectile (i.e. the one who fired it)
	 */
	public void createProjectile(double x, double y, double targetX, double targetY, double speed, double velocityXOffset, double velocityYOffset, int damage, Entity owner) {
		entities.add(new Projectile(this, x, y, targetX, targetY, speed, velocityXOffset, velocityYOffset, damage, owner));
	}

	/**
	 * Creates a new particle effect and adds it to the list of entities.
	 *
	 * @param x         the x coordinate of the center of the particle effect
	 * @param y         the y coordinate of the center of the particle effect
	 * @param duration  how long the particle effect should last for
	 * @param animation the animation to use for the particle effect
	 */
	public void createParticle(double x, double y, double duration, Animation animation) {
		entities.add(new Particle(this, x - animation.getKeyFrame(0).getRegionWidth() / 2, y - animation.getKeyFrame(0).getRegionHeight() / 2, duration, animation));
	}

	/**
	 * Creates a powerup on the floor and adds it to the list of entities.
	 *
	 * @param x       the x coordinate of the powerup
	 * @param y       the y coordinate of the powerup
	 * @param powerup the powerup to grant to the player
	 * @param time    how long the powerup should last for
	 */
	public void createPowerup(double x, double y, Player.Powerup powerup) {
		entities.add(new Powerup(this, x, y, powerup));
	}

	/**
	 * Creates an upgrade on the floor and adds it to the list of entities.
	 *
	 * @param x       the x coordinate of the upgrade
	 * @param y       the y coordinate of the upgrade
	 * @param upgrade the upgrade to grant to the player
	 */
	public void createUpgrade(double x, double y, Player.Upgrade upgrade) {
		entities.add(new Upgrade(this, x, y, upgrade));
	}

	/**
	 * Creates a mob and adds it to the list of entities, but only if it doesn't intersect with another character.
	 * @param x the initial x coordinate
	 * @param y the initial y coordinate
	 * @param health the initial health of the mob
	 * @param textureSet the texture set to use
	 * @param speed how fast the mob moves in pixels per second
	 * @return true if the mob was successfully added, false if there was an intersection and the mob wasn't added
	 */
	public boolean createMob(double x, double y, int health, TextureSet textureSet, int speed) {
		float random = MathUtils.random();
		Mob mob;
		
		//spawn mobs as ranged mobs with probability of RANGED_MOB_SPAWNRATE
		if (random < RANGED_MOB_SPAWNRATE) {
			mob = new Mob(this, x, y, health, textureSet, speed, new ZombieAI(this, 32), true,false);
		} else {
			mob = new Mob(this, x, y, health, textureSet, speed, new ZombieAI(this, 32), false,false);
		}

		// Check mob isn't out of bounds.
		if (x < 0 || x > getMapWidth() - textureSet.getWidth() || y < 0 || y > getMapHeight() - textureSet.getHeight()) {
			return false;
		}

		// Check mob doesn't intersect anything.
		for (Entity entity : entities) {
			if (entity instanceof Character
					&& (mob.intersects(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight()) || mob.collidesX(0) || mob.collidesY(0))) {
				return false;
			}
		}

		entities.add(mob);
		return true;
	}
	
	/**
	 * Creates a boss mob at the given coordinates
	 * @param x the initial x coordinate
	 * @param y the initial y coordinate
	 * @param health the initial health of the mob
	 * @param textureSet the texture set to use
	 * @param speed how fast the mob moves in pixels per second
	 * @return true if the mob was successfully added, false if there was an intersection and the mob wasn't added
	 */
	private boolean createBoss(double x, double y, int health, TextureSet textureSet, int speed) {
		Mob mob;
		mob = new Mob(this, x, y, health, textureSet, speed, new BossAI(this, 32), false,true);
		// Check mob isn't out of bounds.
		if (x < 0 || x > getMapWidth() - textureSet.getWidth() || y < 0 || y > getMapHeight() - textureSet.getHeight()) {
			return false;
		}
		// Check mob doesn't intersect anything.
		for (Entity entity : entities) {
			if (entity instanceof Character
					&& (mob.intersects(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight()) || mob.collidesX(0) || mob.collidesY(0))) {
				return false;
			}
		}
		entities.add(mob);
		return true;
	}
	
	/**
	 * Spawns a boss mob at a randomly chosen (within a range) distance from the player.
	 * @param attempts how many times to attempt spawning the boss randomly.
	 * @param minX the minimum x distance from the player to spawn the mob.
	 * @param minY the minimum y distance from the player to spawn the mob.
	 * @param maxX the maximum x distance from the player to spawn the mob.
	 * @param maxY the maximum y distance from the player to spawn the mob.
	 * @return true if the boss was successfully spawned in the given number of attempts.
	 */
	private boolean spawnRandomBoss(int attempts, int minX, int minY, int maxX, int maxY) {
		for (int i=0; i < attempts; i++) {
				int x = MathUtils.random(minX, maxX) * (MathUtils.randomBoolean() ? -1 : 1);
				int y = MathUtils.random(minY, maxY) * (MathUtils.randomBoolean() ? -1 : 1);
				if (createBoss(getPlayer().getX() + x, getPlayer().getY() + y, 500, Assets.bossNormal, 200)) {
					return true;
				}
			}
			return false;
	}
	
	private void updateEntities(float delta) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);

			// for each entity update target position x and y with player x and y.
			if (entity instanceof Mob) {
				if ((boolean) ((Mob) entity).isRanged()) {
					((Mob) entity).updateTargetPosition(player.getX(), player.getY());
				}
			}

			if (entity.isRemoved()) {
				entities.remove(i);
				if (entity instanceof Mob) {
					mobCount--;

					int scoreToAdd = 0;
					if (((Mob) entity).isBoss()) {
						scoreToAdd = (int) (100 * (player.powerupIsActive(Player.Powerup.SCORE_MULTIPLIER) ? Player.PLAYER_SCORE_MULTIPLIER : 1));
					} else {
						scoreToAdd = (int) (10 * (player.powerupIsActive(Player.Powerup.SCORE_MULTIPLIER) ? Player.PLAYER_SCORE_MULTIPLIER : 1));
					}
					player.addScore(scoreToAdd);
					
					//create an animated text to show added score
					Color textColor;
					if (scoreToAdd <= 10) {
						textColor = Color.WHITE;
					} else if (scoreToAdd <=50){
						textColor = Color.RED;
					} else {
						textColor = Color.BLACK;
					}
					
					parent.getGameScreen().addAnimatedText("+" + Integer.toString(scoreToAdd), (float) (entity.getX() - entity.getWidth()/2), (float) entity.getY() + entity.getHeight(), textColor);
					//respawn killed enemies on SurviveObjective
					if (getObjectiveType() == Objective.SURVIVE_OBJECTIVE) {
						//spawns 2 mobs for every 1 you kill. Levels get progressivley harder
						spawnRandomMobs(2, 100, 100, 300, 300);
						System.out.println(mobCount);
					}
				}
			} else if (entity.distanceTo(player.getX(), player.getY()) < UPDATE_DISTANCE){
				// Don't bother updating entities that aren't on screen.
				entity.update(delta);
			}
		}
	}
	
	private void updateObjective(float delta) {
		// TODO Fix bug where replaying previously completed level wipes game progress.
		if (objective != null) {
			objective.update(delta);

			if (objective.getStatus() == Objective.OBJECTIVE_COMPLETED) {

				DuckGame.stopMusic();
				DuckGame.playSoundEffect(Assets.levelComplete, 1);

				//if game not already completed
				if(!DuckGame.levelsComplete.equals("11111111")) {
					if(map.equals(Assets.levelOneMap)){
						DuckGame.levelsComplete="1000000";
					} else if(map.equals(Assets.levelTwoMap)) {
						DuckGame.levelsComplete="11000000";
					} else if(map.equals(Assets.levelThreeMap)) {
						DuckGame.levelsComplete="11100000";
					} else if(map.equals(Assets.levelFourMap)) {
						DuckGame.levelsComplete="11110000";
					} else if(map.equals(Assets.levelFiveMap)) {
						DuckGame.levelsComplete="11111000";
					} else if(map.equals(Assets.levelSixMap)) {
						DuckGame.levelsComplete="11111100";
					} else if(map.equals(Assets.levelSevenMap)) {
						DuckGame.levelsComplete="11111110";
					} else if(map.equals(Assets.levelEightMap)) {
						DuckGame.levelsComplete="11111111";
					}
					
					//add level score to total
					parent.addScoreToTotal(player.getScore());

					if(DuckGame.levelsComplete.equals("11111111")) {
						parent.showCompleteScreen();
					} else {
						parent.showWinScreen(player.getScore());
					}

					//game already completed so don't record score and just display game complete screen
				} else {
					parent.showCompleteScreen();
				}

				//always save the settings
				DuckGame.saveSettings();

			} else if (player.isDead()) {
				DuckGame.stopMusic();
				DuckGame.playSoundEffect(Assets.gameOver, 1);
				parent.showLoseScreen();
			}
		}

		//spawn boss at specified time
		if (getObjectiveType() == Objective.SURVIVE_OBJECTIVE) {
			SurviveObjective surviveObjective = ((SurviveObjective) objective);
			if (surviveObjective.getTimeRemaining() < surviveObjective.getBossSpawnTime()) {
				if (bossFlag){
					System.out.println("trying to spawn boss 100 times at min 100 max 300 distance");
					if (spawnRandomBoss(100, 100, 100, 300, 300)){
						bossFlag = false;
					}
				}
			}
		}
	}

	/**
	 * Handles updating of all round specific objects.
	 *
	 * @param delta the time elapsed since the last update
	 */
	public void update(float delta) {
		
		updateObjective(delta);
		
		updateEntities(delta);
	}
	
	public DuckGame getGame() {
		return parent;
	}
}
