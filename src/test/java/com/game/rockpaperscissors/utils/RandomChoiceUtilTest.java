package com.game.rockpaperscissors.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.game.rockpaperscissors.exception.NotExistElementsException;
import com.game.rockpaperscissors.model.enums.Choice;
import com.game.rockpaperscissors.model.enums.RoundResult;

public class RandomChoiceUtilTest {

	/**
	 * Test get a random element of an Enum.
	 * 
	 * @throws NotExistElementsException
	 */
	@Test
	public void shouldGetRandomChoiceExistElementInEnum() 
			throws NotExistElementsException {
		String choice = RandomChoiceUtil.randomChoice(Choice.class).name();
		assertTrue(Arrays.stream(Choice.values())
				.filter(e -> e.name().equalsIgnoreCase(choice))
				.findAny().isPresent());
	}

	/**
	 * Test not get an element out of values of the Enum.
	 * 
	 * @throws NotExistElementsException
	 */
	@Test
	public void shouldNotGetElementIfNotExistElementInEnum() 
			throws NotExistElementsException {
		String choice = RandomChoiceUtil.randomChoice(RoundResult.class).name();
		assertFalse(Arrays.stream(Choice.values())
				.filter(e -> e.name().equalsIgnoreCase(choice))
				.findAny().isPresent());
	}

	/**
	 * Test throws exception when the enum is null.
	 * 
	 * @throws NotExistElementsException
	 */
	@Test(expected = NotExistElementsException.class)
	public void shouldThrowsNotExistElementsExceptionWhenNotExistElement() 
			throws NotExistElementsException {
		RandomChoiceUtil.randomChoice(null).name();
	}
}
