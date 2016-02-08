package com.superduckinvaders.game.objective;

import com.badlogic.gdx.Gdx;
import com.superduckinvaders.game.Round;
import com.superduckinvaders.game.entity.Player;

public class SurviveObjective extends Objective {
	
	/**
	 * The amount of time the player must survive for.
	 */
	private int time;
	
	private float accum;

	public SurviveObjective(Round parent, int time) {
		super(parent);
		this.time = time;
	}

	@Override
	public String getObjectiveString() {
		return "Survive for " + Integer.toString(time) + " seconds to win!";
	}

	@Override
	public void update(float delta) {
		
		accum += delta;
		if (accum >= 1) {
			System.out.println(time);
			time--;
			accum = 0;
		}
		
		//if timer ran out objective completed
		if(time <= 0) {
			status = OBJECTIVE_COMPLETED;
		}
	}

}
