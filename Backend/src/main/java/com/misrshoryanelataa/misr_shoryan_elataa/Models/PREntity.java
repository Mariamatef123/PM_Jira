package com.misrshoryanelataa.misr_shoryan_elataa.Models;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class PREntity extends StaffEntity {
    boolean isAdmin;

   
@JsonManagedReference
@OneToMany(mappedBy = "pr",  cascade = CascadeType.ALL,
           orphanRemoval = true)
private List<CampaignEntity> campaigns = new ArrayList<>();

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public PREntity() {

    }

    public void setCampaign(CampaignEntity campaign) {
        this.campaigns.add(campaign);
    }

    public List<CampaignEntity> getCampaigns() {
        return campaigns;
    }

}