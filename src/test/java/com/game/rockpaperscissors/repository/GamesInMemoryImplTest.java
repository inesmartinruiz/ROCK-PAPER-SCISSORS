package com.game.rockpaperscissors.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.game.rockpaperscissors.exception.InvalidUserException;
import com.game.rockpaperscissors.model.Round;
import com.game.rockpaperscissors.model.enums.Choice;
import com.game.rockpaperscissors.model.enums.RoundResult;

public class GamesInMemoryImplTest {

	private GamesInMemoryImpl<Round> gamesRepository;

	@Before
	public void addAGameToUser() throws InvalidUserException {
		gamesRepository = new GamesInMemoryImpl<>();
	}

	/**
	 * Test that a round of a user is in the games of the user.
	 * 
	 * @throws InvalidUserException 
	 */
	@Test
	public void shouldFindGameInGamesByUser() throws InvalidUserException {
		addRound_PAPER_ROCK_GamesUser1();
		
		List<Round> gamesUser1 = gamesRepository.findAllGamesByUser("user1");

		assertEquals(gamesUser1.size(), 1);
		assertEquals(gamesUser1.get(0).getPlayerOne(), Choice.PAPER);
		assertEquals(gamesUser1.get(0).getPlayerTwo(), Choice.ROCK);
		assertEquals(gamesUser1.get(0).getResult(), RoundResult.WIN_ONE);
	}

	/**
	 * Test that a round is not in the games of another user.
	 * 
	 * @throws InvalidUserException 
	 */
	@Test
	public void shouldNotFindGameInGamesOfDifferentUser() 
			throws InvalidUserException {
		addRound_PAPER_ROCK_GamesUser1();
		addRound_SCISSORS_ROCK_GamesUser2();
		
		List<Round> gamesUser1 = gamesRepository.findAllGamesByUser("user1");
		List<Round> gamesUser2 = gamesRepository.findAllGamesByUser("user2");

		assertNotEquals(gamesUser1.get(0).getPlayerOne(), 
				gamesUser2.get(0).getPlayerOne());
	}

	/**
	 * Test that a round is added in the games of the user.
	 * 
	 * @throws InvalidUserException 
	 */
	@Test
	public void shouldAddGameToUser() throws InvalidUserException {
		addRound_PAPER_ROCK_GamesUser1();
		
		Round roundDraw = new Round(Choice.ROCK, Choice.ROCK);
		roundDraw.setResult(RoundResult.DRAW);
		gamesRepository.addGameToUser("user1", roundDraw);

		assertEquals(gamesRepository.findAllGamesByUser("user1").size(), 2);
		assertTrue(gamesRepository.findAllGamesByUser("user1")
				.contains(roundDraw));
	}

	/**
	 * Test that a round is not added in the games of another user.
	 * 
	 * @throws InvalidUserException 
	 */
	@Test
	public void shouldNotAddGameToDifferentUser() throws InvalidUserException {
		addRound_PAPER_ROCK_GamesUser1();
		addRound_SCISSORS_ROCK_GamesUser2();
		
		Round roundDraw = new Round(Choice.ROCK, Choice.ROCK);
		roundDraw.setResult(RoundResult.DRAW);
		gamesRepository.addGameToUser("user1", roundDraw);

		assertFalse(gamesRepository.findAllGamesByUser("user2")
				.contains(roundDraw));
	}

	/**
	 * Test that all the games of the user are deleted.
	 * @throws InvalidUserException 
	 */
	@Test
	public void shouldDeleteAllGamesUser() throws InvalidUserException {
		addRound_PAPER_ROCK_GamesUser1();
		
		gamesRepository.deleteGamesUser("user1");
		assertEquals(gamesRepository.findAllGamesByUser("user1").size(), 0);
	}

	/**
	 * Test that all the games of the another user are not deleted.
	 * 
	 * @throws InvalidUserException 
	 */
	@Test
	public void shouldNotDeleteAllGamesOfDifferentUser() 
			throws InvalidUserException {
		addRound_PAPER_ROCK_GamesUser1();
		addRound_SCISSORS_ROCK_GamesUser2();
		
		gamesRepository.deleteGamesUser("user2");
		assertEquals(gamesRepository.findAllGamesByUser("user1").size(), 1);
	}
	
	/**
	 * Test that an invalid user throws an InvalidUserException
	 * 
	 * @throws InvalidUserException 
	 */
	@Test(expected = InvalidUserException.class)
	public void shouldThrowsInvalidUserExceptionAnInvalidUser() 
			throws InvalidUserException {
		gamesRepository.findAllGamesByUser(null);
	}

	/**
	 * Add a round(PAPER, ROCK) to the games of User1.
	 * @throws InvalidUserException 
	 */
	private void addRound_PAPER_ROCK_GamesUser1() throws InvalidUserException {
		Round roundPlayer1Win = new Round(Choice.PAPER, Choice.ROCK);
		roundPlayer1Win.setResult(RoundResult.WIN_ONE);

		gamesRepository.addGameToUser("user1", roundPlayer1Win);
	}

	/**
	 * Add a round(SCISSORS, ROCK) to the games of User2.
	 * @throws InvalidUserException 
	 */
	private void addRound_SCISSORS_ROCK_GamesUser2() 
			throws InvalidUserException {
		Round roundPlayer2Win = new Round(Choice.SCISSORS, Choice.ROCK);
		roundPlayer2Win.setResult(RoundResult.WIN_TWO);

		gamesRepository.addGameToUser("user2", roundPlayer2Win);
	}

}
