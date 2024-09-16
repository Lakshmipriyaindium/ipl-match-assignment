package com.indium.spring_boot_assignment.service;

import com.indium.spring_boot_assignment.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class TopWicketTakerService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Cacheable(value = "topwicketsCache", key = "#pageable")
    public Page<Map<String, Object>> getTopWicketTakers(Pageable pageable) {
        Page<Object[]> results = deliveryRepository.findTopWicketTakers(pageable);
        return results.map(row -> Map.of(
                "bowlerName", row[0],
                "totalWickets", row[1]
        ));
    }
    @CacheEvict(value = {"topwicketsCache"}, allEntries = true)
    public void clearCache() {

    }
}

