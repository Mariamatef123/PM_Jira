package com.misrshoryanelataa.misr_shoryan_elataa.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.misrshoryanelataa.misr_shoryan_elataa.Dtos.LoginResponseDTO;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.StaffEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Repos.StaffRepo;

@Service
public class StaffService {

    @Autowired
    StaffRepo staffRepo;


    public ResponseEntity<Object> login(String officialEmail, String password) {

        if (staffRepo.existsByOfficialEmail(officialEmail)) {
            StaffEntity staff = staffRepo.findByOfficialEmail(officialEmail);
            if (staff.getPassword().equals(password)) {
                LoginResponseDTO response = new LoginResponseDTO();
                response.setId(staff.getId());
                response.setName(staff.getName());
                response.setEmail(staff.getOfficialEmail());
              response.setRole(staff.getRole());
                response.setPhone(staff.getPhoneNumber());
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body("الرقم السري خطأ");
        }
        return ResponseEntity.badRequest().body("البريد الاكتروني خطأ");
    }
}