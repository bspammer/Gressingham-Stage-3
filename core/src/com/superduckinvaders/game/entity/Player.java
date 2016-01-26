package com.superduckinvaders.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.assets.Assets;
import com.superduckinvaders.game.assets.TextureSet;

/**
 * Represents the player of the game.
 */
public class Player extends Character {

    /**
     * Player's maximum health.
     */
    public static final int PLAYER_HEALTH = 6;
    /**
     * Player's standard movement speed in pixels per second.
     */
    public static final int PLAYER_SPEED = 200;
    /**
     * Player's standard attack delay (how many seconds between attacks).
     */
    public static final float PLAYER_ATTACK_DELAY = 0.01f;
    /**
     * How much the Player's score increases should be multiplied by if they have the score multiplier powerup.
     */
    public static final double PLAYER_SCORE_MULTIPLIER = 5;
    /**
     * How much the Player's speed should be multiplied by if they have the super speed powerup.
     */
    public static final double PLAYER_SUPER_SPEED_MULTIPLIER = 3;
    /**
     * How much the Player's speed should me multiplied by if they are flying.
     */
    public static final double PLAYER_FLIGHT_SPEED_MULTIPLIER = 2;
    /**
     * How much the Player's attack rate should be multiplied by if they have the rate of fire powerup.
     */
    public static final double PLAYER_ATTACK_DELAY_MULTIPLIER = 1;
    /**
     * How long the Player can fly for, in seconds.
     */
    public static final double PLAYER_FLIGHT_TIME = 1;
    /**
     * How long after flying before the Player can fly again, in seconds.
     */
    public static final double PLAYER_FLIGHT_COOLDOWN = 5;

    /**
     * Player's current score.
     */
    private int points = 0;
    /**
     * Player's current powerup.
     */
    private Powerup powerup = Powerup.NONE;
    /**
     * Player's upgrade.
     */
    private Upgrade upgrade = Upgrade.NONE;
    /**
     * How much time is remaining on the Player's powerup.
     */
    private double powerupInitial, powerupTimer = 0;
    /**
     * Shows if a player is flying. If less than 0, player is flying for -flyingTimer seconds. If less than PLAYER_FLIGHT_COOLDOWN, flying is on cooldown.
     */
    private double flyingTimer = 5;
    /**
     * How long it has been since the Player last attacked.
     */
    private double attackTimer = 0;

    /**
     * Initialises this Player at the specified coordinates and with the specified initial health.
     *
     * @param parent the round this Player belongs to
     * @param x      the initial x coordinate
     * @param y      the initial y coordinate
     */
    public Player(Round parent, double x, double y) {
        super(parent, x, y, PLAYER_HEALTH);
    }

    /**
     * Increases the Player's score by the specified amount.
     *
     * @param amount the amount to increase the score by
     */
    public void addScore(int amount) {
        points += amount;
    }

    /**
     * Gets the Player's current score.
     *
     * @return the current score
     */
    public int getScore() {
        return points;
    }

    /**
     * Gets the Player's current flying timer.
     *
     * @return the current flying timer
     */
    public double getFlyingTimer() {
        return flyingTimer;
    }

    /**
     * Gets the Player's current powerup (in the Powerup enum).
     *
     * @return the current powerup
     */
    public Powerup getPowerup() {
        return powerup;
    }
    
    /**
     * Get the players current upgrade (in the Upgrade enum). 
     * @return the current upgrade
     */
    public Upgrade getUpgrade(){
    	return upgrade;
    }

    /**
     * Sets the Player's current upgrade.
     *
     * @param upgrade the upgrade to set
     */
    public void setUpgrade(Upgrade upgrade) {
        this.upgrade = upgrade;
    }

    /**
     * Gets the time remaining on the Player's powerup.
     *
     * @return the time remaining on the powerup
     */
    public double getPowerupTime() {
        return powerupTimer;
    }

    /**
     * Gets the time that the current powerup initially lasted for.
     *
     * @return the initial powerup time
     */
    public double getPowerupInitialTime() {
        return powerupInitial;
    }

    /**
     * Clears the Player's current powerup.
     */
    public void clearPowerup() {
        powerup = Powerup.NONE;
        powerupInitial = powerupTimer = 0;
    }

    /**
     * Sets the Player's current powerup and how long it should last.
     *
     * @param powerup the powerup to set (in the Powerup enum)
     * @param time    how long the powerup should last, in seconds
     */
    public void setPowerup(Powerup powerup, double time) {
        this.powerup = powerup;
        this.powerupInitial = this.powerupTimer = time;
    }

    /**
     * Returns if the player is currently flying
     * @return true if the Player is currently flying, false if not
     */
    public boolean isFlying() {
        return flyingTimer <= 0;
    }


    /**
     * @return the width of this Player
     */
    @Override
    public int getWidth() {
        return Assets.playerNormal.getWidth();
    }

    /**
     * @return the height of this Player
     */
    @Override
    public int getHeight() {
        return Assets.playerNormal.getHeight();
    }

    /**
     * Damages the Player, taking into account the possibility of invulnerability.
     *
     * @param health the number of points to damage
     */
    @Override
    public void damage(int health) {
        // Only apply damage if we don't have the invulnerability powerup.
        if (powerup != Powerup.INVULNERABLE) {
            super.damage(health);
        }
    }

    /**
     * Updates the state of this Player.
     *
     * @param delta how much time has passed since the last update
     */
    @Override
    public void update(float delta) {
        // Decrement powerup timer.
        if (powerupTimer > 0) {
            powerupTimer -= delta;
        } else if (powerupTimer <= 0) {
            clearPowerup();
        }

        // Update flying timer.
        flyingTimer += delta;

        // Update attack timer.
        attackTimer += delta;

        // Left mouse to attack.
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (attackTimer >= PLAYER_ATTACK_DELAY * (getPowerup() == Powerup.RATE_OF_FIRE ? PLAYER_ATTACK_DELAY_MULTIPLIER : 1)) {
                attackTimer = 0;

                if (upgrade == Upgrade.GUN) {
                    Vector3 target = parent.unproject(Gdx.input.getX(), Gdx.input.getY());

                    // Face target when firing gun.
                    facing = directionTo(target.x, target.y);
                    fireAt(target.x, target.y, 300, 100);
                } else {
                    // TODO: tweak melee range
                    melee(100, 100);
                }
            }
        }

        // Press space to start flying, but only if flying isn't cooling down and we're moving.
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && flyingTimer >= PLAYER_FLIGHT_COOLDOWN && (velocityX != 0 || velocityY != 0)) {
            flyingTimer = -PLAYER_FLIGHT_TIME;
            velocityX *= PLAYER_FLIGHT_SPEED_MULTIPLIER;
            velocityY *= PLAYER_FLIGHT_SPEED_MULTIPLIER;
        }

        // Only allow movement via keys if not flying.
        if (!isFlying()) {
            // Calculate speed at which to move the player.
            double speed = PLAYER_SPEED * (powerup == Powerup.SUPER_SPEED ? PLAYER_SUPER_SPEED_MULTIPLIER : 1);

            // Left/right movement.
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                velocityX = -speed;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                velocityX = speed;
            } else {
                velocityX = 0;
            }

            // Left/right movement.
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                velocityY = speed;
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                velocityY = -speed;
            } else {
                velocityY = 0;
            }

            // If moving diagonally, move slower.
            // This must not be done while flying otherwise the player will slow down and stop.
            if (velocityX != 0 && velocityY != 0) {
                velocityX /= Math.sqrt(2);
                velocityY /= Math.sqrt(2);
            }
        }

        // Update movement.
        super.update(delta);
    }

    /**
     * Renders this Player.
     *
     * @param spriteBatch the sprite batch on which to render
     */
    @Override
    public void render(SpriteBatch spriteBatch) {
        // Use the right texture set.
        TextureSet textureSet = isFlying() ? Assets.playerFlying : Assets.playerNormal;

        spriteBatch.draw(textureSet.getTexture(facing, stateTime), (int) x, (int) y);
    }

    /**
     * Available powerups (only last for a while).
     */
    public enum Powerup {
        NONE,
        SCORE_MULTIPLIER,
        SUPER_SPEED,
        RATE_OF_FIRE,
        INVULNERABLE;

        /**
         * Gets a texture for this powerup's floor item.
         *
         * @param powerup the powerup
         * @return the texture for the floor item
         */
        public static TextureRegion getTextureForPowerup(Powerup powerup) {
            switch (powerup) {
	            case SCORE_MULTIPLIER:
                    return Assets.floorItemScore;
                case SUPER_SPEED:
	                return Assets.floorItemSpeed;
                case RATE_OF_FIRE:
                    return Assets.floorItemFireRate;
                case INVULNERABLE:
                    return Assets.floorItemInvulnerable;
                default:
                    return null;
            }
        }
    }

    /**
     * Available upgrades (upgrades are persistent).
     */
    public enum Upgrade {
        NONE,
        GUN;

        /**
         * Gets a texture for this upgrade's floor item.
         *
         * @param upgrade the upgrade
         * @return the texture for the floor item
         */
        public static TextureRegion getTextureForUpgrade(Upgrade upgrade) {
            switch (upgrade) {
                case GUN:
                    return Assets.floorItemGun;
                default:
                    return null;
            }
        }
    }
}
