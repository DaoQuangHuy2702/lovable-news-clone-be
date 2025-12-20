package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.entity.Warrior;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WarriorRepository extends JpaRepository<Warrior, String> {
    Page<Warrior> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT w FROM Warrior w WHERE " +
            "(:name IS NULL OR LOWER(w.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:rank IS NULL OR w.rank = :rank) AND " +
            "(:status IS NULL OR w.status = :status)")
    Page<Warrior> findWithFilters(@Param("name") String name, @Param("rank") String rank,
            @Param("status") String status, Pageable pageable);
}
