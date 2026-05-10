package com.misrshoryanelataa.misr_shoryan_elataa.Models;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class StaffEntity extends UserEntity {
    @Enumerated(EnumType.STRING)
    Role role;

   @Column(name = "OfficialEmail")
    String officialEmail;
   
    public void setOfficialEmail(String officialEmail) {
        this.officialEmail = officialEmail;
    }

    public String getOfficialEmail() {
        return officialEmail;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }
}