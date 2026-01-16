package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.entity.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, String> {
    Page<LeaveRequest> findByWarriorId(String warriorId, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT lr FROM LeaveRequest lr WHERE lr.warriorId = :warriorId AND YEAR(lr.startDate) = :year AND lr.isDeleted = false")
    Page<LeaveRequest> findByWarriorIdAndYear(
            @org.springframework.data.repository.query.Param("warriorId") String warriorId,
            @org.springframework.data.repository.query.Param("year") int year, Pageable pageable);

    List<LeaveRequest> findByWarriorIdAndIsDeletedFalse(String warriorId);
}
