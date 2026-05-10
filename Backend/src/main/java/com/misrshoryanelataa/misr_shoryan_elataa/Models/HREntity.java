package com.misrshoryanelataa.misr_shoryan_elataa.Models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class HREntity extends StaffEntity {
    Boolean isAdmin;

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

@JsonManagedReference("hr-volunteer")
@OneToMany(mappedBy = "hr",
           cascade = CascadeType.REMOVE,
           orphanRemoval = true)
private List<VolunteerEntity> volunteers;

   

    @OneToMany(mappedBy = "hr" ,cascade = CascadeType.ALL,
           orphanRemoval = true)
    private List<InterviewEntity> interviews;

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setInterview(InterviewEntity interview) {
        this.interviews.add(interview);
    }

    public List<InterviewEntity> getInterview() {
        return interviews;
    }

    public List<VolunteerEntity> getVolunteers() {
        return volunteers;
    }

    public HREntity() {

    }
public void setVolunteers(List<VolunteerEntity> volunteers) {
    this.volunteers = volunteers;
}

public void addVolunteer(VolunteerEntity volunteer) {
    volunteers.add(volunteer);
    volunteer.setHr(this); // 🔥 maintain both sides
}
}