package com.misrshoryanelataa.misr_shoryan_elataa.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class DonGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int orderIndex =0 ;// rotation order (1 → 6)
    private LocalDate lastRotationDate;


    @OneToOne(optional = false)
    @JoinColumn(name = "child_id", nullable = false, unique = true)
    private ChildEntity child;


    @ManyToOne(optional = false)
    @JoinColumn(name = "lep_id", nullable = false)
    private LEPEntity lep;

    @JsonManagedReference("group-donors")
    @OneToMany(mappedBy = "group", cascade = CascadeType.MERGE)
private List<DonorEntity> donors;
    public LocalDate getLastRotationDate() {
        return lastRotationDate;
    }

    public void setLastRotationDate(LocalDate lastRotationDate) {
        this.lastRotationDate = lastRotationDate;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public int getId() {
        return id;
    }

    public ChildEntity getChild() {
        return child;
    }

    public void setChild(ChildEntity child) {
        this.child = child;
    }

    public LEPEntity getLep() {
        return lep;
    }

    public void setLep(LEPEntity lep) {
        this.lep = lep;
    }

    public List<DonorEntity> getDonors() {
        return donors;
    }

    public void setDonors(List<DonorEntity> donors) {
        this.donors = donors;
    }
}
