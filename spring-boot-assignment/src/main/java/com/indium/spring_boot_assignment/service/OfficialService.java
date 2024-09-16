package com.indium.spring_boot_assignment.service;

import com.indium.spring_boot_assignment.entity.Official;
import com.indium.spring_boot_assignment.repository.OfficialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfficialService {

    @Autowired
    OfficialRepository officialRepository;

    @Cacheable(value = "refereesCache", key = "#matchNumber")
    public List<Official> findRefereesByMatchNumber(Integer matchNumber) {
        return officialRepository.findRefereesByMatchNumber(matchNumber);
    }
    @CacheEvict(value = {"refereesCache"}, allEntries = true)
    public void clearCache() {

    }
}
