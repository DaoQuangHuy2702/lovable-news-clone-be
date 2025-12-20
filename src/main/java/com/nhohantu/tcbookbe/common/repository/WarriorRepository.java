package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.entity.Warrior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarriorRepository extends JpaRepository<Warrior, String> {
    List<Warrior> findByNameContainingIgnoreCase(String name);

    @Query("SELECT w FROM Warrior w WHERE " +
            "(:name IS NULL OR LOWER(w.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:unit IS NULL OR w.unit = :unit) AND " +
            "(:status IS NULL OR w.status = :status)")
    List<Warrior> findWithFilters(@Param("name") String name, @Param("unit") String unit,
            @Param("status") String status);
}
