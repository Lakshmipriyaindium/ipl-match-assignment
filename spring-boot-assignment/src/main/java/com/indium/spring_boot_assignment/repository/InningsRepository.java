package com.indium.spring_boot_assignment.repository;

import com.indium.spring_boot_assignment.entity.Innings;
import org.springframework.data.repository.CrudRepository;

public interface InningsRepository extends CrudRepository<Innings,Integer> {
}
