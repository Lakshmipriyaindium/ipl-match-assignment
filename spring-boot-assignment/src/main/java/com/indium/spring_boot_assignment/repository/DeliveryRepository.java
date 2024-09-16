package com.indium.spring_boot_assignment.repository;

import com.indium.spring_boot_assignment.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DeliveryRepository extends CrudRepository<Delivery,Integer> {
    @Query("SELECT d.match.id, SUM(d.runsBatter) FROM Delivery d WHERE d.batterName = :batterName GROUP BY d.match.id")
    List<Object[]> getCumulativeScoreByBatterNamePerMatch(@Param("batterName") String batterName);

    @Query("SELECT d.bowlerName, COUNT(d.wicketPlayerOutName), GROUP_CONCAT(d.wicketPlayerOutName) " +
            "FROM Delivery d WHERE d.bowlerName = :bowlerName " +
            "GROUP BY d.bowlerName")
    List<Object[]> findWicketsSummaryByBowlerName(@Param("bowlerName") String bowlerName);

    @Query("SELECT d.match.matchId AS matchId, d.batterName AS batterName, SUM(d.runsBatter) AS totalRuns " +
            "FROM Delivery d " +
            "GROUP BY d.match.matchId, d.batterName " +
            "ORDER BY d.match.matchId ASC, totalRuns ASC")
    Page<Object[]> findTopPlayersByMatch(Pageable pageable);

    @Query("SELECT d.bowlerName AS bowlerName, COUNT(d.wicketKind) AS totalWickets " +
            "FROM Delivery d " +
            "WHERE d.wicketKind IS NOT NULL " +
            "GROUP BY d.bowlerName " +
            "ORDER BY totalWickets ASC")
    Page<Object[]> findTopWicketTakers(Pageable pageable);



}
