package com.indium.spring_boot_assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.indium.spring_boot_assignment.entity.*;
import com.indium.spring_boot_assignment.repository.*;
import com.indium.spring_boot_assignment.service.CricketMatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CircketMatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PlayersRepository playersRepository;

    @Mock
    private OfficialRepository officialRepository;

    @Mock
    private InningsRepository inningsRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private ExtraRepository extraRepository;

    @Mock
    private PowerPlayRepository powerPlayRepository;

    @InjectMocks
    private CricketMatchService cricketMatchService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testInsertMatchData() throws IOException {
        // Read JSON data from file
        String jsonData = new String(Files.readAllBytes(Paths.get("src/test/resources/SampleDataMatch.json")));

        // Create Match object to be saved
        Match match = new Match();
        match.setDataVersion("1");
        match.setCreated(LocalDate.parse("2024-01-01"));
        match.setRevision(1);
        match.setCity("Mumbai");
        match.setDate(LocalDate.parse("2024-01-01"));
        match.setEventName("T20 World Cup");
        match.setEventMatchNumber(1);
        match.setGender("male");
        match.setMatchType("T20");
        match.setOvers(20);
        match.setPlayerOfMatch("Player A");
        match.setWinner("Team A");
        match.setWinByRuns(10);
        match.setVenue("Wankhede Stadium");
        match.setTossWinner("Team B");
        match.setTossDecision("bat");

        // Mock repository behaviors
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(playersRepository.save(any(Players.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(officialRepository.save(any(Official.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(inningsRepository.save(any(Innings.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(extraRepository.save(any(Extra.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(powerPlayRepository.save(any(PowerPlay.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the method to test
        Match result = cricketMatchService.insertMatchData(jsonData);

        // Verify interactions
        verify(matchRepository).save(any(Match.class));
        verify(teamRepository, atLeast(2)).save(any(Team.class));
        verify(playersRepository, atLeast(4)).save(any(Players.class));
        verify(officialRepository, atLeast(2)).save(any(Official.class));
        verify(inningsRepository, atLeast(1)).save(any(Innings.class));
        verify(deliveryRepository, atLeast(1)).save(any(Delivery.class));
        verify(extraRepository, atLeast(1)).save(any(Extra.class));
        verify(powerPlayRepository, atLeast(1)).save(any(PowerPlay.class));

        // Assert that the result matches the expected values
        assertEquals("Bangalore", result.getCity());
        assertEquals("Kolkata Knight Riders", result.getWinner());
    }
    @Test
    public void testClearCache() {
        cricketMatchService.clearCache();
    }
}
