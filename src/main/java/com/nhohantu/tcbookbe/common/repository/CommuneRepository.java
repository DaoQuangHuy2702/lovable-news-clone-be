package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.entity.Commune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommuneRepository extends JpaRepository<Commune, String> {
    List<Commune> findByProvinceCode(String provinceCode);

    List<Commune> findByProvinceCodeOrderByNameAsc(String provinceCode);

    List<Commune> findByProvinceCodeAndNameContainingIgnoreCaseOrderByNameAsc(String provinceCode, String name);

    List<Commune> findByNameContainingIgnoreCaseOrderByNameAsc(String name);

    List<Commune> findAllByOrderByNameAsc();
}
