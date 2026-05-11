package com.misrshoryanelataa.misr_shoryan_elataa.Controllers;

import org.springframework.web.bind.annotation.RestController;


import com.misrshoryanelataa.misr_shoryan_elataa.Models.InterviewSlotEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.VolunteerEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Services.VolunteerService;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class VolunteerController {

    @Autowired
    VolunteerService volunteerService;

    @GetMapping("/volunteer/interview")
    public List<InterviewSlotEntity> getInterview() {
        return volunteerService.getInterview();
    }

    @GetMapping("/volunteer/available-interviewSlots")
    public List<InterviewSlotEntity> getAvailableInterviewSlots() {
        return volunteerService.getAvailableInterviewSlots();
    }

    @PostMapping("/volunteer/{volunteerId}/choose-slot/{slotId}")
    public Object chooseInterviewSlot(@PathVariable int slotId, @PathVariable int volunteerId) {
        InterviewSlotEntity slot = volunteerService.getInterview().stream()
                .filter(s -> s.getSlotID() == slotId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Interview slot not found"));
        
        return volunteerService.chooseInterviewSlot(slotId,volunteerId);
    }

@PostMapping("/create-volunteer")
public ResponseEntity<VolunteerEntity> createVolunteer(@RequestBody VolunteerEntity volunteer) {
  
        VolunteerEntity saved = volunteerService.createVolunteer(volunteer);
        return ResponseEntity.ok(saved);
   
}
}

    
