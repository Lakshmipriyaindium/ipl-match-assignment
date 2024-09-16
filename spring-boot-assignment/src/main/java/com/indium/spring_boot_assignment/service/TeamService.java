package com.indium.spring_boot_assignment.service;

import com.indium.spring_boot_assignment.repository.PlayersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    @Autowired
    private PlayersRepository playersRepository;

    @Cacheable(value = "playerteamCache", key = "{#teamName, #eventMatchNumber}")
    public List<List<String>> getPlayersInfo(String teamName, Integer eventMatchNumber) {
        List<Object[]> results = playersRepository.findPlayerInfoByTeamNameAndEventMatchNumber(teamName, eventMatchNumber);
        List<List<String>> playerInfoList = new ArrayList<>();

        for (Object[] result : results) {
            List<String> info = new ArrayList<>();
            info.add((String) result[0]); // playerName
            info.add((String) result[1]); // teamName
            info.add(result[2].toString()); // eventMatchNumber
            playerInfoList.add(info);
        }

        return playerInfoList;
    }
    @CacheEvict(value = {"playerteamCache"}, allEntries = true)
    public void clearCache() {

    }
}

