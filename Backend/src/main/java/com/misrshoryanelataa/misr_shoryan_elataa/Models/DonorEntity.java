package com.misrshoryanelataa.misr_shoryan_elataa.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.BloodType;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.DonationType;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.volunteerStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.Date;

@Entity
public class DonorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(columnDefinition = "NVARCHAR(255)")
    @Email
    private String email;
    
    @Column(columnDefinition = "NVARCHAR(255)")
    private String phone;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String city;
    private int age;
    private Date checkUP;

    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    @Enumerated(EnumType.STRING)
    private DonationType donationType;

    @Enumerated(EnumType.STRING)
    private volunteerStatus donorStatus = volunteerStatus.PENDING;

    @JsonBackReference("group-donors")
    @ManyToOne
    @JoinColumn(name = "group_id")
    private DonGroupEntity group;

    @Column(name = "donation_order")
    private Integer donationOrder;

    public volunteerStatus getDonorstatus() {
        return donorStatus;
    }

    public void setDonorstatus(volunteerStatus donorstatus) {
        this.donorStatus = donorstatus;
    }

    public Integer getDonationOrder() {
        return donationOrder;
    }

    public void setDonationOrder(Integer donationOrder) {
        this.donationOrder = donationOrder;
    }

    public DonGroupEntity getGroup() {
        return group;
    }

    public void setGroup(DonGroupEntity group) {
        this.group = group;
    }

    public DonorEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public DonationType getDonationType() {
        return donationType;
    }

    public void setDonationType(DonationType donationType) {
        this.donationType = donationType;
    }

    public Date getCheckUP() {
        return checkUP;
    }

    public void setCheckUP(Date checkUP) {
        this.checkUP = checkUP;
    }
}
