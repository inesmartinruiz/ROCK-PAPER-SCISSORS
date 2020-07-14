package com.game.rockpaperscissors.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.game.rockpaperscissors.service.PlayGameService;

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class PlayGameControllerTest {
	
	protected MockMvc mockMvc;
    
	@Mock
	private PlayGameService playGameService;
	
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
    public void testInitialPlayRoundPage() throws Exception {
        this.mockMvc.perform(get("/rockpaperscissors/play-round"))
                .andExpect(status().isOk())
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
        this.mockMvc.perform(post("/rockpaperscissors/restart-game"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(
                		"redirect:/rockpaperscissors/play-round"));
    }

}
