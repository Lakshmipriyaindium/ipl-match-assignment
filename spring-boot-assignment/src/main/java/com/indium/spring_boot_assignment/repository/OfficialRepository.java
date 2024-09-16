package com.indium.spring_boot_assignment.repository;

import com.indium.spring_boot_assignment.entity.Official;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfficialRepository extends CrudRepository<Official,Integer> {
    @Query("SELECT o FROM Official o JOIN o.match m WHERE m.eventMatchNumber = :eventMatchNumber AND o.role = 'match_referees'")
    List<Official> findRefereesByMatchNumber(@Param("eventMatchNumber") Integer eventMatchNumber);
}
