package com.superduckinvaders.game.objective;

import com.superduckinvaders.game.Round;

/**
 * Represents an objective involving the player surviving for an amount of time.
 */
public class SurviveObjective extends Objective {

	/**
	 * The amount of time the player must survive for.
	 */
	private int time;

	/**
	 * Accumulation variable to count the time passed based on delta time.
	 */
	private float accum;

	/**
	 * Initialises this SurviveObjective.
	 *
	 * @param Rparent the round this CollectObjective belongs to.
	 * @param time represents the total time the player must survive to complete the obejective.
	 */
	public SurviveObjective(Round parent, int time) {
		super(parent);
		this.time = time;
	}

	/**
	 * Gets a string describing this SurviveObjective to be printed on screen.
	 *
	 * @return a string describing this SurviveObjective
	 */
	@Override
	public String getObjectiveString() {
		return "Survive for " + Integer.toString(time) + " seconds to win!";
	}

	/**
	 * Updates the status towards this SurviveObjective.
	 *
	 * @param delta how much time has passed since the last update.
	 */
	@Override
	public void update(float delta) {
		
		accum += delta;
		if (accum >= 1) {
			time--;
			accum = 0;
		}

		//if timer ran out objective completed
		if(time <= 0) {
			status = OBJECTIVE_COMPLETED;
		}
		
	}

}
