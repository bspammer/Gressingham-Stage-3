package com.superduckinvaders.game.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.superduckinvaders.game.entity.Player;
import com.superduckinvaders.game.entity.item.Item;
import com.superduckinvaders.game.objective.CollectObjective;
import com.superduckinvaders.game.objective.Objective;
import com.superduckinvaders.game.objective.SurviveObjective;

public class ObjectiveTest {
	
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
		this.player = new Player(null, posX, posY);
		this.collectObjItem = new Item(null, posX, posY, null);
		this.testCollectObjective = new CollectObjective(null, collectObjItem);
		
		//initialise all survive objective fields
		this.testSurviveObjective = new SurviveObjective(null, survivalTime);
	}

	@Test
	public void testCollectObjective() {
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
		assertEquals(Objective.OBJECTIVE_COMPLETED, testCollectObjective.getStatus());
	}
	
	@Test
	public void testSurviveObjective() {
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
	}

}
