package com.game.rockpaperscissors.repository;

import java.util.List;

import com.game.rockpaperscissors.exception.InvalidUserException;

/**
 * Games Repository Interface.
 * 
 * @author Ines
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

}
