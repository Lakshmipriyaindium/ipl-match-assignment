package com.indium.spring_boot_assignment.service;

import com.indium.spring_boot_assignment.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Service
public class TopPlayersService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Cacheable(value = "topplayersCache", key = "#pageable")
    public Page<Map<String, Object>> getTopPlayersByMatch(Pageable pageable) {
        Page<Object[]> results = deliveryRepository.findTopPlayersByMatch(pageable);
        return results.map(row -> Map.of(
                "matchId", row[0],
                "batterName", row[1],
                "totalRuns", row[2]
        ));
    }
    @CacheEvict(value = {"topplayersCache"}, allEntries = true)
    public void clearCache() {

    }
}
