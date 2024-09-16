package com.indium.spring_boot_assignment.repository;

import com.indium.spring_boot_assignment.entity.Match;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MatchRepository extends CrudRepository<Match, Integer> {

    @Query("SELECT p.match FROM Players p WHERE p.playerName = :playerName")
    List<Match> findAllMatchesByPlayerName(@Param("playerName") String playerName);

    @Query("SELECT m.matchId, m.eventName, COUNT(m.matchId) " +
            "FROM Match m " +
            "WHERE m.date = :date " +
            "GROUP BY m.matchId, m.eventName")
    List<Object[]> findMatchesByDate(@Param("date") LocalDate date);

    @Query("SELECT SUM(d.runsBatter) FROM Delivery d JOIN d.match m WHERE d.batterName = :batterName AND m.eventMatchNumber = :matchNumber")
    Integer findTotalRunsByBatterAndMatch(@Param("batterName") String batterName, @Param("matchNumber") Integer matchNumber);

    @Query("SELECT COUNT(d.deliveryId) FROM Delivery d JOIN d.match m WHERE d.batterName = :batterName AND m.eventMatchNumber = :matchNumber")
    Integer findTotalBallsFacedByBatterAndMatch(@Param("batterName") String batterName, @Param("matchNumber") Integer matchNumber);

    @Query(value = "SELECT m.event_match_number AS match_number, m.match_id, " +
            "d.innings_id, t.team_name, SUM(d.total_runs) AS total_runs " +
            "FROM matchs m " +
            "JOIN Deliveries d ON m.match_id = d.match_id " +
            "JOIN Team t ON m.match_id = t.match_id " +
            "AND ( " +
            "    (d.innings_id % 2 = 1 AND t.team_id = ( " +
            "        SELECT MIN(team_id) FROM Team WHERE match_id = m.match_id " +
            "    )) " +
            "    OR  " +
            "    (d.innings_id % 2 = 0 AND t.team_id = ( " +
            "        SELECT MAX(team_id) FROM Team WHERE match_id = m.match_id " +
            "    )) " +
            ") " +
            "WHERE m.date = :matchDate " + // Filter by match date
            "GROUP BY m.event_match_number, m.match_id, d.innings_id, t.team_name " +
            "ORDER BY m.event_match_number, d.innings_id",
            nativeQuery = true)
    List<Object[]> getMatchScoresByDate(@Param("matchDate") LocalDate matchDate);


    Optional<Match> findByMatchId(Integer matchId);
}
