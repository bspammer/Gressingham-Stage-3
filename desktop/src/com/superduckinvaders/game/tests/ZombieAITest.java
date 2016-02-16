package com.superduckinvaders.game.tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.superduckinvaders.game.DuckGame;
import com.superduckinvaders.game.ai.AI;
import com.superduckinvaders.game.assets.Assets;
import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.entity.Mob;

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
		testRound = duckGame.getRound();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

//	@Test
//	public void MovementTest() {
//		int testPlayerX = (int)testRound.getPlayer().getX();
//		int testPlayerY = (int)testRound.getPlayer().getY();
//		Mob testMob1 = new Mob(testRound, testPlayerX+100, testPlayerY, 10, Assets.badGuyNormal, 10, AI.type.ZOMBIE, new int[]{20});
//		Mob testMob2 = new Mob(testRound, testPlayerX-100, testPlayerY, 10, Assets.badGuyNormal, 10, AI.type.ZOMBIE, new int[]{20});
//		testMob1.update(1);
//		testMob2.update(1);
//		assertEquals(testPlayerX+90, (int)testMob1.getX());
//		assertEquals(testPlayerY, (int)testMob1.getY());
//		assertEquals(testPlayerX-90, (int)testMob2.getX());
//		assertEquals(testPlayerY, (int)testMob2.getY());
//	}
//	
//	@Test
//	public void AttackTest(){
//		int testPlayerX = (int)testRound.getPlayer().getX();
//		int testPlayerY = (int)testRound.getPlayer().getY();
//		Mob testMob = new Mob(testRound, testPlayerX+50, testPlayerY, 10, Assets.badGuyNormal, 10, AI.type.ZOMBIE, new int[]{100});
//		int currentHealth = testRound.getPlayer().getCurrentHealth();
//		testMob.update(1);
//		assertEquals(currentHealth-1, testRound.getPlayer().getCurrentHealth());
//	}

}
