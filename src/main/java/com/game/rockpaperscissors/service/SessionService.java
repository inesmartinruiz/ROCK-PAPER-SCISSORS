package com.game.rockpaperscissors.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

/**
 * Session Service.
 *
 */
@Service
public class SessionService {

	/**
	 * Get a random user id.
	 * 
	 * @return User id.
	 */
	public String getUserId() {
		return UUID.randomUUID().toString();
	}

}
