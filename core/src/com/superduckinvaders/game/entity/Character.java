package com.superduckinvaders.game.entity;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.assets.TextureSet;

/**
 * Represents a character in the game.
 */
public abstract class Character extends Entity {

	
	
	
	/**
	 * whether or not the character is swimming
	 */
	protected boolean isSwimming = false;
	
    /**
     * The direction the Character is facing.
     */
    protected int facing = TextureSet.FACING_FRONT;

    /**
     * The state time for the animation. Set to 0 for not moving.
     */
    protected float stateTime = 0;

    /**
     * Current health and the maximum health of this Character.
     */
    protected int maximumHealth, currentHealth;

    /**
     * Initialises this Character.
     *
     * @param parent        the round this Character belongs to
     * @param x             the initial x coordinate
     * @param y             the initial y coordinate
     * @param maximumHealth the maximum (and initial) health of this Character
     */
    public Character(Round parent, double x, double y, int maximumHealth) {
        super(parent, x, y);

        this.maximumHealth = this.currentHealth = maximumHealth;
    }

    /**
     * Gets the direction the character is facing
     * @return the direction this Character is facing (one of the FACING_ constants in TextureSet)
     */
    public int getFacing() {
        return facing;
    }

    /**
     * Gets the current health of this Character.
     *
     * @return the current health of this Character
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Gets the maximum health of this Character.
     *
     * @return the maximum health of this Character
     */
    public int getMaximumHealth() {
        return maximumHealth;
    }

    /**
     * Heals this Character's current health by the specified number of points.
     *
     * @param health the number of health points to heal
     */
    public void heal(int health) {
        this.currentHealth += health;

        if (currentHealth > maximumHealth) {
            currentHealth = maximumHealth;
        }
    }

    /**
     * Damages this Character's health by the specified number of points.
     *
     * @param health the number of points to damage
     */
    public void damage(int health) {
        this.currentHealth -= health;
    }

    /**
     * Returns if the character is dead
     * @return whether this Character is dead (i.e. its health is 0)
     */
    public boolean isDead() {
        return currentHealth <= 0;
    }

    /**
     * Causes this Character to fire a projectile at the specified coordinates.
     *
     * @param x      the target x coordinate
     * @param y      the target y coordinate
     * @param speed  how fast the projectile moves
     * @param damage how much damage the projectile deals
     */
    protected void fireAt(double x, double y, int speed, int damage) {
        parent.createProjectile(this.x + getWidth() / 2, this.y + getHeight() / 2, x, y, speed, velocityX, velocityY, damage, this);
    }

    /**
     * Causes this Character to use a melee attack.
     *
     * @param range  how far the attack reaches in pixels
     * @param damage how much damage the attack deals
     */
    protected void melee(double range, int damage) {
        // Don't let mobs melee other mobs (for now).
        if (this instanceof Mob) {
            Player player = parent.getPlayer();

            if (distanceTo(player.getX(), player.getY()) <= range && directionTo(player.getX(), player.getY()) == facing) {
                player.damage(damage);
            }
        } else {
            // Attack the closest Character within the range.
            Character closest = null;

            for (Entity entity : parent.getEntities()) {
                // Disregard entity if it's me or it isn't a Character.
                if (this == entity || !(entity instanceof Character)) {
                    continue;
                }

                double x = entity.getX(), y = entity.getY();
                if (distanceTo(x, y) <= range && directionTo(x, y) == facing && (closest == null || distanceTo(x, y) < distanceTo(closest.getX(), closest.getY()))) {
                    closest = (Character) entity;
                }
            }

            // Can't attack if nothing in range.
            if (closest != null) {
                closest.damage(damage);
            }
        }
    }
    
    /**
     * checks whether the character is swimming and sets isSwimming appropriately
     */
    public void checkSwimming(){
    	int tileWidth = parent.getTileWidth();
    	TiledMapTileLayer water = (TiledMapTileLayer) parent.getMap().getLayers().get("Water");
		if (water.getCell((int)x/tileWidth,(int)y/tileWidth) != null){
			isSwimming = true;
		}
		else{
			isSwimming=false;
		}
    }

    /**
     * getter for the isSwimming var
     * @return bool
     */
    public boolean getSwimming(){
    	return isSwimming;
    }
    
    
    
    
    /**
     * Updates the state of this Character.
     *
     * @param delta how much time has passed since the last update
     */
    @Override
    public void update(float delta) {
    	
    	//update isSwimming 
    	checkSwimming();
    	
 
        // Update Character facing.
        if (velocityX < 0) {
            facing = TextureSet.FACING_LEFT;
        } else if (velocityX > 0) {
            facing = TextureSet.FACING_RIGHT;
        }

        if (velocityY < 0) {
            facing = TextureSet.FACING_FRONT;
        } else if (velocityY > 0) {
            facing = TextureSet.FACING_BACK;
        }

        
        // Update animation state time.
        if (velocityX != 0 || velocityY != 0) {
            stateTime += delta;
        } else {
            stateTime = 0;
        }

        if (isDead()) {
            removed = true;
        }

        super.update(delta);
    }
}
