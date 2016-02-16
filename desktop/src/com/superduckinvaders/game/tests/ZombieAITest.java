package com.superduckinvaders.game.tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.superduckinvaders.game.DuckGame;
import com.superduckinvaders.game.ai.AI;
import com.superduckinvaders.game.ai.ZombieAI;
import com.superduckinvaders.game.assets.Assets;
import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.entity.Mob;
import com.superduckinvaders.game.entity.Player;

public class ZombieAITest {
	
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

	@Test
	public void movementTest() {
		int testPlayerX = (int)testRound.getPlayer().getX();
		int testPlayerY = (int)testRound.getPlayer().getY();
		Mob testMob = new Mob(testRound, testPlayerX + 50, testPlayerY, 100, Assets.badGuyNormal, 1, new ZombieAI(testRound, 32), true, false);
		
		while (testMob.getX() != testPlayerX + 49) {
			testMob.update(1);
		}

		assertEquals(testPlayerX + 49, (int) testMob.getX());
		assertEquals(testPlayerY, (int) testMob.getY());
	}
	
	@Test
	public void AttackTest(){
		int testPlayerX = (int)testRound.getPlayer().getX();
		int testPlayerY = (int)testRound.getPlayer().getY();
		Mob testMob = new Mob(testRound, testPlayerX, testPlayerY, 100, Assets.badGuyNormal, 10, new ZombieAI(testRound, 32), true, false);
		int currentHealth = testRound.getPlayer().getCurrentHealth();
		testMob.update(1);
		assertEquals(currentHealth-1, testRound.getPlayer().getCurrentHealth());
	}

}
