package com.indium.spring_boot_assignment;

import com.indium.spring_boot_assignment.repository.MatchRepository;
import com.indium.spring_boot_assignment.service.StrikeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class StrikeRateServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private StrikeRateService strikeRateService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStrikeRate_validData() {
        String batterName = "John Doe";
        Integer matchNumber = 1;
        Integer totalRuns = 75;
        Integer totalBallsFaced = 30;
        when(matchRepository.findTotalRunsByBatterAndMatch(batterName, matchNumber)).thenReturn(totalRuns);
        when(matchRepository.findTotalBallsFacedByBatterAndMatch(batterName, matchNumber)).thenReturn(totalBallsFaced);
        String result = strikeRateService.getStrikeRate(batterName, matchNumber);

        // Assert
        String expected = "Strike Rate: 250.00";
        assertEquals(expected, result);
    }

    @Test
    public void testGetStrikeRate_noBallsFaced() {
        String batterName = "Jane Doe";
        Integer matchNumber = 2;
        Integer totalRuns = 50;
        Integer totalBallsFaced = 0;
        when(matchRepository.findTotalRunsByBatterAndMatch(batterName, matchNumber)).thenReturn(totalRuns);
        when(matchRepository.findTotalBallsFacedByBatterAndMatch(batterName, matchNumber)).thenReturn(totalBallsFaced);
        String result = strikeRateService.getStrikeRate(batterName, matchNumber);
        String expected = "Strike Rate: 0.0";
        assertEquals(expected, result);
    }

    @Test
    public void testGetStrikeRate_noData() {
        // Arrange
        String batterName = "John Doe";
        Integer matchNumber = 3;
        when(matchRepository.findTotalRunsByBatterAndMatch(batterName, matchNumber)).thenReturn(null);
        when(matchRepository.findTotalBallsFacedByBatterAndMatch(batterName, matchNumber)).thenReturn(null);
        String result = strikeRateService.getStrikeRate(batterName, matchNumber);
        String expected = "Strike Rate: 0.0";
        assertEquals(expected, result);
    }
    @Test
    public void testClearCache() {
        strikeRateService.clearCache();
    }
}

