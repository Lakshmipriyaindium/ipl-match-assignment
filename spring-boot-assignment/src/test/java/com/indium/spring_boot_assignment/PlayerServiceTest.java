package com.indium.spring_boot_assignment;

import com.indium.spring_boot_assignment.entity.Match;
import com.indium.spring_boot_assignment.repository.MatchRepository;
import com.indium.spring_boot_assignment.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@SpringJUnitConfig
public class PlayerServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllMatchesByPlayerName() {
        // Given
        String playerName = "John Doe";
        Match match1 = new Match();
        match1.setMatchId(1);
        Match match2 = new Match();
        match2.setMatchId(2);
        List<Match> matches = Arrays.asList(match1, match2);

        when(matchRepository.findAllMatchesByPlayerName(playerName)).thenReturn(matches);

        // When
        List<Match> result = playerService.findAllMatchesByPlayerName(playerName);

        // Then
        assertEquals(matches, result);
        verify(matchRepository, times(1)).findAllMatchesByPlayerName(playerName);
    }

    @Test
    public void testClearCache() {
        playerService.clearCache();
    }
}

