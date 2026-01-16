package com.nhohantu.tcbookbe.cms.controller;

import com.nhohantu.tcbookbe.cms.service.implement.LeaveRequestService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.LeaveRequest;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @GetMapping("/warrior/{warriorId}")
    public ResponseEntity<ResponseDTO<Page<LeaveRequest>>> getByWarrior(
            @PathVariable String warriorId,
            @RequestParam(required = false) Integer year,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseBuilder.okResponse("Get leave requests success",
                leaveRequestService.getLeaveRequestsByWarrior(warriorId, year, pageable),
                StatusCodeEnum.SUCCESS2000);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<LeaveRequest>> getById(@PathVariable String id) {
        return ResponseBuilder.okResponse("Get leave request success",
                leaveRequestService.getById(id),
                StatusCodeEnum.SUCCESS2000);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<LeaveRequest>> create(@RequestBody LeaveRequest request) {
        return ResponseBuilder.okResponse("Create leave request success",
                leaveRequestService.createLeaveRequest(request),
                StatusCodeEnum.SUCCESS2000);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<LeaveRequest>> update(@PathVariable String id,
            @RequestBody LeaveRequest request) {
        return ResponseBuilder.okResponse("Update leave request success",
                leaveRequestService.updateLeaveRequest(id, request),
                StatusCodeEnum.SUCCESS2000);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable String id) {
        leaveRequestService.deleteLeaveRequest(id);
        return ResponseBuilder.okResponse("Delete leave request success", null, StatusCodeEnum.SUCCESS2000);
    }
}
