package com.nhohantu.tcbookbe.cms.service.implement;

import com.nhohantu.tcbookbe.common.model.entity.LeaveRequest;
import com.nhohantu.tcbookbe.common.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final com.nhohantu.tcbookbe.common.repository.LeaveBalanceRepository leaveBalanceRepository;

    public Page<LeaveRequest> getLeaveRequestsByWarrior(String warriorId, Integer year, Pageable pageable) {
        if (year != null) {
            return leaveRequestRepository.findByWarriorIdAndYear(warriorId, year, pageable);
        }
        return leaveRequestRepository.findByWarriorId(warriorId, pageable);
    }

    public LeaveRequest getById(String id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nghỉ phép"));
    }

    @Transactional
    public LeaveRequest createLeaveRequest(LeaveRequest request) {
        int year = request.getStartDate().getYear();
        com.nhohantu.tcbookbe.common.model.entity.LeaveBalance lb = leaveBalanceRepository
                .findByWarriorIdAndYear(request.getWarriorId(), year)
                .orElseThrow(() -> new RuntimeException(
                        "Quân nhân này chưa được thiết lập tổng số ngày phép cho năm " + year));

        if (lb.getTotalLeaveDays() == null) {
            throw new RuntimeException("Quân nhân này chưa được thiết lập tổng số ngày phép cho năm " + year);
        }

        // Calculate total days (inclusive)
        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        if (days <= 0) {
            throw new RuntimeException("Ngày bắt đầu phải trước hoặc bằng ngày kết thúc");
        }
        request.setTotalDays((int) days);

        int used = lb.getUsedLeaveDays() != null ? lb.getUsedLeaveDays() : 0;
        int remaining = lb.getTotalLeaveDays() - used;

        if (days > remaining) {
            throw new RuntimeException(
                    "Số ngày xin nghỉ vượt quá số ngày còn lại của năm " + year + " (" + remaining + " ngày)");
        }

        if ("APPROVED".equals(request.getStatus())) {
            lb.setUsedLeaveDays(used + (int) days);
            leaveBalanceRepository.save(lb);
        }

        return leaveRequestRepository.save(request);
    }

    @Transactional
    public LeaveRequest updateLeaveRequest(String id, LeaveRequest details) {
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nghỉ phép"));

        if ("APPROVED".equals(request.getStatus())) {
            throw new RuntimeException("Không thể sửa đơn đã được duyệt");
        }

        int year = details.getStartDate().getYear();
        com.nhohantu.tcbookbe.common.model.entity.LeaveBalance lb = leaveBalanceRepository
                .findByWarriorIdAndYear(request.getWarriorId(), year)
                .orElseThrow(() -> new RuntimeException(
                        "Quân nhân này chưa được thiết lập tổng số ngày phép cho năm " + year));

        long days = ChronoUnit.DAYS.between(details.getStartDate(), details.getEndDate()) + 1;
        if (days <= 0) {
            throw new RuntimeException("Ngày bắt đầu phải trước hoặc bằng ngày kết thúc");
        }

        int used = lb.getUsedLeaveDays() != null ? lb.getUsedLeaveDays() : 0;
        int remaining = lb.getTotalLeaveDays() - used;

        if (days > remaining) {
            throw new RuntimeException(
                    "Số ngày xin nghỉ vượt quá số ngày còn lại của năm " + year + " (" + remaining + " ngày)");
        }

        request.setStartDate(details.getStartDate());
        request.setEndDate(details.getEndDate());
        request.setTotalDays((int) days);
        request.setLocation(details.getLocation());
        request.setNotes(details.getNotes());
        request.setStatus(details.getStatus());

        if ("APPROVED".equals(request.getStatus())) {
            lb.setUsedLeaveDays(used + (int) days);
            leaveBalanceRepository.save(lb);
        }

        return leaveRequestRepository.save(request);
    }

    @Transactional
    public void deleteLeaveRequest(String id) {
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nghỉ phép"));

        if ("APPROVED".equals(request.getStatus())) {
            throw new RuntimeException("Không thể xoá đơn đã được duyệt");
        }

        leaveRequestRepository.delete(request);
    }
}
