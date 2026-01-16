package com.nhohantu.tcbookbe.common.model.entity;

import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "leave_balances", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "warrior_id", "year_num" })
})
public class LeaveBalance extends BaseModel {

    @Column(name = "warrior_id", nullable = false)
    private String warriorId;

    @Column(name = "year_num", nullable = false)
    private Integer year;

    @Column(name = "total_leave_days")
    private Integer totalLeaveDays;

    @Column(name = "used_leave_days")
    private Integer usedLeaveDays;
}
