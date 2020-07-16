package com.game.rockpaperscissors.repository;

import java.util.List;
import java.util.Map;

import com.game.rockpaperscissors.exception.InvalidUserException;

/**
 * Games Repository Interface.
 *
 * @param <E>
 */
public interface GamesRepository<E> {

	/**
	 * Get all the games played by a user.
	 * 
	 * @param user A user.
	 * @return List with all round played by the user.
	 * @throws InvalidUserException
	 */
	public List<E> findAllGamesByUser(String user) throws InvalidUserException;

	/**
	 * Add one round in the games played by the user.
	 * 
	 * @param user  A user.
	 * @param round A round played by the user.
	 * @throws InvalidUserException
	 */
	public void addGameToUser(String user, E round) throws InvalidUserException;

	/**
	 * Delete all the games played by the user.
	 * 
	 * @param user A user.
	 * @throws InvalidUserException
	 */
	public void deleteGamesUser(String user) throws InvalidUserException;
	
	/**
	 * Get all the games of all the users.
	 * 
	 * @return Map<String, List<E>>
	 */
	public Map<String, List<E>> getAllGames();

}
