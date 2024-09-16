package com.indium.spring_boot_assignment;

import com.indium.spring_boot_assignment.repository.DeliveryRepository;
import com.indium.spring_boot_assignment.service.ScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ScoreServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private ScoreService scoreService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCumulativeScore_withRecords() {
        // Arrange
        String batterName = "John Doe";
        List<Object[]> scores = new ArrayList<>();
        scores.add(new Object[]{1, 50});
        scores.add(new Object[]{2, 75});
        when(deliveryRepository.getCumulativeScoreByBatterNamePerMatch(batterName)).thenReturn(scores);

        // Act
        String result = scoreService.getCumulativeScore(batterName);

        // Assert
        String expected = "The cumulative scores of John Doe by match are:\n" +
                "Match ID: 1, Total Runs: 50\n" +
                "Match ID: 2, Total Runs: 75\n";
        assertEquals(expected, result);
    }

    @Test
    public void testGetCumulativeScore_noRecords() {
        String batterName = "Jane Doe";
        when(deliveryRepository.getCumulativeScoreByBatterNamePerMatch(batterName)).thenReturn(new ArrayList<>());
        String result = scoreService.getCumulativeScore(batterName);
        String expected = "No records found for batter: Jane Doe";
        assertEquals(expected, result);
    }
    @Test
    public void testClearCache() {
        scoreService.clearCache();
    }
}

