package com.nhohantu.tcbookbe.cms.service.implement;

import com.nhohantu.tcbookbe.common.model.entity.Warrior;
import com.nhohantu.tcbookbe.common.repository.WarriorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WarriorService {

    private final WarriorRepository warriorRepository;

    public Page<Warrior> getAllWarriors(
            Pageable pageable) {
        return warriorRepository.findAll(pageable);
    }

    public Page<Warrior> searchWarriors(
            String name, Pageable pageable) {
        return warriorRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Warrior> getWarriorsWithFilters(
            String name, String rank, String status, Pageable pageable) {
        return warriorRepository.findWithFilters(name, rank, status, pageable);
    }

    public Warrior createWarrior(
            Warrior warrior) {
        return warriorRepository.save(warrior);
    }

    public Warrior getWarrior(String id) {
        return warriorRepository.findById(id).orElseThrow(() -> new RuntimeException("Warrior not found"));
    }

    public Warrior updateWarrior(String id,
            Warrior warriorDetails) {
        Warrior warrior = getWarrior(id);
        warrior.setName(warriorDetails.getName());
        warrior.setRank(warriorDetails.getRank());
        warrior.setUnit(warriorDetails.getUnit());
        warrior.setStatus(warriorDetails.getStatus());
        warrior.setBirthDate(warriorDetails.getBirthDate());
        warrior.setGender(warriorDetails.getGender());
        warrior.setAddress(warriorDetails.getAddress());
        return warriorRepository.save(warrior);
    }

    public void deleteWarrior(String id) {
        warriorRepository.deleteById(id);
    }
}
