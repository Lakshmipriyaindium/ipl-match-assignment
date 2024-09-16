package com.indium.spring_boot_assignment;

import com.indium.spring_boot_assignment.repository.DeliveryRepository;
import com.indium.spring_boot_assignment.service.TopPlayersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TopPlayerServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private TopPlayersService topPlayersService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTopPlayersByMatch_withResults() {
        // Arrange
        Pageable pageable = Pageable.ofSize(10);
        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{1, "John Doe", 75});
        results.add(new Object[]{2, "Jane Smith", 50});
        Page<Object[]> page = new PageImpl<>(results, pageable, results.size());

        when(deliveryRepository.findTopPlayersByMatch(pageable)).thenReturn(page);

        // Expected output
        List<Map<String, Object>> expected = List.of(
                Map.of("matchId", 1, "batterName", "John Doe", "totalRuns", 75),
                Map.of("matchId", 2, "batterName", "Jane Smith", "totalRuns", 50)
        );

        // Act
        Page<Map<String, Object>> result = topPlayersService.getTopPlayersByMatch(pageable);

        // Assert
        assertEquals(expected, result.getContent());
    }

    @Test
    public void testGetTopPlayersByMatch_noResults() {
        // Arrange
        Pageable pageable = Pageable.ofSize(10);
        Page<Object[]> page = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(deliveryRepository.findTopPlayersByMatch(pageable)).thenReturn(page);

        // Expected output
        List<Map<String, Object>> expected = new ArrayList<>();

        // Act
        Page<Map<String, Object>> result = topPlayersService.getTopPlayersByMatch(pageable);

        // Assert
        assertEquals(expected, result.getContent());
    }
    @Test
    public void testClearCache() {
        topPlayersService.clearCache();
    }
}

