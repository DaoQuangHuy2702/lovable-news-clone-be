package com.nhohantu.tcbookbe.common.model.system;

import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "role_permission")
public class RolePermissionModel extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "role_id", columnDefinition = "VARCHAR(36)")
    private SysRoleModel role;

    @ManyToOne
    @JoinColumn(name = "permission_id", columnDefinition = "VARCHAR(36)")
    private SysPermissionModel permission;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "assigned_by")
    private String assignedBy;
}