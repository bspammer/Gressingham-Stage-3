package com.superduckinvaders.game.tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.superduckinvaders.game.DuckGame;
import com.superduckinvaders.game.assets.Assets;
import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.entity.Mob;
import com.superduckinvaders.game.entity.Projectile;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class ProjectileTest {
	
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
	public void projectileMovementTest() {

		//simple movement tests
		//x direction test
		Mob testMob = new Mob(testRound, 300, 300, 5, Assets.badGuyNormal, 100);
		Projectile testProjectile = new Projectile(testRound, testMob.getX()+100, testMob.getY()+100, testMob.getX()+100, testMob.getY()+100, 10, 10, testMob);
		int[] expectedCoord = new int[] {400, 400};
		int[] actualCoord = new int[]{(int)testProjectile.getX(), (int)testProjectile.getY()};
		assertArrayEquals(expectedCoord, actualCoord);
		testProjectile.update(1);
		expectedCoord = new int[] {410, 400};
		actualCoord = new int[] {(int)testProjectile.getX(), (int)testProjectile.getY()};
		assertArrayEquals(expectedCoord, actualCoord);
		
		//y direction test
		testProjectile = new Projectile(testRound, testMob.getX(), testMob.getY(), testMob.getX(), testMob.getY()+100, 10, 10, testMob);
		testProjectile.update(1);
		expectedCoord = new int[]{300, 310};
		actualCoord = new int[]{(int)testProjectile.getX(), (int)testProjectile.getY()};
		assertArrayEquals(expectedCoord, actualCoord);

		}
	
	@Test
	public void projectileCollisionTest() {
		Mob testMob = new Mob(testRound, 300, 300, 5, Assets.badGuyNormal, 100);
		Projectile testProjectile = new Projectile(testRound, testMob.getX()+100, testMob.getY()+100, testMob.getX()+100, testMob.getY()+100, 10, 10, testMob);
		testRound.addEntity(testMob);
		
		//collision detection tests
		//move the mob to some boundary of the map
		testMob.setVelocity(-1, 0);
		for (int i =0; i<100; i++){
			testMob.update((float)0.1);
		}
		
		//the mob should be stuck colliding against something
		testProjectile = new Projectile(testRound, testMob.getX(), testMob.getY() + 100, testMob.getX(), testMob.getY(), 100, 100, null);
		//fire projectile at mob and check it is removed
		testProjectile.update(1);
		assertEquals(true, testProjectile.isRemoved());

	}

}
