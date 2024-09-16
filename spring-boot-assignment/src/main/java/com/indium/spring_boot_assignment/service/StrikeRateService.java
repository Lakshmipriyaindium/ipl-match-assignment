package com.indium.spring_boot_assignment.service;

import com.indium.spring_boot_assignment.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class StrikeRateService {

    @Autowired
    private MatchRepository matchRepository;

    @Cacheable(value = "skrikeCache", key = "#matchNumber")
    public String getStrikeRate(String batterName, Integer matchNumber) {
        Integer totalRuns = matchRepository.findTotalRunsByBatterAndMatch(batterName, matchNumber);
        Integer totalBallsFaced = matchRepository.findTotalBallsFacedByBatterAndMatch(batterName, matchNumber);

        if (totalRuns == null || totalBallsFaced == null || totalBallsFaced == 0) {
            return "Strike Rate: 0.0";
        }

        double strikeRate = ((double) totalRuns / totalBallsFaced) * 100;
        return String.format("Strike Rate: %.2f", strikeRate);
    }
    @CacheEvict(value = {"skrikeCache"}, allEntries = true)
    public void clearCache() {

    }
}

