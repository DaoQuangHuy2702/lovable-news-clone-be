package com.nhohantu.tcbookbe.common.model.entity;

import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "warriors")
public class Warrior extends BaseModel {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rank_name")
    private String rank;

    @Column(name = "unit")
    private String unit;

    @Column(name = "status")
    private String status;
}
