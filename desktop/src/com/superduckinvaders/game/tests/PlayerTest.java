package com.superduckinvaders.game.tests;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.superduckinvaders.game.DuckGame;
import com.superduckinvaders.game.entity.Player;
import com.superduckinvaders.game.entity.Player.Powerup;
import com.superduckinvaders.game.Round;

public class PlayerTest {
	
	protected static DuckGame duckGame;
	protected static Round testRound;
	
	private HashMap<Powerup, Double> actualPowerups, expectedPowerups;
	private Player testPlayer;
 
    @BeforeClass
    public static void setUp() {
    	duckGame = new DuckGame();
 
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.resizable = false;
		config.title = "SUPER DUCK INVADERS! - Team Mallard";
		new LwjglApplication(duckGame, config);
        
		while (duckGame.onGameScreen == false) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		testRound = duckGame.getRound();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public PlayerTest() {
		this.testPlayer = testRound.getPlayer();
		this.actualPowerups = testPlayer.getPowerups();
		this.expectedPowerups = testPlayer.getPowerups();
    }
    
    @Before
    public void clearPowerups() {
    	for (Powerup powerup : Player.Powerup.values()) {
    		testPlayer.setPowerup(powerup, 0);
    	}
    }
    
    @Test
    public void upgradeTest() {
    	testPlayer.update(1);
    	
		assertEquals(Player.Upgrade.NONE, testPlayer.getUpgrade());
		testPlayer.setUpgrade(Player.Upgrade.GUN);
		assertEquals(Player.Upgrade.GUN, testPlayer.getUpgrade());
    }

	@Test
	public void setInvulnPowerupTest() {
		testPlayer.update(1);
		
		testPlayer.setPowerup(Player.Powerup.INVULNERABLE, 1);
		actualPowerups = testPlayer.getPowerups();
		expectedPowerups.put(Player.Powerup.INVULNERABLE, 1.0);
		
		assertEquals(actualPowerups, expectedPowerups);
	}
	
	@Test
	public void invulnPowerupShouldPreventDamage() {
		testPlayer.update(1);
		int startHealth = testPlayer.getCurrentHealth();
		
		testPlayer.setPowerup(Player.Powerup.INVULNERABLE, 5);
		testPlayer.damage(5);
		
		int endHealth = testPlayer.getCurrentHealth();
		
		assertEquals(startHealth, endHealth);
	}
	
	@Test
	public void setFireratePowerupTest() {
		testPlayer.setPowerup(Player.Powerup.RATE_OF_FIRE, 1);
		actualPowerups = testPlayer.getPowerups();
		expectedPowerups.put(Player.Powerup.RATE_OF_FIRE, 1.0);
		
		assertEquals(actualPowerups, expectedPowerups);
	}
	
	@Test
	public void setRegenPowerupTest() {
		testPlayer.update(1);
		
		testPlayer.setPowerup(Player.Powerup.REGENERATION, 1);
		actualPowerups = testPlayer.getPowerups();
		expectedPowerups.put(Player.Powerup.REGENERATION, 1.0);
		
		assertEquals(actualPowerups, expectedPowerups);
	}
	
	@Test
	public void regenPowerupShouldRegenerateHealth() {
		int startHealth = testPlayer.getCurrentHealth();
		if (startHealth == Player.PLAYER_HEALTH) {
			testPlayer.damage(1);
		}
		
		testPlayer.setPowerup(Player.Powerup.REGENERATION, 100);
		testPlayer.update(50);
		testPlayer.update(50);
		testPlayer.update(50);
		
		int endHealth = testPlayer.getCurrentHealth();
		System.out.println(endHealth);
		
		//Player should definitely be at max health after 100 seconds of regen
		assertEquals(endHealth, Player.PLAYER_HEALTH);
		
	}
	
	@Test
	public void setMultiplierPowerupTest() {
		testPlayer.update(1);
		
		testPlayer.setPowerup(Player.Powerup.SCORE_MULTIPLIER, 1);
		actualPowerups = testPlayer.getPowerups();
		expectedPowerups.put(Player.Powerup.SCORE_MULTIPLIER, 1.0);
		
		assertEquals(actualPowerups, expectedPowerups);
	}
	
	@Test
	public void setSpeedPowerupTest() {
		testPlayer.update(1);
		
		testPlayer.setPowerup(Player.Powerup.SUPER_SPEED, 1);
		actualPowerups = testPlayer.getPowerups();
		expectedPowerups.put(Player.Powerup.SUPER_SPEED, 1.0);
		
		assertEquals(actualPowerups, expectedPowerups);
	}
	
	@Test
	public void scoreTest() {
		assertEquals(0, testPlayer.getScore());
		testPlayer.addScore(10);
		assertEquals(10, testPlayer.getScore());
	}

}
