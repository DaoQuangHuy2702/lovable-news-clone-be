package com.nhohantu.tcbookbe.common.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "leave_requests")
public class LeaveRequest extends BaseModel {

    @Column(name = "warrior_id", length = 36, nullable = false)
    private String warriorId;

    @Column(name = "start_date", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    @Column(name = "total_days", nullable = false)
    private Integer totalDays;

    @Column(name = "location")
    private String location;

    @Column(name = "status", nullable = false)
    private String status; // PENDING, APPROVED

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
