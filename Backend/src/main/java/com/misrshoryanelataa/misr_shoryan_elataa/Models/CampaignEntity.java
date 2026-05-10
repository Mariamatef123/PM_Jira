package com.misrshoryanelataa.misr_shoryan_elataa.Models;
import java.sql.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class CampaignEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date date;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "pr_id")
    private PREntity pr;


    @Column(columnDefinition = "NVARCHAR(255)")
    String title;
    @Column(columnDefinition = "NVARCHAR(255)")
    String location;
    @Column(columnDefinition = "NVARCHAR(255)")
    String description;
    boolean isActivated;

    public void setPr(PREntity pr) {
        this.pr = pr;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public PREntity getPr() {
        return pr;
    }

    public void setIsActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public boolean getIsActivated() {
        return isActivated;
    }

    public void setId(int id) {
        this.id = id;

    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {

        return location;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public CampaignEntity() {
    }

}