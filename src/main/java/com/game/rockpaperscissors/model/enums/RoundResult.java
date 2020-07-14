package com.game.rockpaperscissors.model.enums;

import lombok.Getter;

/**
 * Round Result enum.
 * 
 * @author Ines
 *
 */
@Getter
public enum RoundResult {

	WIN_ONE("Player 1 wins"),

	WIN_TWO("Player 2 wins"),

	DRAW("Draw");

	private String textResult;

	private RoundResult(String textResult) {
		this.textResult = textResult;
	}

}
