package com.misrshoryanelataa.misr_shoryan_elataa.Models;

import com.misrshoryanelataa.misr_shoryan_elataa.Enums.QuestionType;
import jakarta.persistence.*;

@Entity
@Table(name = "medical_values_entity")
public class MedicalValuesEntity {

    @Id
    private Integer id;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private QuestionType questiontype;

    private Double minValue;
    private Double maxValue;
    private Boolean yesIsInvalid;
    private Integer dependsOnQuestionId;

    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public QuestionType getType() { return questiontype; }
    public Double getMinValue() { return minValue; }
    public Double getMaxValue() { return maxValue; }
    public Boolean getYesIsInvalid() { return yesIsInvalid; }
    public Integer getDependsOnQuestionId() { return dependsOnQuestionId; }
}