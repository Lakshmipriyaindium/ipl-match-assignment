package com.indium.spring_boot_assignment.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.indium.spring_boot_assignment.entity.Match;
import com.indium.spring_boot_assignment.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MatchService {

    @Autowired
    MatchRepository matchRepository;

    @Cacheable(value = "matchCache", key = "#matchId")
    public Match getMatchById(Integer matchId) {
        System.out.println("Fetching from repository for ID: " + matchId);
        return matchRepository.findById(matchId).orElse(null);
    }

    @CacheEvict(value = {"matchCache"}, allEntries = true)
    public void clearCache() {

    }
}
