package com.game.rockpaperscissors.service;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.game.rockpaperscissors.exception.InvalidUserException;
import com.game.rockpaperscissors.exception.PlayRoundException;
import com.game.rockpaperscissors.model.Round;
import com.game.rockpaperscissors.model.enums.Choice;
import com.game.rockpaperscissors.model.enums.RoundResult;
import com.game.rockpaperscissors.repository.GamesRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
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
		Round roundPlayer1Win = new Round(Choice.PAPER, Choice.ROCK);
		roundPlayer1Win.setResult(RoundResult.WIN_ONE);
		Round roundPlayer2Win = new Round(Choice.SCISSORS, Choice.ROCK);
		roundPlayer2Win.setResult(RoundResult.WIN_TWO);

		List<Round> gamesUser1 = 
				Arrays.asList(roundPlayer1Win, roundPlayer2Win);

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
		Round roundPlayer1Win = new Round(Choice.PAPER, Choice.ROCK);
		roundPlayer1Win.setResult(RoundResult.WIN_ONE);
		Round roundPlayer2Win = new Round(Choice.SCISSORS, Choice.ROCK);
		roundPlayer2Win.setResult(RoundResult.WIN_TWO);

		List<Round> gamesUser1 = Arrays.asList(roundPlayer1Win);
		List<Round> gamesUser2 = Arrays.asList(roundPlayer2Win);

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
		doThrow(InvalidUserException.class).when(gamesRepository)
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

}
