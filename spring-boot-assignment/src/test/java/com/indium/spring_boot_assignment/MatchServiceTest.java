package com.indium.spring_boot_assignment;

import com.indium.spring_boot_assignment.entity.Match;
import com.indium.spring_boot_assignment.repository.MatchRepository;
import com.indium.spring_boot_assignment.service.MatchService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@SpringJUnitConfig
public class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchService matchService;

    public MatchServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMatchById_Found() {
        // Given
        Integer matchId = 1;
        Match match = new Match();
        match.setMatchId(matchId);
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        // When
        Match result = matchService.getMatchById(matchId);

        // Then
        assertEquals(match, result);
        verify(matchRepository, times(1)).findById(matchId);
    }

    @Test
    public void testGetMatchById_NotFound() {
        // Given
        Integer matchId = 2;
        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        // When
        Match result = matchService.getMatchById(matchId);

        // Then
        assertEquals(null, result);
        verify(matchRepository, times(1)).findById(matchId);
    }

    @Test
    public void testClearCache() {
        matchService.clearCache();
    }
}

