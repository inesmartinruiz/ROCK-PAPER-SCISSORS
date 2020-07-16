package com.game.rockpaperscissors.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.game.rockpaperscissors.service.PlayGameService;
import com.game.rockpaperscissors.service.SessionService;

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class PlayGameControllerTest {
	
	protected MockMvc mockMvc;
    
	@Mock
	private PlayGameService playGameService;
	
	@Mock
	private SessionService sessionService;
	
	@InjectMocks
	private PlayGameController playGameController;
	
	@Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InternalResourceViewResolver resolver = 
        		new InternalResourceViewResolver(); 
        resolver.setSuffix(".html");
        mockMvc = MockMvcBuilders.standaloneSetup(playGameController)
        			.setViewResolvers(resolver).build();
    }

    @Test
    public void testIndexPage() throws Exception {
        this.mockMvc.perform(get("/rockpaperscissors/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(
                		"redirect:/rockpaperscissors/play-round"));
    }
    
    @Test
    public void testPlayRoundPage() throws Exception {
        this.mockMvc.perform(get("/rockpaperscissors/play-round"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("rounds"))
                .andExpect(model().attributeExists("userId"))
                .andExpect(view().name("play-round"));
    }
    
    @Test
    public void testPlayOneRoundPage() throws Exception {    	
        this.mockMvc.perform(post("/rockpaperscissors/play-one-round"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(
                		"redirect:/rockpaperscissors/play-round"));
    }
    
    @Test
    public void testRestartGamePage() throws Exception {   	
    	BDDMockito.given(sessionService.getUserId()).willReturn("user1");
        this.mockMvc.perform(post("/rockpaperscissors/restart-game"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(
                		"redirect:/rockpaperscissors/play-round"));
        BDDMockito.verify(playGameService, BDDMockito.times(1))
        	.restartGame("user1");
    }
    
    @Test
    public void testTotalPage() throws Exception {   	
        this.mockMvc.perform(get("/rockpaperscissors/totals"))
                .andExpect(status().isOk())
                .andExpect(view().name("total"));
    }

}
