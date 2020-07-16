package com.game.rockpaperscissors.service;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.game.rockpaperscissors.exception.InvalidUserException;
import com.game.rockpaperscissors.exception.PlayRoundException;
import com.game.rockpaperscissors.model.Round;
import com.game.rockpaperscissors.model.enums.Choice;
import com.game.rockpaperscissors.model.enums.RoundResult;
import com.game.rockpaperscissors.repository.GamesRepository;

@RunWith(SpringRunner.class)
public class PlayGameServiceTest {

	@Mock
	private GamesRepository<Round> gamesRepository;

	@InjectMocks
	private PlayGameService playGameService;

	/**
	 * Test gets all the games of one user.
	 *  
	 * @throws InvalidUserException
	 */
	@Test
	public void shouldGetAllTheGamesOfTheUser() 
			throws InvalidUserException {
		List<Round> gamesUser1 = 
				Arrays.asList(getWINPLAYER1Round(), getWINPLAYER2Round());

		BDDMockito.given(gamesRepository.findAllGamesByUser("user1"))
			.willReturn(gamesUser1);

		List<Round> roundsUser1 = playGameService.getAll("user1");

		assertEquals(gamesUser1.size(), roundsUser1.size());
		assertEquals(gamesUser1.get(0), roundsUser1.get(0));
	}

	/**
	 * Test round played by user1 should not be the same as round played 
	 * by user2.
	 * 
	 * @throws InvalidUserException
	 */
	@Test
	public void shouldGetDifferentGamesByDifferentUsers() 
			throws InvalidUserException {
		List<Round> gamesUser1 = Arrays.asList(getWINPLAYER1Round());
		List<Round> gamesUser2 = Arrays.asList(getWINPLAYER2Round());

		BDDMockito.given(gamesRepository.findAllGamesByUser("user1"))
			.willReturn(gamesUser1);

		List<Round> roundsUser1 = playGameService.getAll("user1");

		assertNotEquals(roundsUser1.get(0), gamesUser2.get(0));
	}

	/**
	 * Test adds new game to user.
	 * 
	 * @throws PlayRoundException
	 * @throws InvalidUserException
	 */
	@Test
	public void shouldAddNewGameToUser() 
			throws PlayRoundException, InvalidUserException {
		playGameService.playNewGame("user1");

		BDDMockito.verify(gamesRepository, BDDMockito.times(1))
			.addGameToUser(BDDMockito.eq("user1"), BDDMockito.any(Round.class));
	}
	
	/**
	 * Test throws exception if user is null.
	 * 
	 * @throws PlayRoundException
	 * @throws InvalidUserException
	 */
	@Test(expected = InvalidUserException.class)
	public void shouldThrowsInvalidUserException() 
			throws PlayRoundException, InvalidUserException {
		BDDMockito.doThrow(InvalidUserException.class).when(gamesRepository)
			.addGameToUser(BDDMockito.eq(null), BDDMockito.any(Round.class));
		playGameService.playNewGame(null);
	}

	/**
	 * Test deletes games of a user.
	 * 
	 * @throws PlayRoundException
	 * @throws InvalidUserException
	 */
	@Test
	public void shouldDeleteGamesByUser() 
			throws PlayRoundException, InvalidUserException {
		playGameService.playNewGame("user1");
		playGameService.restartGame("user1");
		
		assertEquals(playGameService.getAll("user1").size(), 0);	
	}

	/**
	 * Test totals 0 games played.
	 * 
	 */
	@Test
	public void shouldCorrectTotalsOfCeroGamesPlayed() {
		BDDMockito.given(gamesRepository.getAllGames())
			.willReturn(new HashMap<>());
		
		assertEquals(playGameService.getTotals().get("totalRoundsPlayed"), 0);
		assertEquals(playGameService.getTotals().get("totalWinsPlayer1"), 0);
		assertEquals(playGameService.getTotals().get("totalWinsPlayer2"), 0);
		assertEquals(playGameService.getTotals().get("totalDraw"), 0);
	}
	
	/**
	 * Test totals all games played with all of them wining player1.
	 * 
	 */
	@Test
	public void shouldCorrectTotalsOfAllGamesPlayedWiningPlayer1() {
		Map<String, List<Round>> games = new HashMap<>();

		List<Round> roundsUser1 = Arrays.asList(
				getWINPLAYER1Round(), getWINPLAYER1Round(), 
				getWINPLAYER1Round(), getWINPLAYER1Round());
		List<Round> roundsUser2 = Arrays.asList(
				getWINPLAYER1Round(), getWINPLAYER1Round());
		
		games.put("user1", roundsUser1);
		games.put("user2", roundsUser2);
		games.put("user3", roundsUser1);
		
		BDDMockito.given(gamesRepository.getAllGames())
			.willReturn(games);
		
		assertEquals(playGameService.getTotals().get("totalRoundsPlayed"), 10);
		assertEquals(playGameService.getTotals().get("totalWinsPlayer1"), 10);
		assertEquals(playGameService.getTotals().get("totalWinsPlayer2"), 0);
		assertEquals(playGameService.getTotals().get("totalDraw"), 0);
	}
	
	/**
	 * Test totals all games played with all of them wining player2.
	 * 
	 */
	@Test
	public void shouldCorrectTotalsOfAllGamesPlayedWiningPlayer2() {
		Map<String, List<Round>> games = new HashMap<>();

		List<Round> roundsUser1 = Arrays.asList(
				getWINPLAYER2Round(), getWINPLAYER2Round(), 
				getWINPLAYER2Round(), getWINPLAYER2Round());
		List<Round> roundsUser2 = Arrays.asList(
				getWINPLAYER2Round(), getWINPLAYER2Round());
		
		games.put("user1", roundsUser1);
		games.put("user2", roundsUser2);
		games.put("user3", roundsUser1);
		games.put("user4", new ArrayList<>());
		
		BDDMockito.given(gamesRepository.getAllGames())
			.willReturn(games);
		
		assertEquals(playGameService.getTotals().get("totalRoundsPlayed"), 10);
		assertEquals(playGameService.getTotals().get("totalWinsPlayer1"), 0);
		assertEquals(playGameService.getTotals().get("totalWinsPlayer2"), 10);
		assertEquals(playGameService.getTotals().get("totalDraw"), 0);
	}
	
	/**
	 * Test totals ten games played.
	 * 
	 */
	@Test
	public void shouldCorrectTotalsOfGamesPlayed() {
		Map<String, List<Round>> games = new HashMap<>();

		List<Round> roundsUser1 = Arrays.asList(
				getDRAWRound(),
				getWINPLAYER1Round(), 
				getWINPLAYER2Round(), getWINPLAYER2Round());
		List<Round> roundsUser2 = Arrays.asList(
				getWINPLAYER1Round(), 
				getWINPLAYER2Round());
		
		games.put("user1", roundsUser1);
		games.put("user2", roundsUser2);
		games.put("user3", roundsUser1);
		
		BDDMockito.given(gamesRepository.getAllGames())
			.willReturn(games);
		
		assertEquals(playGameService.getTotals().get("totalRoundsPlayed"), 10);
		assertEquals(playGameService.getTotals().get("totalWinsPlayer1"), 3);
		assertEquals(playGameService.getTotals().get("totalWinsPlayer2"), 5);
		assertEquals(playGameService.getTotals().get("totalDraw"), 2);
	}

	/**
	 * Get a round with Draw result.
	 * 
	 * @return Round
	 */
	private Round getDRAWRound() {
		Round round = new Round(Choice.ROCK, Choice.ROCK);
		round.setResult(RoundResult.DRAW);
		return round;
	}
	
	/**
	 * Get a round with win for player 1.
	 * 
	 * @return Round
	 */
	private Round getWINPLAYER1Round() {
		Round round = new Round(Choice.PAPER, Choice.ROCK);
		round.setResult(RoundResult.WIN_ONE);
		return round;
	}
	
	/**
	 * Get a round with win for player 2.
	 * 
	 * @return Round
	 */
	private Round getWINPLAYER2Round() {
		Round round = new Round(Choice.SCISSORS, Choice.ROCK);
		round.setResult(RoundResult.WIN_TWO);
		return round;
	}
}
