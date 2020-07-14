package com.game.rockpaperscissors.model;

import com.game.rockpaperscissors.model.enums.Choice;
import com.game.rockpaperscissors.model.enums.RoundResult;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Round {
	
	private @NonNull Choice playerOne;
	
	private @NonNull Choice playerTwo;
	
	private RoundResult result;

}
