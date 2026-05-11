package com.misrshoryanelataa.misr_shoryan_elataa.Services;
import com.misrshoryanelataa.misr_shoryan_elataa.Repos.VolunteerRepo;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.InterviewStatus;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.InterviewEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.InterviewSlotEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.VolunteerEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Repos.InterviewRepo;
import com.misrshoryanelataa.misr_shoryan_elataa.Repos.InterviewSlotsRepo;

@Service
public class VolunteerService {
    
    @Autowired
    private InterviewSlotsRepo interviewSlotRepo;

    @Autowired
    private InterviewRepo interviewRepo;

    @Autowired
    private VolunteerRepo volunteerRepo;
    public List<InterviewSlotEntity> getInterview() {
        return interviewSlotRepo.findAll();
    }

    public List<InterviewSlotEntity> getAvailableInterviewSlots() {
        return interviewRepo.findAll().stream()
                .flatMap(interview -> interview.getInterviewSlots().stream())
                .filter(slot -> slot.getStatus() == InterviewStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    // public Object chooseInterviewSlot(InterviewSlotEntity slot,int volunteerId) {
    //     InterviewSlotEntity interviewSlot = interviewRepo.findAll().stream()
    //             .flatMap(interview -> interview.getInterviewSlots().stream())
    //             .filter(s -> s.getSlotID() == slot.getSlotID())
    //             .findFirst()
    //             .orElseThrow(() -> new RuntimeException("Interview slot not found"));
    //         VolunteerEntity volunteer = volunteerRepo.findById(volunteerId)
    //             .orElseThrow(() -> new RuntimeException("Volunteer not found"));

    //     if (interviewSlot.getStatus() != InterviewStatus.AVAILABLE) {
    //         throw new RuntimeException("Interview slot is not available");
    //     }
    //     interviewSlot.setStatus(InterviewStatus.UNAVAILABLE);
    //     interviewSlot.setVolunteer(volunteer);
    //     volunteer.setInterviewSlot(interviewSlot);
        
    //     InterviewEntity interview = new InterviewEntity();
    //     volunteerRepo.save(volunteer);
    //     interviewRepo.save(interview);
    //     return "Interview slot booked successfully";
    // }

@Transactional
public Object chooseInterviewSlot(int slotId, int volunteerId) {

    InterviewSlotEntity slot = interviewSlotRepo.findById(slotId)
            .orElseThrow(() -> new RuntimeException("Interview slot not found"));

    VolunteerEntity volunteer = volunteerRepo.findById(volunteerId)
            .orElseThrow(() -> new RuntimeException("Volunteer not found"));

    if (slot.getStatus() != InterviewStatus.AVAILABLE) {
        throw new RuntimeException("Slot not available");
    }

    if (volunteer.getInterviewSlot() != null) {
        throw new RuntimeException("Volunteer already booked a slot");
    }

    // 🔥 set both sides (IMPORTANT)
    slot.setStatus(InterviewStatus.UNAVAILABLE);
    slot.setVolunteer(volunteer); // this will auto-link both sides now
    volunteer.setInterviewSlot(slot);
    // 💾 save OWNER ONLY is enough (InterviewSlot owns relation)
    interviewSlotRepo.save(slot);
    volunteerRepo.save(volunteer);
    return "Interview slot booked successfully";
}

public VolunteerEntity createVolunteer(VolunteerEntity volunteer) {

    if (volunteerRepo.existsByUniversityEmail(volunteer.getUniversityEmail())) {
        throw new RuntimeException("Email already exists");
    }

    if (volunteer.getUniversityEmail() == null ||
        !volunteer.getUniversityEmail().endsWith("@med.asu.eg")) {

        throw new RuntimeException("must enter your university email");
    }

    volunteer.setAssignedDepartment(null);
   

    
    return volunteerRepo.save(volunteer); 
}

}