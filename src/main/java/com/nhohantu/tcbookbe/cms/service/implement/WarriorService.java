package com.nhohantu.tcbookbe.cms.service.implement;

import com.nhohantu.tcbookbe.common.model.entity.FamilyMember;
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
    private final com.nhohantu.tcbookbe.common.repository.ProvinceRepository provinceRepository;
    private final com.nhohantu.tcbookbe.common.repository.CommuneRepository communeRepository;

    public Page<Warrior> getAllWarriors(
            Pageable pageable) {
        Page<Warrior> warriors = warriorRepository.findAll(pageable);
        warriors.forEach(this::populateWarriorNames);
        return warriors;
    }

    public Page<Warrior> searchWarriors(
            String name, Pageable pageable) {
        Page<Warrior> warriors = warriorRepository.findByNameContainingIgnoreCase(name, pageable);
        warriors.forEach(this::populateWarriorNames);
        return warriors;
    }

    public Page<Warrior> getWarriorsWithFilters(
            String name, String rank, String status, Pageable pageable) {
        Page<Warrior> warriors = warriorRepository.findWithFilters(name, rank, status, pageable);
        warriors.forEach(this::populateWarriorNames);
        return warriors;
    }

    public Warrior createWarrior(
            Warrior warrior) {
        if (warrior.getFamilyMembers() != null) {
            warrior.getFamilyMembers().forEach(member -> member.setWarrior(warrior));
        }
        return warriorRepository.save(warrior);
    }

    public Warrior getWarrior(String id) {
        Warrior warrior = warriorRepository.findById(id).orElseThrow(() -> new RuntimeException("Warrior not found"));
        return populateWarriorNames(warrior);
    }

    private Warrior populateWarriorNames(Warrior warrior) {
        if (warrior.getHometownProvinceCode() != null) {
            provinceRepository.findByCode(warrior.getHometownProvinceCode())
                    .ifPresent(p -> warrior.setHometownProvinceName(p.getName()));
        }
        if (warrior.getHometownCommuneCode() != null) {
            communeRepository.findById(warrior.getHometownCommuneCode())
                    .ifPresent(c -> warrior.setHometownCommuneName(c.getName()));
        }
        if (warrior.getCurrentProvinceCode() != null) {
            provinceRepository.findByCode(warrior.getCurrentProvinceCode())
                    .ifPresent(p -> warrior.setCurrentProvinceName(p.getName()));
        }
        if (warrior.getCurrentCommuneCode() != null) {
            communeRepository.findById(warrior.getCurrentCommuneCode())
                    .ifPresent(c -> warrior.setCurrentCommuneName(c.getName()));
        }
        return warrior;
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
        warrior.setPhoneNumber(warriorDetails.getPhoneNumber());
        warrior.setNotes(warriorDetails.getNotes());
        warrior.setStrengths(warriorDetails.getStrengths());
        warrior.setAspirations(warriorDetails.getAspirations());
        warrior.setHometownProvinceCode(warriorDetails.getHometownProvinceCode());
        warrior.setHometownCommuneCode(warriorDetails.getHometownCommuneCode());
        warrior.setHometownAddress(warriorDetails.getHometownAddress());
        warrior.setCurrentProvinceCode(warriorDetails.getCurrentProvinceCode());
        warrior.setCurrentCommuneCode(warriorDetails.getCurrentCommuneCode());
        warrior.setCurrentAddress(warriorDetails.getCurrentAddress());
        warrior.setAvatar(warriorDetails.getAvatar());

        // Handle family members update
        if (warrior.getFamilyMembers() != null) {
            warrior.getFamilyMembers().clear();
        } else {
            warrior.setFamilyMembers(new java.util.ArrayList<>());
        }
        if (warriorDetails.getFamilyMembers() != null) {
            for (FamilyMember member : warriorDetails.getFamilyMembers()) {
                member.setWarrior(warrior);
                warrior.getFamilyMembers().add(member);
            }
        }

        return warriorRepository.save(warrior);
    }

    public void deleteWarrior(String id) {
        warriorRepository.deleteById(id);
    }
}
