package com.nhohantu.tcbookbe.cms.service.implement;

import com.nhohantu.tcbookbe.common.model.entity.Warrior;
import com.nhohantu.tcbookbe.common.repository.WarriorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarriorService {
    private final WarriorRepository warriorRepository;

    public List<Warrior> getAllWarriors() {
        return warriorRepository.findAll();
    }

    public List<Warrior> getWarriorsWithFilters(String name, String rank, String status) {
        return warriorRepository.findWithFilters(name, rank, status);
    }

    public Warrior createWarrior(Warrior warrior) {
        return warriorRepository.save(warrior);
    }

    public Warrior getWarrior(String id) {
        return warriorRepository.findById(id).orElseThrow(() -> new RuntimeException("Warrior not found"));
    }

    public Warrior updateWarrior(String id, Warrior warriorDetails) {
        Warrior warrior = getWarrior(id);
        warrior.setName(warriorDetails.getName());
        warrior.setRank(warriorDetails.getRank());
        warrior.setUnit(warriorDetails.getUnit());
        warrior.setStatus(warriorDetails.getStatus());
        return warriorRepository.save(warrior);
    }

    public void deleteWarrior(String id) {
        warriorRepository.deleteById(id);
    }
}
