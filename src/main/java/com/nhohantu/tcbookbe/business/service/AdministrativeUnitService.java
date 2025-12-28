package com.nhohantu.tcbookbe.business.service;

import com.nhohantu.tcbookbe.common.model.dto.response.CommuneResponse;
import com.nhohantu.tcbookbe.common.model.dto.response.ProvinceResponse;
import com.nhohantu.tcbookbe.common.model.entity.Commune;
import com.nhohantu.tcbookbe.common.model.entity.Province;
import com.nhohantu.tcbookbe.common.repository.CommuneRepository;
import com.nhohantu.tcbookbe.common.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdministrativeUnitService {

    private final ProvinceRepository provinceRepository;
    private final CommuneRepository communeRepository;

    public List<ProvinceResponse> getAllProvinces() {
        return provinceRepository.findAll().stream()
                .map(this::mapToProvinceResponse)
                .collect(Collectors.toList());
    }

    public ProvinceResponse getProvinceById(String id) {
        return provinceRepository.findById(id)
                .map(this::mapToProvinceResponse)
                .orElse(null);
    }

    public List<CommuneResponse> getAllCommunes() {
        return communeRepository.findAll().stream()
                .map(this::mapToCommuneResponse)
                .collect(Collectors.toList());
    }

    public CommuneResponse getCommuneById(String id) {
        return communeRepository.findById(id)
                .map(this::mapToCommuneResponse)
                .orElse(null);
    }

    public List<CommuneResponse> getCommunesByProvinceCode(String provinceCode) {
        return communeRepository.findByProvinceCode(provinceCode).stream()
                .map(this::mapToCommuneResponse)
                .collect(Collectors.toList());
    }

    private ProvinceResponse mapToProvinceResponse(Province province) {
        return ProvinceResponse.builder()
                .id(province.getId())
                .name(province.getName())
                .code(province.getCode())
                .build();
    }

    private CommuneResponse mapToCommuneResponse(Commune commune) {
        return CommuneResponse.builder()
                .id(commune.getId())
                .name(commune.getName())
                .provinceCode(commune.getProvince() != null ? commune.getProvince().getCode() : null)
                .build();
    }
}
