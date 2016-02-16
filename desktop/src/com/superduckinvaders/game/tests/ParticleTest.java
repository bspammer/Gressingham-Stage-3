package com.superduckinvaders.game.tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.superduckinvaders.game.DuckGame;
import com.superduckinvaders.game.entity.Particle;
import com.superduckinvaders.game.Round;

public class ParticleTest {

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
	public void durationTest() {
		Particle testParticle = new Particle(testRound, 100, 100, 1, null);
		assertEquals(false, testParticle.isRemoved());
		testParticle.update((float) 0.9);
		assertEquals(false, testParticle.isRemoved());
		testParticle.update((float) 0.2);
		assertEquals(true, testParticle.isRemoved());
	}

}
