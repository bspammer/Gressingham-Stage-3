package com.superduckinvaders.game.objective;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.superduckinvaders.game.assets.Assets;

public class AnimatedText {
	/**
	 * Time the text will be on the screen in seconds
	 */
	private static final float LIFETIME = 1f;
	/**
	 * Value to control how much the text moves upwards over its lifetime
	 */
	private static final float MOVEMENT_AMOUNT = 20;
	private String text;
	private float currentX, currentY;
	private float displayTimer;
	private Color colour;
	
	/**
	 * Initialises an instance of AnimatedText with given parameters.
	 * @param text The text to display.
	 * @param x The x position of the text.
	 * @param y The y position of the text.
	 * @param colour The colour of the text.
	 */
	public AnimatedText(String text, float x, float y, Color colour) {
		this.text = text;
		this.currentX = x;
		this.currentY = y;
		this.colour = colour;
		this.displayTimer = LIFETIME;
	}

	/**
	 * Draws the animated text with correct offset position on the SpriteBatch
	 * @param spriteBatch The SpriteBatch to draw to
	 * @param delta Time since the last draw call
	 * @return true if text has time remaining, false otherwise
	 */
	public boolean draw(SpriteBatch spriteBatch, float delta) {
		if (displayTimer > 0) {
			currentY += MOVEMENT_AMOUNT * delta/LIFETIME;
			
			Color temp = Assets.font.getColor();
			Assets.font.setColor(colour);
			Assets.font.draw(spriteBatch, text, currentX, currentY);
			Assets.font.setColor(temp);
			displayTimer -= delta;
			return true;
		}
		return false;
	}
}
