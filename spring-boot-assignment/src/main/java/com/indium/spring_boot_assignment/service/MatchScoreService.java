package com.indium.spring_boot_assignment.service;

import com.indium.spring_boot_assignment.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchScoreService {

    @Autowired
    private MatchRepository matchRepository;

    @Cacheable(value = "matchscoreCache", key = "#matchDate")
    public List<Map<String, Object>> getMatchScoresByDate(LocalDate matchDate) {
        List<Object[]> results = matchRepository.getMatchScoresByDate(matchDate);

        return results.stream().map(row -> Map.of(
                "matchNumber", row[0],
                "matchId", row[1],
                "inningsId", row[2],
                "teamName", row[3],
                "totalRuns", row[4]
        )).collect(Collectors.toList());
    }
    @CacheEvict(value = {"matchscoreCache"}, allEntries = true)
    public void clearCache() {

    }
}

