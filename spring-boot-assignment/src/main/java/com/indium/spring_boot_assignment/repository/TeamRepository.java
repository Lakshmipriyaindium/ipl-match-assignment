package com.indium.spring_boot_assignment.repository;

import com.indium.spring_boot_assignment.entity.Match;
import com.indium.spring_boot_assignment.entity.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team,Integer> {
    Team findByTeamNameAndMatch(String teamName, Match match);
}
