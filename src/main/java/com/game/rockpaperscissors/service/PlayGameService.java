package com.game.rockpaperscissors.service;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	 * Get a map with the information of totals of round and rounds results.
	 * 
	 * @return Map<String, Integer> Map with totals.
	 */
	public Map<String, Integer> getTotals() {
		Map<String, List<Round>> games = gamesRepository.getAllGames();
		Map<String, Integer> totals = new HashMap<>();
		totals.put("totalRoundsPlayed", getTotalRoundsPlayed(games));
		Map<RoundResult, Long> totalWins = getTotalPlayersWinByResult(games);
		totals.put("totalWinsPlayer1", 
				totalWins.get(RoundResult.WIN_ONE).intValue());
		totals.put("totalWinsPlayer2", 
				totalWins.get(RoundResult.WIN_TWO).intValue());
		totals.put("totalDraw", 
				totalWins.get(RoundResult.DRAW).intValue());
		return totals;
	}
	

	/**
	 * Get total rounds played.
	 * 
	 * @param games All the games of all the players.
	 * 
	 * @return int Total rounds played.
	 */
	private int getTotalRoundsPlayed(Map<String, List<Round>> games) {
		return games.entrySet().stream()
		 			.mapToInt(gamesUser -> gamesUser.getValue().size()).sum();
	}

	/**
	 * Get total wins for the players.
	 * 
	 * @param games All the games of all the players.
	 * @return Map<RoundResult, Long> with total wins for each player
	 */
	private Map<RoundResult, Long> getTotalPlayersWinByResult(
			Map<String, List<Round>> games) {
		Map<RoundResult, Long> totalPlayersWinByResult = 
				games.entrySet().stream()
					.map(entry -> entry.getValue())
					.flatMap(List::stream)
					.collect(Collectors.groupingBy(Round::getResult, 
							Collectors.counting()));
		
		// If any player never wins
		EnumSet.allOf(RoundResult.class)
			.forEach(roundResult -> totalPlayersWinByResult
					.computeIfAbsent(roundResult, value -> 0L));
		
		return totalPlayersWinByResult;
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
