package com.indium.spring_boot_assignment.service;

import com.indium.spring_boot_assignment.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreService {

    @Autowired
    DeliveryRepository deliveryRepository;

    @Cacheable(value = "cumulativescoreCache", key = "#batterName")
    public String getCumulativeScore(String batterName) {
        List<Object[]> scores = deliveryRepository.getCumulativeScoreByBatterNamePerMatch(batterName);

        if (scores != null && !scores.isEmpty()) {
            StringBuilder response = new StringBuilder("The cumulative scores of " + batterName + " by match are:\n");
            for (Object[] score : scores) {
                response.append("Match ID: ").append(score[0]).append(", Total Runs: ").append(score[1]).append("\n");
            }
            return response.toString();
        } else {
            return "No records found for batter: " + batterName;
        }
    }
    @CacheEvict(value = {"cumulativescoreCache"}, allEntries = true)
    public void clearCache() {

    }
}
