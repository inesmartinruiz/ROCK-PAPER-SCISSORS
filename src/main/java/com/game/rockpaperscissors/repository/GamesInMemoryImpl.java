package com.game.rockpaperscissors.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.game.rockpaperscissors.exception.InvalidUserException;

/**
 * Games In Memory Repository Implementation.
 * 
 * @author Ines
 *
 * @param <E>
 */
@Component
public class GamesInMemoryImpl<E> implements GamesRepository<E> {

	private Map<String, List<E>> gamesByUser = new HashMap<>();

	@Override
	public List<E> findAllGamesByUser(String user) throws InvalidUserException {
		if (user == null) {
			throw new InvalidUserException("Invalid User");
		}
		if (gamesByUser == null) {
			gamesByUser = new HashMap<>();
		}
		if (gamesByUser.get(user) == null) {
			return new ArrayList<>();
		}
		return gamesByUser.get(user);
	}

	@Override
	public void addGameToUser(String user, E round) 
			throws InvalidUserException {
		if (user == null) {
			throw new InvalidUserException("Invalid User");
		}
		if (gamesByUser == null) {
			gamesByUser = new HashMap<>();
		}
		if (gamesByUser.containsKey(user)) {
			gamesByUser.get(user).add(round);
		} else {
			List<E> rounds = new ArrayList<>();
			rounds.add(round);
			gamesByUser.put(user, rounds);
		}
	}

	@Override
	public void deleteGamesUser(String user) throws InvalidUserException {
		if (user == null) {
			throw new InvalidUserException("Invalid User");
		}
		if ((gamesByUser != null) && (gamesByUser.containsKey(user))) {
			gamesByUser.put(user, new ArrayList<>());
		}
	}
}
