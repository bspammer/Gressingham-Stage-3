package com.superduckinvaders.game.tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.superduckinvaders.game.DuckGame;
import com.superduckinvaders.game.assets.Assets;
import com.superduckinvaders.game.entity.Mob;
import com.superduckinvaders.game.entity.Player.Powerup;
import com.superduckinvaders.game.entity.Player.Upgrade;
import com.superduckinvaders.game.Round;

public class RoundTest {
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
		testRound = new Round(duckGame, Assets.levelOneMap);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//TODO Note there are issues with round testing due to concurrent computation of round in libGDX

	@Test
	public void spawningTest() {
		int expectedMobCount = testRound.getEntities().size();
		
		//check bad mob spawn
		assertEquals(false, testRound.createMob(-10, -10, 10, Assets.badGuyNormal, 10));
		assertEquals(expectedMobCount, testRound.getEntities().size());

		//check correct mob spawn
		assertEquals(true, testRound.createMob(100, 100, 10, Assets.badGuyNormal, 10));
		assertEquals(expectedMobCount + 1, testRound.getEntities().size());
		
		//include previous mob in entities before new tests
		expectedMobCount = testRound.getEntities().size();

		//check powerups and upgrades spawn
		testRound.createParticle(100, 100, 20, Assets.explosionAnimation);
		testRound.createPowerup(100, 100, Powerup.INVULNERABLE);
		testRound.createProjectile(100.0, 100.0, 200.0, 200.0, 10.0, 0.0, 0.0, 10, testRound.getPlayer());
		testRound.createUpgrade(100, 100, Upgrade.GUN);
		assertEquals(expectedMobCount + 4, testRound.getEntities().size());
	}

	@Test
	public void entityRemovalTest() {
		int expectedMobCount = testRound.getEntities().size();
		
		//add mob manually add entity
		Mob testMob = new Mob(testRound, 100, 100, 10, Assets.badGuyNormal, 10);
		testRound.addEntity(testMob);
		assertEquals(expectedMobCount + 1, testRound.getEntities().size());
		testRound.update(2);

		//kill mob and remove entity
		expectedMobCount = testRound.getEntities().size();
		testMob.damage(1000);
		testRound.update(2);
		int mobEntity = testRound.getEntities().indexOf(testMob);
		testRound.getEntities().remove(mobEntity);
		testRound.update(2);
		assertEquals(expectedMobCount - 1, testRound.getEntities().size());
		
	}

}
