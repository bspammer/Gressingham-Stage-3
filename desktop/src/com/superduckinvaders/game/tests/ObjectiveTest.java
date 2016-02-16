package com.superduckinvaders.game.tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.superduckinvaders.game.DuckGame;
import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.assets.Assets;
import com.superduckinvaders.game.entity.Player;
import com.superduckinvaders.game.entity.item.Item;
import com.superduckinvaders.game.objective.CollectObjective;
import com.superduckinvaders.game.objective.Objective;
import com.superduckinvaders.game.objective.SurviveObjective;

public class ObjectiveTest {
	
	
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
	
	private SurviveObjective testSurviveObjective;
	private int survivalTime = 2;
	
	private CollectObjective testCollectObjective;
	private Item collectObjItem;
	private double posX, posY;
	private Player player;
	
	public ObjectiveTest() {
		//initialise all collect objective fields
		this.posX = 1;
		this.posY = 1;
		this.player = new Player(testRound, posX, posY);
		this.collectObjItem = new Item(null, posX, posY, Assets.flag);
		this.testCollectObjective = new CollectObjective(testRound, collectObjItem);
		
		//initialise all survive objective fields
		this.testSurviveObjective = new SurviveObjective(testRound, survivalTime);
	}

	@Test
	public void testCollectObjective() {
		//points before completion
		int beforeScore = duckGame.getTotalScore();
		
		//test objective initialised correctly
		assertEquals("Locate and collect the red flag!" , testCollectObjective.getObjectiveString());
		assertEquals(Objective.OBJECTIVE_ONGOING, testCollectObjective.getStatus());
		
		//objective item placed in correct location
		int[] expected = {(int)posX, (int)posY};
		int[] actual = {(int)collectObjItem.getX(), (int)collectObjItem.getY()};
		assertArrayEquals(expected, actual);
		
		//test objective completion criteria
		if(collectObjItem.getX() == player.getX() && collectObjItem.getY() == player.getY()) {
			testCollectObjective.testingSetStatus(Objective.OBJECTIVE_COMPLETED);
		}
		
		//test objective actually complete
		assertEquals(Objective.OBJECTIVE_COMPLETED, testCollectObjective.getStatus());
		
		//test score has been added after completion
		testCollectObjective.update(1);
		int afterScore = duckGame.getTotalScore();
		assertEquals(beforeScore + 100, afterScore);
	}
	
	@Test
	public void testSurviveObjective() {
		//points before completion
		int beforeScore = duckGame.getTotalScore();
		
		//test objective initialised correctly
		assertEquals(testSurviveObjective.getBossSpawnTime(), survivalTime / 2);
		assertEquals("Survive for " + Integer.toString(this.survivalTime ) + " seconds to win!", testSurviveObjective.getObjectiveString());
		assertEquals(Objective.OBJECTIVE_ONGOING, testSurviveObjective.getStatus());
		
		//test time works
		testSurviveObjective.update(1);
		assertEquals(testSurviveObjective.getTimeRemaining(), survivalTime - 1);
		
		//test objective completion criteria
		testSurviveObjective.update(1);
		assertEquals(testSurviveObjective.getStatus(), Objective.OBJECTIVE_COMPLETED);
		
		//test score has been added after completion
		testSurviveObjective.update(1);
		int afterScore = duckGame.getTotalScore();
		assertEquals(beforeScore + 200, afterScore);
	}

}
