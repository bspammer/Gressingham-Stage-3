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
	public void updateMovementTest() {
		Mob testSubject = new Mob(testRound, 20, 20, 5, Assets.badGuyNormal, 2);
		
		//x positive direction
		testSubject.setVelocity(1, 0);
		testSubject.update(1);
		int[] expectedCoord = {22, 20};
		int[] actualCoord = {(int)testSubject.getX(), (int)testSubject.getY()};
		assertArrayEquals(expectedCoord, actualCoord);
		
		//x negative direction, lag frames introduced (i.e. delta is higher than usual)
		testSubject.setVelocity(-1, 0);
		testSubject.update(2);
		expectedCoord[0]=18;
		expectedCoord[1]=20;
		actualCoord[0]=(int)testSubject.getX();
		actualCoord[1]=(int)testSubject.getY();
		assertArrayEquals(expectedCoord, actualCoord);
		
		//combine x and y directions, assume vector normalization before movement
		testSubject.setVelocity(3, 4);
		testSubject.update(5);
		expectedCoord[0] = 24;
		expectedCoord[1] = 28;
		actualCoord[0]=(int)testSubject.getX();
		actualCoord[1]=(int)testSubject.getY();
		assertArrayEquals(expectedCoord, actualCoord);
	}
}
		
