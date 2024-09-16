package com.indium.spring_boot_assignment;

import com.indium.spring_boot_assignment.entity.Official;
import com.indium.spring_boot_assignment.repository.OfficialRepository;
import com.indium.spring_boot_assignment.service.OfficialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@SpringJUnitConfig
public class OfficialServiceTest {

    @Mock
    private OfficialRepository officialRepository;

    @InjectMocks
    private OfficialService officialService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindRefereesByMatchNumber() {
        // Given
        Integer matchNumber = 123;
        Official official1 = new Official();
        official1.setName("John Doe");
        Official official2 = new Official();
        official2.setName("Jane Smith");
        List<Official> officials = Arrays.asList(official1, official2);

        when(officialRepository.findRefereesByMatchNumber(matchNumber)).thenReturn(officials);

        // When
        List<Official> result = officialService.findRefereesByMatchNumber(matchNumber);

        // Then
        assertEquals(officials, result);
        verify(officialRepository, times(1)).findRefereesByMatchNumber(matchNumber);
    }

    @Test
    public void testClearCache() {
        officialService.clearCache();
    }
}

