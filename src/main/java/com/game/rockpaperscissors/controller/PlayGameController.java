package com.game.rockpaperscissors.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.game.rockpaperscissors.exception.InvalidUserException;
import com.game.rockpaperscissors.exception.PlayRoundException;
import com.game.rockpaperscissors.model.Round;
import com.game.rockpaperscissors.service.PlayGameService;
import com.game.rockpaperscissors.service.SessionService;

@Controller
@RequestMapping("rockpaperscissors")
// It keeps userId between different tabs and windows but different browsers.
@SessionAttributes("userId") 
public class PlayGameController {

	private static final String USER_ID = "userId";
	private static final String ROUNDS_ATTRIBUTE = "rounds";

	private PlayGameService playGameService;
	private SessionService sessionService;
	
	public PlayGameController(PlayGameService playGameService, 
			SessionService sessionService) {
		this.playGameService = playGameService;
		this.sessionService = sessionService;
	}
	
	@ModelAttribute(USER_ID)
	public String userId() {
	    return "";
	}
	
    @GetMapping("/")
    public String index(Model model, RedirectAttributes redirectAttrs)  {
    	String userId = Objects.toString(model.getAttribute(USER_ID));
    	if (userId != null && !userId.isEmpty()) {
    		model.addAttribute(USER_ID, userId);
        } else {
        	model.addAttribute(USER_ID, sessionService.getUserId());
        }
    	model.addAttribute(ROUNDS_ATTRIBUTE, new ArrayList<>());
    	redirectAttrs.addFlashAttribute(USER_ID, 
    			Objects.toString(model.getAttribute(USER_ID)));  	
        return "redirect:/rockpaperscissors/play-round";
    }

	@GetMapping("/play-round")
	public String playRound(@ModelAttribute(USER_ID) String userId, 
			Model model) throws InvalidUserException {
		List<Round> games = playGameService.getAll(userId);
		model.addAttribute(ROUNDS_ATTRIBUTE, games);
		return "play-round";
	}

	@PostMapping("/play-one-round")
	public String playOneRound(@ModelAttribute(USER_ID) final String userId, 
			Model model, RedirectAttributes redirectAttrs) 
			throws PlayRoundException, InvalidUserException {
		playGameService.playNewGame(userId);
		redirectAttrs.addFlashAttribute(USER_ID, userId);
		return "redirect:/rockpaperscissors/play-round";
	}

	@PostMapping("/restart-game")
	public String restartGame(Model model, RedirectAttributes redirectAttrs) 
			throws InvalidUserException {
		String userId = sessionService.getUserId();
		redirectAttrs.addFlashAttribute(USER_ID, userId);
		playGameService.restartGame(userId);
		return "redirect:/rockpaperscissors/play-round";
	}
	
	@GetMapping("/totals")
	public String totals(Model model) {
		playGameService.getTotals().entrySet().stream()
			.forEach(e -> model.addAttribute(e.getKey(), e.getValue()));
		return "total";
	}
}
