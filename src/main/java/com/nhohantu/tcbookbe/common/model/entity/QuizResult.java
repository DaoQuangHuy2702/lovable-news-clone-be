package com.nhohantu.tcbookbe.common.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "quiz_results")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class QuizResult extends BaseModel {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth_date")
    @com.fasterxml.jackson.annotation.JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer.class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer.class)
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Column(name = "rank_name")
    private String rank;

    @Column(name = "position")
    private String position;

    @Column(name = "unit")
    private String unit;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "score")
    private Integer score;

    @Column(name = "completion_time")
    private Long completionTime; // In seconds

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Quiz quiz;
}
