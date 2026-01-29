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
@RequestMapping("/public/administrative-units")
@RequiredArgsConstructor
public class AdministrativeUnitController {

    private final AdministrativeUnitService administrativeUnitService;

    @GetMapping("/provinces")
    public ResponseEntity<ResponseDTO<List<ProvinceResponse>>> getAllProvinces(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String search) {
        return ResponseBuilder.okResponse("Lấy danh sách tỉnh thành thành công",
                administrativeUnitService.getAllProvinces(search),
                StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/provinces/{id}")
    public ResponseEntity<ResponseDTO<ProvinceResponse>> getProvinceById(@PathVariable String id) {
        ProvinceResponse province = administrativeUnitService.getProvinceById(id);
        if (province == null) {
            return ResponseBuilder.badRequestResponse("Không tìm thấy tỉnh thành", StatusCodeEnum.EXCEPTION0404);
        }
        return ResponseBuilder.okResponse("Lấy thông tin tỉnh thành thành công",
                province,
                StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/communes")
    public ResponseEntity<ResponseDTO<List<CommuneResponse>>> getAllCommunes(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String search) {
        return ResponseBuilder.okResponse("Lấy danh sách xã phường thành công",
                administrativeUnitService.getAllCommunes(search),
                StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/communes/{id}")
    public ResponseEntity<ResponseDTO<CommuneResponse>> getCommuneById(@PathVariable String id) {
        CommuneResponse commune = administrativeUnitService.getCommuneById(id);
        if (commune == null) {
            return ResponseBuilder.badRequestResponse("Không tìm thấy xã phường", StatusCodeEnum.EXCEPTION0404);
        }
        return ResponseBuilder.okResponse("Lấy thông tin xã phường thành công",
                commune,
                StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/communes/province/{provinceCode}")
    public ResponseEntity<ResponseDTO<List<CommuneResponse>>> getCommunesByProvince(
            @PathVariable String provinceCode,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String search) {
        return ResponseBuilder.okResponse("Lấy danh sách xã phường theo tỉnh thành thành công",
                administrativeUnitService.getCommunesByProvinceCode(provinceCode, search),
                StatusCodeEnum.SUCCESS2000);
    }
}
