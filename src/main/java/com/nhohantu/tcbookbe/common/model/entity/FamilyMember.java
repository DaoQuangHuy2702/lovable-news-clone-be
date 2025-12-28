package com.nhohantu.tcbookbe.common.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "family_members")
public class FamilyMember extends BaseModel {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "relationship")
    private String relationship; // Cha, Mẹ, Vợ, Con

    @ManyToOne
    @JoinColumn(name = "warrior_id")
    @JsonBackReference
    private Warrior warrior;
}
