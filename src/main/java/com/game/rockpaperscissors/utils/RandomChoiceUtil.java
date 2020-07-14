package com.game.rockpaperscissors.utils;

import java.util.concurrent.ThreadLocalRandom;

import com.game.rockpaperscissors.exception.NotExistElementsException;

/**
 * Utility class that return an random element of an enum class.
 */
public class RandomChoiceUtil {

	/**
	 * Method that return an random element of an enum class.
	 * 
	 * @param enumType Enum class.
	 * @return An element of enumType class.
	 * @throws NotExistElementsException
	 */
	public static <T extends Enum<?>> T randomChoice(Class<T> enumType) 
			throws NotExistElementsException {
		if (enumType == null) {
			throw new NotExistElementsException();
		}
		try {
			return enumType.getEnumConstants()[
			    ThreadLocalRandom.current().nextInt(
			    		enumType.getEnumConstants().length)];
		} catch (NullPointerException e) {
			throw new NotExistElementsException();
		}
	}
}
