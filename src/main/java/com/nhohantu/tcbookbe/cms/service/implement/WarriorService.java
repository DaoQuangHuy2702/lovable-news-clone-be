package com.nhohantu.tcbookbe.cms.service.implement;

import com.nhohantu.tcbookbe.common.model.entity.FamilyMember;
import com.nhohantu.tcbookbe.common.model.entity.Warrior;
import com.nhohantu.tcbookbe.common.repository.WarriorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WarriorService {

    private final WarriorRepository warriorRepository;
    private final com.nhohantu.tcbookbe.common.repository.ProvinceRepository provinceRepository;
    private final com.nhohantu.tcbookbe.common.repository.CommuneRepository communeRepository;
    private final com.nhohantu.tcbookbe.common.repository.LeaveBalanceRepository leaveBalanceRepository;
    private final com.nhohantu.tcbookbe.common.service.UploadService uploadService;

    public Page<Warrior> getAllWarriors(
            Pageable pageable) {
        Page<Warrior> warriors = warriorRepository.findAll(pageable);
        warriors.forEach(w -> {
            populateWarriorNames(w);
            populateLeaveBalance(w, java.time.Year.now().getValue());
        });
        return warriors;
    }

    public Page<Warrior> searchWarriors(
            String name, Pageable pageable) {
        Page<Warrior> warriors = warriorRepository.findByNameContainingIgnoreCase(name, pageable);
        warriors.forEach(w -> {
            populateWarriorNames(w);
            populateLeaveBalance(w, java.time.Year.now().getValue());
        });
        return warriors;
    }

    public Page<Warrior> getWarriorsWithFilters(
            String name, String rank, Pageable pageable) {
        Page<Warrior> warriors = warriorRepository.findWithFilters(name, rank, pageable);
        warriors.forEach(w -> {
            populateWarriorNames(w);
            populateLeaveBalance(w, java.time.Year.now().getValue());
        });
        return warriors;
    }

    public Page<Warrior> getLeaveManagementWarriors(String name, Integer year, Pageable pageable) {
        int targetYear = year != null ? year : java.time.Year.now().getValue();
        Page<Warrior> warriors = warriorRepository.findLeaveManagementWarriors(name, targetYear, pageable);
        warriors.forEach(w -> {
            populateWarriorNames(w);
            populateLeaveBalance(w, targetYear);
        });
        return warriors;
    }

    @Transactional
    public Warrior createWarrior(
            Warrior warrior) {
        if (warrior.getFamilyMembers() != null) {
            warrior.getFamilyMembers().forEach(member -> member.setWarrior(warrior));
        }
        Warrior saved = warriorRepository.save(warrior);

        if (warrior.getTotalLeaveDays() != null) {
            int year = java.time.Year.now().getValue();
            com.nhohantu.tcbookbe.common.model.entity.LeaveBalance lb = com.nhohantu.tcbookbe.common.model.entity.LeaveBalance
                    .builder()
                    .warriorId(saved.getId())
                    .year(year)
                    .totalLeaveDays(warrior.getTotalLeaveDays())
                    .usedLeaveDays(0)
                    .build();
            leaveBalanceRepository.save(lb);

            // Sync transient fields for the return object
            saved.setTotalLeaveDays(lb.getTotalLeaveDays());
            saved.setUsedLeaveDays(lb.getUsedLeaveDays());
        }
        return saved;
    }

    public Warrior getWarrior(String id, Integer year) {
        Warrior warrior = warriorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chiến sĩ"));
        populateWarriorNames(warrior);
        int targetYear = year != null ? year : java.time.Year.now().getValue();
        populateLeaveBalance(warrior, targetYear);
        return warrior;
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

    private void populateLeaveBalance(Warrior warrior, int year) {
        leaveBalanceRepository.findByWarriorIdAndYear(warrior.getId(), year)
                .ifPresent(lb -> {
                    warrior.setTotalLeaveDays(lb.getTotalLeaveDays());
                    warrior.setUsedLeaveDays(lb.getUsedLeaveDays() != null ? lb.getUsedLeaveDays() : 0);
                });
    }

    public Warrior updateWarrior(String id,
            Warrior warriorDetails) {
        Warrior warrior = getWarrior(id, java.time.Year.now().getValue());
        warrior.setName(warriorDetails.getName());
        warrior.setRank(warriorDetails.getRank());
        warrior.setUnit(warriorDetails.getUnit());
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

        // Delete old image if avatar changed
        if (warrior.getAvatar() != null && !warrior.getAvatar().equals(warriorDetails.getAvatar())) {
            uploadService.deleteFile(warrior.getAvatar());
        }

        warrior.setAvatar(warriorDetails.getAvatar());

        if (warriorDetails.getTotalLeaveDays() != null) {
            int year = java.time.Year.now().getValue();
            com.nhohantu.tcbookbe.common.model.entity.LeaveBalance lb = leaveBalanceRepository
                    .findByWarriorIdAndYear(id, year)
                    .orElseGet(() -> com.nhohantu.tcbookbe.common.model.entity.LeaveBalance.builder()
                            .warriorId(id)
                            .year(year)
                            .usedLeaveDays(0)
                            .build());

            int currentUsed = lb.getUsedLeaveDays() != null ? lb.getUsedLeaveDays() : 0;
            if (warriorDetails.getTotalLeaveDays() < currentUsed) {
                throw new RuntimeException("Tổng số ngày phép (" + warriorDetails.getTotalLeaveDays()
                        + ") không được nhỏ hơn số ngày đã nghỉ thực tế (" + currentUsed + ")");
            }
            lb.setTotalLeaveDays(warriorDetails.getTotalLeaveDays());
            leaveBalanceRepository.save(lb);

            // Sync transient fields
            warrior.setTotalLeaveDays(lb.getTotalLeaveDays());
            warrior.setUsedLeaveDays(lb.getUsedLeaveDays());
        }

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

    @Transactional
    public void deleteWarrior(String id) {
        Warrior warrior = getWarrior(id, java.time.Year.now().getValue());

        // Delete image from Cloudinary
        if (warrior.getAvatar() != null) {
            uploadService.deleteFile(warrior.getAvatar());
        }

        warriorRepository.delete(warrior);
    }
}
