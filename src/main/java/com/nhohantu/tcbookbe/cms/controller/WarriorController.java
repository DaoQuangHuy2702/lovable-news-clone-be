package com.nhohantu.tcbookbe.cms.controller;

import com.nhohantu.tcbookbe.cms.service.implement.WarriorService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.Warrior;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/warriors")
@RequiredArgsConstructor
public class WarriorController {
    private final WarriorService warriorService;

    @GetMapping
    public ResponseEntity<ResponseDTO<Page<Warrior>>> getAllWarriors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String rank,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Warrior> warriors = warriorService.getWarriorsWithFilters(name, rank, pageable);
        return ResponseBuilder.okResponse("Lấy danh sách chiến sĩ thành công", warriors, StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/leave-management")
    public ResponseEntity<ResponseDTO<Page<Warrior>>> getLeaveManagementWarriors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer year,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Warrior> warriors = warriorService.getLeaveManagementWarriors(name, year, pageable);
        return ResponseBuilder.okResponse("Lấy danh sách quản lý nghỉ phép thành công", warriors,
                StatusCodeEnum.SUCCESS2000);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<Warrior>> createWarrior(@jakarta.validation.Valid @RequestBody Warrior warrior) {
        return ResponseBuilder.okResponse("Tạo mới chiến sĩ thành công", warriorService.createWarrior(warrior),
                StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Warrior>> getWarrior(@PathVariable String id,
            @RequestParam(required = false) Integer year) {
        return ResponseBuilder.okResponse("Lấy thông tin chiến sĩ thành công", warriorService.getWarrior(id, year),
                StatusCodeEnum.SUCCESS2000);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<Warrior>> updateWarrior(@PathVariable String id,
            @jakarta.validation.Valid @RequestBody Warrior warrior) {
        return ResponseBuilder.okResponse("Cập nhật thông tin chiến sĩ thành công",
                warriorService.updateWarrior(id, warrior),
                StatusCodeEnum.SUCCESS2000);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteWarrior(@PathVariable String id) {
        warriorService.deleteWarrior(id);
        return ResponseBuilder.okResponse("Xóa chiến sĩ thành công", null, StatusCodeEnum.SUCCESS2000);
    }
}
