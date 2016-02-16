package com.superduckinvaders.game.tests;

import static org.junit.Assert.*;

import java.util.HashMap;

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
    
    @Test
    public void upgradeTest() {
		assertEquals(Player.Upgrade.NONE, testPlayer.getUpgrade());
		testPlayer.setUpgrade(Player.Upgrade.GUN);
		assertEquals(Player.Upgrade.GUN, testPlayer.getUpgrade());
    }

	@Test
	public void invulnTest() {
		testPlayer.setPowerup(Player.Powerup.INVULNERABLE, 1);
		actualPowerups = testPlayer.getPowerups();
		expectedPowerups.put(Player.Powerup.INVULNERABLE, 1.0);
		
		assertEquals(actualPowerups, expectedPowerups);
	}
	
	@Test
	public void firerateTest() {
		testPlayer.update(1);
		
		testPlayer.setPowerup(Player.Powerup.RATE_OF_FIRE, 1);
		actualPowerups = testPlayer.getPowerups();
		expectedPowerups.put(Player.Powerup.RATE_OF_FIRE, 1.0);
		
		assertEquals(actualPowerups, expectedPowerups);
	}
	
	@Test
	public void regenTest() {
		testPlayer.update(1);
		
		testPlayer.setPowerup(Player.Powerup.REGENERATION, 1);
		actualPowerups = testPlayer.getPowerups();
		expectedPowerups.put(Player.Powerup.REGENERATION, 1.0);
		
		assertEquals(actualPowerups, expectedPowerups);
	}
	
	@Test
	public void multiplierTest() {
		testPlayer.update(1);
		
		testPlayer.setPowerup(Player.Powerup.SCORE_MULTIPLIER, 1);
		actualPowerups = testPlayer.getPowerups();
		expectedPowerups.put(Player.Powerup.SCORE_MULTIPLIER, 1.0);
		
		assertEquals(actualPowerups, expectedPowerups);
	}
	
	@Test
	public void speedTest() {
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
