package com.misrshoryanelataa.misr_shoryan_elataa.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.BloodType;
import jakarta.persistence.*;

@Entity
public class ChildEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    
    private String name;
    private int age;
    private int parentNumber;

    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    @JsonBackReference("lep-child")
    @ManyToOne(optional = false)
    @JoinColumn(name = "lep_id", nullable = false)
    private LEPEntity lep;

    public ChildEntity() {
    }

    @JsonIgnore
    @OneToOne(mappedBy = "child", cascade = CascadeType.ALL)
    private DonGroupEntity donorGroup;

    public DonGroupEntity getDonorGroup() {
        return donorGroup;
    }

    public void setDonorGroup(DonGroupEntity donorGroup) {
        this.donorGroup = donorGroup;
    }

    public LEPEntity getLep() {
        return lep;
    }

    public void setLep(LEPEntity lep) {
        this.lep = lep;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getParentNumber() {
        return parentNumber;
    }

    public void setParentNumber(int parentNumber) {
        this.parentNumber = parentNumber;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    @JsonProperty("groupId")
    public Integer getGroupId() {
        return donorGroup != null ? donorGroup.getId() : null;
    }
}
