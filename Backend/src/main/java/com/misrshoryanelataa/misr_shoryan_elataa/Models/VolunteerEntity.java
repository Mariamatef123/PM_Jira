package com.misrshoryanelataa.misr_shoryan_elataa.Models;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.Role;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.volunteerStatus;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

// @Entity
// public class VolunteerEntity extends UserEntity {
//     @Column(name="UniversityEmail")
//     String universityEmail;


    
// @JsonBackReference("hr-volunteer")  // ✅ child side
// @ManyToOne(fetch = FetchType.EAGER)
// @JoinColumn(name = "hr_id")
// private HREntity hr;

// // @ManyToOne(fetch = FetchType.EAGER)
// // @JoinColumn(name = "hr_id")
// // private HREntity hr;

//     // @JsonBackReference("volunteer")
//     // // @JsonIgnore
//     // // @JsonManagedReference("volunteer")
//     // @OneToOne
//     // @JoinColumn(name = "interview_slot_id")
//     // private InterviewSlotEntity interviewSlot;

// //     @OneToOne
// // @JoinColumn(name = "interview_slot_id")
// // private InterviewSlotEntity interviewSlot;

// @JsonProperty("interview_slot_date")
// public LocalDate getInterviewSlotDate() {
//     return interviewSlot != null ? interviewSlot.getSlotDate() : null;
// }
// @OneToOne(mappedBy = "volunteer")

// private InterviewSlotEntity interviewSlot;

// public void setInterviewSlot(InterviewSlotEntity interviewSlot) {
//     this.interviewSlot = interviewSlot;

//     if (interviewSlot != null && interviewSlot.getVolunteer() != this) {
//         interviewSlot.setVolunteer(this);
//     }
// }


// public void setHr(HREntity hr) {
//     this.hr = hr;
// }
// public HREntity getHr() {
//     return hr;
// }

//     @Enumerated(EnumType.STRING)
//     private volunteerStatus status = volunteerStatus.PENDING;

//     @Enumerated(EnumType.STRING)
//     Role AssignedDepartment;

//     String phoneNumber;
//     public void setAssignedDepartment(Role assignedDepartment) {
//         AssignedDepartment = assignedDepartment;
//     }

//     public Role getAssignedDepartment() {
//         return AssignedDepartment;
//     }

//     public void setPhoneNumber(String phoneNumber) {
//         this.phoneNumber = phoneNumber;
//     }

//     public void setUniversityEmail(String universityEmail) {
//         this.universityEmail = universityEmail;
//     }

//     public String getUniversityEmail() {
//         return universityEmail;
//     }

//     public String getPhoneNumber() {
//         return phoneNumber;
//     }



//     // public void setInterviewSlot(InterviewSlotEntity interviewSlot) {

//     //     this.interviewSlot = interviewSlot;
//     // }

//     public InterviewSlotEntity getInterviewSlot() {
//         return interviewSlot;
//     }

//     public void setStatus(volunteerStatus status) {
//         this.status = status;
//     }

//     public volunteerStatus getStatus() {
//         return status;
//     }

//     public VolunteerEntity() {

//     }
// }

@Entity
@Access(AccessType.FIELD) // ✅ IMPORTANT
public class VolunteerEntity extends UserEntity {

    @Column(name = "university_email")
    private String universityEmail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hr_id")
    @JsonBackReference("hr-volunteer")
    private HREntity hr;

    // ✅ FIXED OneToOne mapping
    @OneToOne(mappedBy = "volunteer",cascade = CascadeType.ALL,
          orphanRemoval = true)
    @JsonManagedReference("volunteer-slot")
    private InterviewSlotEntity interviewSlot;

    

    @JsonProperty("interview_slot_date")
    public LocalDate getInterviewSlotDate() {
        return interviewSlot != null ? interviewSlot.getSlotDate() : null;
    }

    public void setInterviewSlot(InterviewSlotEntity interviewSlot) {
        this.interviewSlot = interviewSlot;

        if (interviewSlot != null && interviewSlot.getVolunteer() != this) {
            interviewSlot.setVolunteer(this);
        }
    }

    public InterviewSlotEntity getInterviewSlot() {
        return interviewSlot;
    }

    public void setHr(HREntity hr) {
        this.hr = hr;
    }

    public HREntity getHr() {
        return hr;
    }

    @Enumerated(EnumType.STRING)
    private volunteerStatus status = volunteerStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private Role assignedDepartment;


    public void setAssignedDepartment(Role assignedDepartment) {
        this.assignedDepartment = assignedDepartment;
    }

    public Role getAssignedDepartment() {
        return assignedDepartment;
    }

    public void setUniversityEmail(String universityEmail) {
        this.universityEmail = universityEmail;
    }

    public String getUniversityEmail() {
        return universityEmail;
    }

    public void setStatus(volunteerStatus status) {
        this.status = status;
    }

    public volunteerStatus getStatus() {
        return status;
    }

    public VolunteerEntity() {}
}