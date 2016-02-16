package com.superduckinvaders.game.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import com.superduckinvaders.game.entity.Mob;
public class CharacterTest {
	
	private Mob testSubject;
	
	public CharacterTest() {
		testSubject = new Mob(null, 120, 130, 5, null, 10);
	}

	@Test
	public void testConstructor() {
		int[] expected = {120, 130, 5};
		int[] actual = {(int)testSubject.getX(), (int)testSubject.getY(), testSubject.getMaximumHealth()};
		
		//character created successfully.
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testMaxHealth() {
		assertEquals(testSubject.getMaximumHealth(), testSubject.getCurrentHealth());
	}
	
	@Test
	public void testDamage() {
		//damaged correctly
		testSubject.heal(10);
		testSubject.damage(3);
		assertEquals(2, testSubject.getCurrentHealth());
	}
	
	@Test
	public void testHealing() {
		//healed correctly
		testSubject.heal(10);
		testSubject.damage(3);
		testSubject.heal(1);
		assertEquals(3, testSubject.getCurrentHealth());
	}
	
	@Test
	public void testHealFully() {
		//heal full health correctly
		testSubject.heal(10);
		assertEquals(5, testSubject.getCurrentHealth());
	}
	
	@Test
	public void testKill() {
		//heal fully then murder
		testSubject.heal(10);
		testSubject.damage(99999);
		assertEquals(true, testSubject.isDead());
	}
}
