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

    @Column(name = "rank_name", nullable = false)
    private String rank;

    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "status")
    private String status;

    @Column(name = "birth_date")
    @com.fasterxml.jackson.annotation.JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer.class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer.class)
    @org.springframework.format.annotation.DateTimeFormat(pattern = "dd/MM/yyyy")
    @jakarta.validation.constraints.PastOrPresent(message = "Ngày sinh không được vượt quá ngày hiện tại")
    private java.time.LocalDate birthDate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;
}
