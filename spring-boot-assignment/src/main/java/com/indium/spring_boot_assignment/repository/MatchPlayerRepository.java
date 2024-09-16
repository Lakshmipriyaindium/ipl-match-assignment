package com.indium.spring_boot_assignment.repository;

import com.indium.spring_boot_assignment.entity.MatchPlayer;
import org.springframework.data.repository.CrudRepository;

public interface MatchPlayerRepository extends CrudRepository<MatchPlayer,Integer> {
}
