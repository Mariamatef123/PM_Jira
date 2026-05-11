package com.misrshoryanelataa.misr_shoryan_elataa.DTO;

import java.util.List;

public class DonorGroupDTO {
    public DonorGroupDTO() {
        // TODO Auto-generated method stub
    }
        private int id;
    private ChildDTO child;
    private List<DonorDTO> donors;
       private int donorsCount;
    public void setChild(ChildDTO child) {
        this.child = child;
    }
    public void setDonors(List<DonorDTO> donors) {
        this.donors = donors;
    }public void setId(int id) {
        this.id = id;
    }
    public ChildDTO getChild() {
        return child;
    }public List<DonorDTO> getDonors() {
        return donors;
    }public int getId() {
        return id;
    }

    public DonorGroupDTO(int id, ChildDTO child, List<DonorDTO> donors) {
        this.id = id;
        this.child = child;
        this.donors = donors;
    }
    public void setDonorsCount(int donorsCount) {
        this.donorsCount = donorsCount;
    }
    public int getDonorsCount() {
        return donorsCount;
    }
}