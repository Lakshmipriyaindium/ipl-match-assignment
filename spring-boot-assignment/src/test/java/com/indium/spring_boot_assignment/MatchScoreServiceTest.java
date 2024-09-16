package com.indium.spring_boot_assignment;

import com.indium.spring_boot_assignment.repository.MatchRepository;
import com.indium.spring_boot_assignment.service.MatchScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cache.support.SimpleValueWrapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MatchScoreServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchScoreService matchScoreService;

    @Mock
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMatchScoresByDate() {
        LocalDate matchDate = LocalDate.of(2024, 1, 1);

        // Mock repository response
        Object[] row1 = {1, 101, 201, "Team A", 150};
        Object[] row2 = {2, 102, 202, "Team B", 160};
        List<Object[]> results = List.of(row1, row2);
        when(matchRepository.getMatchScoresByDate(matchDate)).thenReturn(results);

        // Call the service method
        List<Map<String, Object>> scores = matchScoreService.getMatchScoresByDate(matchDate);

        // Verify interactions
        verify(matchRepository).getMatchScoresByDate(matchDate);

        // Verify the results
        assertEquals(2, scores.size());

        Map<String, Object> score1 = scores.get(0);
        assertEquals(1, score1.get("matchNumber"));
        assertEquals(101, score1.get("matchId"));
        assertEquals(201, score1.get("inningsId"));
        assertEquals("Team A", score1.get("teamName"));
        assertEquals(150, score1.get("totalRuns"));

        Map<String, Object> score2 = scores.get(1);
        assertEquals(2, score2.get("matchNumber"));
        assertEquals(102, score2.get("matchId"));
        assertEquals(202, score2.get("inningsId"));
        assertEquals("Team B", score2.get("teamName"));
        assertEquals(160, score2.get("totalRuns"));
    }
    @Test
    public void testClearCache() {
        matchScoreService.clearCache();
    }

}

