package com.superduckinvaders.game.entity.item;

import java.util.HashMap;

import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.entity.Player;

/**
 * Represents a powerup on the floor.
 */
public class Powerup extends Item {
	
	private static final HashMap<Player.Powerup, Integer> POWERUP_MAX_TIMES = new HashMap<Player.Powerup, Integer>();
	static {
		POWERUP_MAX_TIMES.put(Player.Powerup.INVULNERABLE, 10);
		POWERUP_MAX_TIMES.put(Player.Powerup.RATE_OF_FIRE, 10);
		POWERUP_MAX_TIMES.put(Player.Powerup.SCORE_MULTIPLIER, 10);
		POWERUP_MAX_TIMES.put(Player.Powerup.SUPER_SPEED, 10);
	}
	
    /**
     * The powerup that this Powerup gives to the player.
     */
    private Player.Powerup powerup;

    /**
     * How long the powerup will last for.
     */
    private double time;

    public Powerup(Round parent, double x, double y, Player.Powerup powerup) {
        super(parent, x, y, Player.Powerup.getTextureForPowerup(powerup));

        this.powerup = powerup;
        this.time = POWERUP_MAX_TIMES.get(powerup);
    }
    
    /**
     * Returns the max duration for a specific powerup
     * @param powerup The powerup
     * @return The max duration for a specific powerup
     */
    public static int getMaxPowerupTime(Player.Powerup powerup) {
    	return POWERUP_MAX_TIMES.get(powerup);
    }
    
    @Override
    public void update(float delta) {
        Player player = parent.getPlayer();

        if (this.intersects(player.getX(), player.getY(), player.getWidth(), player.getHeight())) {
            player.setPowerup(powerup, time);
            removed = true;
        }
    }
}
