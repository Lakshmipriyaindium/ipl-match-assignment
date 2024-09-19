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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("qa")
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
    @WithMockUser
    public void testGetMatchById() throws Exception {
        Match match = new Match();
        match.setMatchId(1);


        when(matchService.getMatchById(1)).thenReturn(match);


        mockMvc.perform(get("/api/matches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchId").value(1));  // Use 'matchId' instead of 'id'

        verify(matchService, times(1)).getMatchById(1);
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    @Test
    @WithMockUser
    public void testGetMatchesByPlayerName() throws Exception {

        String playerName = "V Kohli";
        List<Match> matches = new ArrayList<>();
        matches.add(new Match());


        when(playerService.findAllMatchesByPlayerName(playerName)).thenReturn(matches);


        mockMvc.perform(get("/api/matches/player/{playerName}", playerName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());


        verify(playerService, times(1)).findAllMatchesByPlayerName(playerName);
    }

    @Test
    @WithMockUser
    public void testCreateMatch() throws Exception {

        Match match = new Match();
        match.setMatchId(1);


        when(cricketMatchService.insertMatchData(anyString())).thenReturn(match);


        mockMvc.perform(post("/api/matches/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}")) // Adjust payload if necessary
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchId").value(1)); // Update to expect matchId


        verify(cricketMatchService, times(1)).insertMatchData(anyString());
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }


    @Test
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
    public void testGetTopPlayers() throws Exception {

        Pageable pageable = PageRequest.of(0, 10); // Or use any other appropriate Pageable implementation


        when(topPlayersService.getTopPlayersByMatch(pageable)).thenReturn(Page.empty());


        mockMvc.perform(get("/api/matches/top-players")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number\":0,\"sort\":{\"empty\":true,\"unsorted\":true,\"sorted\":false},\"numberOfElements\":0,\"first\":true,\"empty\":true}"));


        verify(topPlayersService, times(1)).getTopPlayersByMatch(any(Pageable.class));
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    @Test
    @WithMockUser
    public void testGetTopWicketTakers() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);


        when(topWicketTakerService.getTopWicketTakers(pageable)).thenReturn(Page.empty());


        mockMvc.perform(get("/api/matches/top-wicket-takers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number\":0,\"sort\":{\"empty\":true,\"unsorted\":true,\"sorted\":false},\"numberOfElements\":0,\"first\":true,\"empty\":true}"));


        verify(topWicketTakerService, times(1)).getTopWicketTakers(any(Pageable.class));
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

}