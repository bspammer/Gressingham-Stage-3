package com.superduckinvaders.game.tests;

import static org.junit.Assert.*;

import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.entity.Mob;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.superduckinvaders.game.DuckGame;
import com.superduckinvaders.game.assets.Assets;

import org.junit.BeforeClass;
import org.junit.Test;

public class EntityTest {
	
	protected static DuckGame duckGame;
	protected static Round testRound;

	private Mob testMob;

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
	public void moveXTest() {
		testMob = new Mob(testRound, 20, 20, 5, Assets.badGuyNormal, 2);

		//x positive direction
		testMob.update(1);
		testMob.setVelocity(1, 0);
		testMob.update(1);
		int[] expectedCoord = {22, 20};
		int[] actualCoord = {(int) testMob.getX(), (int) testMob.getY()};
		assertArrayEquals(expectedCoord, actualCoord);

		//x negative direction, lag frames introduced (i.e. delta is higher than usual)
		testMob.setVelocity(-1, 0);
		testMob.update(2);
		expectedCoord[0] = 18;
		expectedCoord[1] = 20;
		actualCoord[0] = (int) testMob.getX();
		actualCoord[1] = (int) testMob.getY();
		assertArrayEquals(expectedCoord, actualCoord);
	}

	@Test
	public void moveYTest() {
		testMob = new Mob(testRound, 20, 20, 5, Assets.badGuyNormal, 2);

		//x positive direction
		testMob.update(1);
		testMob.setVelocity(0, 1);
		testMob.update(1);
		int[] expectedCoord = {20, 22};
		int[] actualCoord = {(int) testMob.getX(), (int) testMob.getY()};
		assertArrayEquals(expectedCoord, actualCoord);

		//x negative direction, lag frames introduced (i.e. delta is higher than usual)
		testMob.setVelocity(0, -1);
		testMob.update(2);
		expectedCoord[0] = 20;
		expectedCoord[1] = 18;
		actualCoord[0] = (int) testMob.getX();
		actualCoord[1] = (int) testMob.getY();
		assertArrayEquals(expectedCoord, actualCoord);
	}

	@Test
	public void moveXYTest() {
		testMob = new Mob(testRound, 20, 20, 5, Assets.badGuyNormal, 2);

		//xy pos direction
		testMob.update(1);
		testMob.setVelocity(1, 1);
		testMob.update(1);
		int[] expectedCoord = {21, 21};
		int[] actualCoord = {(int) testMob.getX(), (int) testMob.getY()};
		assertArrayEquals(expectedCoord, actualCoord);

		//x neg y pos direction
		testMob.setVelocity(-1, 1);
		testMob.update(1);
		expectedCoord[0] = 20;
		expectedCoord[1] = 22;
		actualCoord[0] = (int) testMob.getX();
		actualCoord[1] = (int) testMob.getY();
		assertArrayEquals(expectedCoord, actualCoord);

		//x pos y neg direction
		testMob.setVelocity(1, -1);
		testMob.update(1);
		expectedCoord[0] = 21;
		expectedCoord[1] = 21;
		actualCoord[0] = (int) testMob.getX();
		actualCoord[1] = (int) testMob.getY();
		assertArrayEquals(expectedCoord, actualCoord);

		//x neg y neg direction
		testMob.setVelocity(-1, -1);
		testMob.update(1);
		expectedCoord[0] = 20;
		expectedCoord[1] = 20;
		actualCoord[0] = (int) testMob.getX();
		actualCoord[1] = (int) testMob.getY();
		assertArrayEquals(expectedCoord, actualCoord);
	}
}

