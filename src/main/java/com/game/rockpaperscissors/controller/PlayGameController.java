package com.game.rockpaperscissors.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.game.rockpaperscissors.exception.InvalidUserException;
import com.game.rockpaperscissors.exception.PlayRoundException;
import com.game.rockpaperscissors.model.Round;
import com.game.rockpaperscissors.service.PlayGameService;

@Controller
@RequestMapping("rockpaperscissors")
public class PlayGameController {

	private PlayGameService playGameService;

	public PlayGameController(PlayGameService playGameService) {
		this.playGameService = playGameService;
	}

	@GetMapping("/play-round")
	public String playRound(Model model) throws InvalidUserException {
		List<Round> games = playGameService.getAll("user1");
		if (games == null) {
			games = new ArrayList<>();
		}
		model.addAttribute("rounds", games);
		return "play-round";
	}

	@PostMapping("/play-one-round")
	public String playOneRound(Model model) 
			throws PlayRoundException, InvalidUserException {
		playGameService.playNewGame("user1");
		return "redirect:/rockpaperscissors/play-round";
	}

	@PostMapping("/restart-game")
	public String restartGame(Model model) throws InvalidUserException {
		playGameService.restartGame("user1");
		return "redirect:/rockpaperscissors/play-round";
	}
}
