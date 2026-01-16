package com.nhohantu.tcbookbe.common.model.entity;

import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Column(name = "rank_name", nullable = false)
    private String rank;

    @Column(name = "unit", nullable = false)
    private String unit;

    @jakarta.persistence.Transient
    private Integer totalLeaveDays;

    @jakarta.persistence.Transient
    private Integer usedLeaveDays;

    @Column(name = "birth_date")
    @com.fasterxml.jackson.annotation.JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer.class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer.class)
    @org.springframework.format.annotation.DateTimeFormat(pattern = "dd/MM/yyyy")
    @jakarta.validation.constraints.PastOrPresent(message = "Ngày sinh không được vượt quá ngày hiện tại")
    private java.time.LocalDate birthDate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "strengths", columnDefinition = "TEXT")
    private String strengths;

    @Column(name = "aspirations", columnDefinition = "TEXT")
    private String aspirations;

    @Column(name = "hometown_province_code")
    private String hometownProvinceCode;

    @Column(name = "hometown_commune_code")
    private String hometownCommuneCode;

    @Column(name = "hometown_address")
    private String hometownAddress;

    @Column(name = "current_province_code")
    private String currentProvinceCode;

    @Column(name = "current_commune_code")
    private String currentCommuneCode;

    @Column(name = "current_address")
    private String currentAddress;

    @Column(name = "avatar")
    private String avatar;

    @OneToMany(mappedBy = "warrior", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<FamilyMember> familyMembers;

    @jakarta.persistence.Transient
    private String hometownProvinceName;

    @jakarta.persistence.Transient
    private String hometownCommuneName;

    @jakarta.persistence.Transient
    private String currentProvinceName;

    @jakarta.persistence.Transient
    private String currentCommuneName;
}
