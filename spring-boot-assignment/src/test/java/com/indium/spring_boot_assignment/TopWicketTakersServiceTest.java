package com.indium.spring_boot_assignment;

import com.indium.spring_boot_assignment.repository.DeliveryRepository;
import com.indium.spring_boot_assignment.service.TopWicketTakerService;
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

public class TopWicketTakersServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private TopWicketTakerService topWicketTakerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTopWicketTakers_withResults() {
        // Arrange
        Pageable pageable = Pageable.ofSize(10);
        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{"John Doe", 10});
        results.add(new Object[]{"Jane Smith", 8});
        Page<Object[]> page = new PageImpl<>(results, pageable, results.size());

        when(deliveryRepository.findTopWicketTakers(pageable)).thenReturn(page);
        List<Map<String, Object>> expected = List.of(
                Map.of("bowlerName", "John Doe", "totalWickets", 10),
                Map.of("bowlerName", "Jane Smith", "totalWickets", 8)
        );
        Page<Map<String, Object>> result = topWicketTakerService.getTopWicketTakers(pageable);
        assertEquals(expected, result.getContent());
    }

    @Test
    public void testGetTopWicketTakers_noResults() {
        // Arrange
        Pageable pageable = Pageable.ofSize(10);
        Page<Object[]> page = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(deliveryRepository.findTopWicketTakers(pageable)).thenReturn(page);
        List<Map<String, Object>> expected = new ArrayList<>();
        Page<Map<String, Object>> result = topWicketTakerService.getTopWicketTakers(pageable);
        assertEquals(expected, result.getContent());
    }
    @Test
    public void testClearCache() {
        topWicketTakerService.clearCache();
    }
}
