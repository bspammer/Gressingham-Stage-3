
package com.superduckinvaders.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.ai.AI;
import com.superduckinvaders.game.ai.DummyAI;
import com.superduckinvaders.game.assets.Assets;
import com.superduckinvaders.game.assets.TextureSet;

public class Mob extends Character {

    /**
     * The texture set to use for this Mob.
     */
    private TextureSet textureSet;
    
    /**
     * AI class for the mob
     */
    private AI ai;
    
    /**
     * Speed of the mob in pixels per second
     */
    private int speed;
    
    /**
     * Boolean indicating whether or not the mob is a ranged mob.
     */
    private boolean ranged;
    
    /**
     * Doubles indicating the position x,y of the ranged mob target.
     */
    private double targetX, targetY;
    
    /**
     * Double indicating the chance a mob has to fire a projectile.
     */
    private static double MOB_FIRERATE = 0.005;
    
    /**
     * Constructor for a Mob character.
     * @param parent The round the mob is created in.
     * @param x Mobs position in x
     * @param y Mobs position in y
     * @param health The mobs health
     * @param textureSet The textureSet of the mob.
     * @param speed The speed of the mob.
     * @param ai Which AI to spawn the mob with.
     * @param ranged Is the mob ranged or not.
     */
    public Mob(Round parent, double x, double y, int health, TextureSet textureSet, int speed, AI ai, boolean ranged) {
        super(parent, x, y, health);

        this.textureSet = textureSet;
        this.speed = speed;
        this.ai = ai;
        this.ranged = ranged;
    }

    public Mob(Round parent, int x, int y, int health, TextureSet textureSet, int speed) {
        this(parent, x, y, health, textureSet, speed, new DummyAI(parent), false);
    }
    
    /**
     * Sets the AI for this Mob.
     * @param ai the new AI to use
     */
    public void setAI(AI ai) {
        this.ai = ai;
    }

    /**
     * Sets the speed of the mob
     * @param newSpeed the updated speed
     */
    public void setSpeed(int newSpeed){
        this.speed = newSpeed;
    }
    
    /**
     * change where the given mob moves to according to its speed and a new direction vector
     * @param dirX x component of the direction vector
     * @param dirY y component of the direction vector
     */
    public void setVelocity(int dirX, int dirY){
    	if(dirX == 0 && dirY==0){
    		velocityX=0;
    		velocityY=0;
    		return;
    	}
    	double magnitude = Math.sqrt(dirX*dirX + dirY*dirY);
    	velocityX = (dirX*speed)/magnitude;
    	velocityY = (dirY*speed)/magnitude;

    }
    
    /**
     * gets texture width
     */
    @Override
    public int getWidth() {
        return textureSet.getTexture(TextureSet.FACING_FRONT, 0).getRegionWidth();
    }

    /**
     * gets texture height
     */
    @Override
    public int getHeight() {
        return textureSet.getTexture(TextureSet.FACING_FRONT, 0).getRegionHeight();
    }
    
    public void updateTargetPosition(double x, double y) {
    	targetX = x;
    	targetY = y;
    }

    /**
     * Updates mob
     */
    @Override
    public void update(float delta) {
        ai.update(this, delta);
        float random = MathUtils.random();
        
        //if mob is ranged, fire projectile with given probability.
        if (random < MOB_FIRERATE && ranged && !this.isSwimming) {
        	fireAt(targetX, targetY, 300, 1);
        }

        if(this.getSwimming()){
    		speed=50;
    	}
    	else {
    		speed=100;
    	}
        // Chance of spawning a random powerup.
        if (isDead()) {
            //float random = MathUtils.random();
            Player.Powerup powerup = null;

            if (random < 0.05) {
                powerup = Player.Powerup.SCORE_MULTIPLIER;
            } else if (random >= 0.05 && random < 0.1) {
                powerup = Player.Powerup.INVULNERABLE;
            } else if (random >= 0.1 && random < 0.15) {
                powerup = Player.Powerup.SUPER_SPEED;
            } else if (random >= 0.15 && random < 0.2) {
                powerup = Player.Powerup.RATE_OF_FIRE;
            } else if (random >= 0.2 && random < 0.25) {
            	powerup = Player.Powerup.REGENERATION;
            }

            if (powerup != null) {
                parent.createPowerup(x, y, powerup);
            }
        }

        super.update(delta);
    }
    
    /**
     * Set the direction of facing
     * @param newFacing direction
     */
    public void setFacing(int newFacing) {
    	facing = newFacing;
    }

    /**
     * Renders the mob
     */
    @Override
    public void render(SpriteBatch spriteBatch) {
    	if (this.getSwimming()) {
    		//CHANGE ME
        	textureSet = Assets.badGuySwimming;
        } 
        else if (this.isRanged()) {
        	textureSet = Assets.badGuyGun;
        } else {
        	textureSet = Assets.badGuyNormal;
        }
    	
        spriteBatch.draw(textureSet.getTexture(facing, stateTime), (int) x, (int) y);
    }
    
    /**
     * Determine if the mob is ranged or not.
     * @return boolean indicating if mob is ranged or not.
     */
    public boolean isRanged() {
    	return ranged;
    }
}
