package com.indium.spring_boot_assignment.service;

import com.indium.spring_boot_assignment.entity.Match;
import com.indium.spring_boot_assignment.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    String TOPIC = "match-logs-topic";

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    MatchRepository matchRepository;

    @Cacheable(value = "playernameCache", key = "#playerName")
    public List<Match> findAllMatchesByPlayerName(String playerName) {
        List<Match> matches = matchRepository.findAllMatchesByPlayerName(playerName);
        return matches;
    }
    @CacheEvict(value = {"playernameCache"}, allEntries = true)
    public void clearCache() {

    }
}
