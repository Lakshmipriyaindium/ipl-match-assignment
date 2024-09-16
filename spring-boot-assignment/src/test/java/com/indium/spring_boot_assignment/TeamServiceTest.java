package com.indium.spring_boot_assignment;

import com.indium.spring_boot_assignment.repository.PlayersRepository;
import com.indium.spring_boot_assignment.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TeamServiceTest {

    @Mock
    private PlayersRepository playersRepository;

    @InjectMocks
    private TeamService teamService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPlayersInfo_withResults() {
        // Arrange
        String teamName = "Team A";
        Integer eventMatchNumber = 1;

        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{"John Doe", "Team A", 1});
        results.add(new Object[]{"Jane Smith", "Team A", 1});

        when(playersRepository.findPlayerInfoByTeamNameAndEventMatchNumber(teamName, eventMatchNumber)).thenReturn(results);

        List<List<String>> expected = new ArrayList<>();
        expected.add(List.of("John Doe", "Team A", "1"));
        expected.add(List.of("Jane Smith", "Team A", "1"));

        // Act
        List<List<String>> result = teamService.getPlayersInfo(teamName, eventMatchNumber);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testGetPlayersInfo_noResults() {
        // Arrange
        String teamName = "Team B";
        Integer eventMatchNumber = 2;

        List<Object[]> results = new ArrayList<>();
        when(playersRepository.findPlayerInfoByTeamNameAndEventMatchNumber(teamName, eventMatchNumber)).thenReturn(results);

        List<List<String>> expected = new ArrayList<>();

        // Act
        List<List<String>> result = teamService.getPlayersInfo(teamName, eventMatchNumber);

        // Assert
        assertEquals(expected, result);
    }
    @Test
    public void testClearCache() {
        teamService.clearCache();
    }
}

