package com.game.rockpaperscissors.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.game.rockpaperscissors.exception.InvalidUserException;
import com.game.rockpaperscissors.exception.NotExistElementsException;
import com.game.rockpaperscissors.exception.PlayRoundException;
import com.game.rockpaperscissors.model.Round;
import com.game.rockpaperscissors.model.enums.Choice;
import com.game.rockpaperscissors.model.enums.RoundResult;
import com.game.rockpaperscissors.repository.GamesRepository;
import com.game.rockpaperscissors.utils.RandomChoiceUtil;

/**
 * Play Game Service.
 * 
 * @author Ines
 *
 */
@Service
public class PlayGameService {

	private GamesRepository<Round> gamesRepository;

	public PlayGameService(GamesRepository<Round> gamesRepository) {
		this.gamesRepository = gamesRepository;
	}

	/**
	 * Get all the games by user.
	 * 
	 * @param user User
	 * @return List of Round by user.
	 * @throws InvalidUserException 
	 */
	public List<Round> getAll(String user) throws InvalidUserException {
		return gamesRepository.findAllGamesByUser(user);
	}

	/**
	 * User play a new round.
	 * 
	 * @param user User
	 * @throws PlayRoundException
	 * @throws InvalidUserException 
	 */
	public void playNewGame(String user) 
			throws PlayRoundException, InvalidUserException {
		Round round;
		try {
			round = new Round(RandomChoiceUtil.randomChoice(Choice.class), 
					Choice.ROCK);
		} catch (NotExistElementsException e) {
			throw new 
				PlayRoundException("An error has occurred. Please try again.");
		}
		gamesRepository.addGameToUser(user, getResultRound(round));
	}

	/**
	 * Delete all the games of the user.
	 * 
	 * @param user User
	 * @throws InvalidUserException 
	 */
	public void restartGame(String user) throws InvalidUserException {
		gamesRepository.deleteGamesUser(user);
	}

	/**
	 * Get a round with the result of player1, player2 game.
	 * 
	 * @param round Round.
	 * @return Round with result.
	 */
	private Round getResultRound(Round round) {
		if (round != null) {
			if (round.getPlayerOne().equals(round.getPlayerTwo())) {
				round.setResult(RoundResult.DRAW);
			} else if (round.getPlayerOne().equals(Choice.SCISSORS)) {
				if (round.getPlayerTwo().equals(Choice.ROCK)) {
					round.setResult(RoundResult.WIN_TWO);
				} else {
					round.setResult(RoundResult.WIN_ONE);
				}
			} else if (round.getPlayerOne().equals(Choice.PAPER)) {
				if (round.getPlayerTwo().equals(Choice.ROCK)) {
					round.setResult(RoundResult.WIN_ONE);
				} else {
					round.setResult(RoundResult.WIN_TWO);
				}
			} else if (round.getPlayerOne().equals(Choice.ROCK)) {
				if (round.getPlayerTwo().equals(Choice.PAPER)) {
					round.setResult(RoundResult.WIN_TWO);
				} else {
					round.setResult(RoundResult.WIN_ONE);
				}
			}
		}
		return round;
	}

}
