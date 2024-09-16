package com.indium.spring_boot_assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indium.spring_boot_assignment.controller.MatchController;
import com.indium.spring_boot_assignment.entity.Match;
import com.indium.spring_boot_assignment.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MatchController.class)
public class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchService matchService;

    @MockBean
    private CricketMatchService cricketMatchService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private ScoreService scoreService;

    @MockBean
    private WicketService wicketService;

    @MockBean
    private TeamService teamService;

    @MockBean
    private OfficialService officialService;

    @MockBean
    private StrikeRateService strikeRateService;

    @MockBean
    private MatchScoreService matchScoreService;

    @MockBean
    private TopPlayersService topPlayersService;

    @MockBean
    TopWicketTakerService topWicketTakerService;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetMatchById() throws Exception {
        // Arrange: Mock data setup
        Match match = new Match();
        match.setMatchId(1); // Set the match ID

        // Mock the service response
        when(matchService.getMatchById(1)).thenReturn(match);

        // Act & Assert: Perform the GET request and verify the response
        mockMvc.perform(get("/api/matches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchId").value(1));  // Use 'matchId' instead of 'id'

        // Verify the service method call
        verify(matchService, times(1)).getMatchById(1);
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    @Test
    public void testGetMatchesByPlayerName() throws Exception {
        // Mock data setup
        String playerName = "V Kohli";
        List<Match> matches = new ArrayList<>();
        matches.add(new Match());  // assuming a no-arg constructor in Match

        // Mocking the service
        when(playerService.findAllMatchesByPlayerName(playerName)).thenReturn(matches);

        // Perform the GET request and verify response
        mockMvc.perform(get("/api/matches/player/{playerName}", playerName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());  // assuming match data exists

        // Verify that the service method was called
        verify(playerService, times(1)).findAllMatchesByPlayerName(playerName);
    }

    @Test
    public void testCreateMatch() throws Exception {
        // Create a Match object with the expected matchId
        Match match = new Match();
        match.setMatchId(1);

        // Mock the service to return the created Match object
        when(cricketMatchService.insertMatchData(anyString())).thenReturn(match);

        // Perform the POST request with the correct JSON payload
        mockMvc.perform(post("/api/matches/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}")) // Adjust payload if necessary
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchId").value(1)); // Update to expect matchId

        // Verify interactions with mocks
        verify(cricketMatchService, times(1)).insertMatchData(anyString());
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }


    @Test
    public void testGetCumulativeScore() throws Exception {
        when(scoreService.getCumulativeScore("Sachin Tendulkar")).thenReturn("100");

        mockMvc.perform(get("/api/matches/cumulative-score")
                        .param("batterName", "Sachin Tendulkar"))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));

        verify(scoreService, times(1)).getCumulativeScore("Sachin Tendulkar");
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    @Test
    public void testGetWicketsSummaryByBowler() throws Exception {
        when(wicketService.getWicketsSummaryByBowler("Anil Kumble")).thenReturn("5 wickets");

        mockMvc.perform(get("/api/matches/wickets")
                        .param("bowlerName", "Anil Kumble"))
                .andExpect(status().isOk())
                .andExpect(content().string("5 wickets"));

        verify(wicketService, times(1)).getWicketsSummaryByBowler("Anil Kumble");
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    @Test
    public void testGetPlayersInfo() throws Exception {
        when(teamService.getPlayersInfo("India", 2)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/matches/players")
                        .param("teamName", "India")
                        .param("eventMatchNumber", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(teamService, times(1)).getPlayersInfo("India", 2);
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    @Test
    public void testGetRefereesByMatchNumber() throws Exception {
        when(officialService.findRefereesByMatchNumber(3)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/matches/referees")
                        .param("matchNumber", "3"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(officialService, times(1)).findRefereesByMatchNumber(3);
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    @Test
    public void testGetStrikeRate() throws Exception {
        when(strikeRateService.getStrikeRate("Rohit Sharma", 5)).thenReturn("75.00");

        mockMvc.perform(get("/api/matches/strike-rate")
                        .param("batterName", "Rohit Sharma")
                        .param("matchNumber", "5"))
                .andExpect(status().isOk())
                .andExpect(content().string("75.00"));

        verify(strikeRateService, times(1)).getStrikeRate("Rohit Sharma", 5);
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    @Test
    public void testGetMatchScores() throws Exception {
        when(matchScoreService.getMatchScoresByDate(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/matches/scores")
                        .param("date", "2024-09-13"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(matchScoreService, times(1)).getMatchScoresByDate(any());
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    @Test
    public void testGetTopPlayers() throws Exception {
        // Mock the Pageable
        Pageable pageable = PageRequest.of(0, 10); // Or use any other appropriate Pageable implementation

        // Mock service to return an empty Page
        when(topPlayersService.getTopPlayersByMatch(pageable)).thenReturn(Page.empty());

        // Perform the GET request and assert the response
        mockMvc.perform(get("/api/matches/top-players")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number\":0,\"sort\":{\"empty\":true,\"unsorted\":true,\"sorted\":false},\"numberOfElements\":0,\"first\":true,\"empty\":true}"));

        // Verify interactions with mocks
        verify(topPlayersService, times(1)).getTopPlayersByMatch(any(Pageable.class));
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    @Test
    public void testGetTopWicketTakers() throws Exception {
        // Mock Pageable
        Pageable pageable = PageRequest.of(0, 10); // Adjust as necessary

        // Mock the service to return an empty Page
        when(topWicketTakerService.getTopWicketTakers(pageable)).thenReturn(Page.empty());

        // Perform the GET request and assert the response
        mockMvc.perform(get("/api/matches/top-wicket-takers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number\":0,\"sort\":{\"empty\":true,\"unsorted\":true,\"sorted\":false},\"numberOfElements\":0,\"first\":true,\"empty\":true}"));

        // Verify interactions with mocks
        verify(topWicketTakerService, times(1)).getTopWicketTakers(any(Pageable.class));
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

}