package com.indium.spring_boot_assignment;

import com.indium.spring_boot_assignment.repository.DeliveryRepository;
import com.indium.spring_boot_assignment.service.WicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class WicketServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private WicketService wicketService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetWicketsSummaryByBowler_withResults() {
        String bowlerName = "John Doe";
        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{"John Doe", 5, "Player1, Player2"});
        results.add(new Object[]{"John Doe", 3, "Player3, Player4"});
        when(deliveryRepository.findWicketsSummaryByBowlerName(bowlerName)).thenReturn(results);

        String expected = "Wickets summary for bowler John Doe:\n" +
                "Bowler Name: John Doe, Wickets Taken: 5, Players Out: Player1, Player2\n" +
                "Bowler Name: John Doe, Wickets Taken: 3, Players Out: Player3, Player4\n";
        String result = wicketService.getWicketsSummaryByBowler(bowlerName);
        assertEquals(expected, result);
    }

    @Test
    public void testGetWicketsSummaryByBowler_noResults() {
        String bowlerName = "Jane Smith";
        List<Object[]> results = new ArrayList<>();
        when(deliveryRepository.findWicketsSummaryByBowlerName(bowlerName)).thenReturn(results);
        String expected = "No wickets found for bowler: Jane Smith";
        String result = wicketService.getWicketsSummaryByBowler(bowlerName);
        assertEquals(expected, result);
    }
    @Test
    public void testClearCache() {
        wicketService.clearCache();
    }
}

