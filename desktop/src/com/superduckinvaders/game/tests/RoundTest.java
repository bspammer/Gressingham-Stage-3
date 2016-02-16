package com.superduckinvaders.game.tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.superduckinvaders.game.DuckGame;
import com.superduckinvaders.game.assets.Assets;
import com.superduckinvaders.game.entity.Mob;
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
		testRound = duckGame.getRound();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    //TODO Note there are issues with round testing due to concurrent computation of round in libGDX
    
	@Test
	public void spawningTest() {
		int currentMobCount = testRound.getEntities().size();
		//check bad mob spawn
		assertEquals(false, testRound.createMob(-10, -10, 10, Assets.badGuyNormal, 10));
		assertEquals(currentMobCount, testRound.getEntities().size());
		
		//check correct mob spawn
		assertEquals(true, testRound.createMob(100, 100, 10, Assets.badGuyNormal, 10));
		assertEquals(currentMobCount+1, testRound.getEntities().size());
		
		//add mob manually, kill it and see if it despawns
		Mob testMob = new Mob(testRound, 100, 100, 10, Assets.badGuyNormal, 10);
		testRound.addEntity(testMob);
		assertEquals(currentMobCount+2, testRound.getEntities().size());
		testRound.update(1);
		testRound.update(1);
		currentMobCount = testRound.getEntities().size();
		testMob.damage(1000);
		//give it two frames to catch up
		testRound.update(1);
		testRound.update(1);
		
//		assertEquals(currentMobCount+1, testRound.getEntities().size());
//		testRound.createParticle(100, 100, 20, null);
//		testRound.createPowerup(100, 100, null, 100);
//		testRound.createProjectile(100.0, 100.0, 200.0, 200.0, 10.0, 0.0, 0.0, 10, testRound.getPlayer());
//		testRound.createUpgrade(100, 100, null);
//		assertEquals(currentMobCount+5, testRound.getEntities().size());
	}

}
