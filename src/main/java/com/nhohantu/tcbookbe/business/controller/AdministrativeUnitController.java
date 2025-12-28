package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.business.service.AdministrativeUnitService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.dto.response.CommuneResponse;
import com.nhohantu.tcbookbe.common.model.dto.response.ProvinceResponse;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/administrative-units")
@RequiredArgsConstructor
public class AdministrativeUnitController {

    private final AdministrativeUnitService administrativeUnitService;

    @GetMapping("/provinces")
    public ResponseEntity<ResponseDTO<List<ProvinceResponse>>> getAllProvinces() {
        return ResponseBuilder.okResponse("Get provinces success",
                administrativeUnitService.getAllProvinces(),
                StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/provinces/{id}")
    public ResponseEntity<ResponseDTO<ProvinceResponse>> getProvinceById(@PathVariable String id) {
        ProvinceResponse province = administrativeUnitService.getProvinceById(id);
        if (province == null) {
            return ResponseBuilder.badRequestResponse("Province not found", StatusCodeEnum.EXCEPTION0404);
        }
        return ResponseBuilder.okResponse("Get province success",
                province,
                StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/communes")
    public ResponseEntity<ResponseDTO<List<CommuneResponse>>> getAllCommunes() {
        return ResponseBuilder.okResponse("Get communes success",
                administrativeUnitService.getAllCommunes(),
                StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/communes/{id}")
    public ResponseEntity<ResponseDTO<CommuneResponse>> getCommuneById(@PathVariable String id) {
        CommuneResponse commune = administrativeUnitService.getCommuneById(id);
        if (commune == null) {
            return ResponseBuilder.badRequestResponse("Commune not found", StatusCodeEnum.EXCEPTION0404);
        }
        return ResponseBuilder.okResponse("Get commune success",
                commune,
                StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/communes/province/{provinceCode}")
    public ResponseEntity<ResponseDTO<List<CommuneResponse>>> getCommunesByProvince(@PathVariable String provinceCode) {
        return ResponseBuilder.okResponse("Get communes by province success",
                administrativeUnitService.getCommunesByProvinceCode(provinceCode),
                StatusCodeEnum.SUCCESS2000);
    }
}
