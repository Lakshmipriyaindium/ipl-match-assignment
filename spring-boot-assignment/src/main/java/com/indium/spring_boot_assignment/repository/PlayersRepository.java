package com.indium.spring_boot_assignment.repository;

import com.indium.spring_boot_assignment.entity.Players;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayersRepository extends CrudRepository<Players, Integer> {

    @Query("SELECT p.playerName, t.teamName, m.eventMatchNumber " +
            "FROM Players p " +
            "JOIN p.team t " +
            "JOIN p.match m " +
            "WHERE t.teamName = :teamName AND m.eventMatchNumber = :eventMatchNumber")
    List<Object[]> findPlayerInfoByTeamNameAndEventMatchNumber(
            @Param("teamName") String teamName,
            @Param("eventMatchNumber") Integer eventMatchNumber
    );
}
