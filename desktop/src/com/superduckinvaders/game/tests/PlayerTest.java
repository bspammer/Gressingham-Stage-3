package com.superduckinvaders.game.tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.superduckinvaders.game.DuckGame;
import com.superduckinvaders.game.entity.Player;
import com.superduckinvaders.game.Round;

public class PlayerTest {
	protected static DuckGame duckGame;
	protected static Round testRound;
 
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

	@Test
	public void upgradeTest() {
		Player testPlayer = testRound.getPlayer();
		testPlayer.setPowerup(Player.Powerup.INVULNERABLE, 1);
		assertEquals(Player.Powerup.INVULNERABLE, testPlayer.getPowerup());
		testPlayer.update(1);
		testPlayer.update(1);
		assertEquals(Player.Powerup.NONE, testPlayer.getPowerup());
		assertEquals(Player.Upgrade.NONE, testPlayer.getUpgrade());
		testPlayer.setUpgrade(Player.Upgrade.GUN);
		assertEquals(Player.Upgrade.GUN, testPlayer.getUpgrade());
	}

}
