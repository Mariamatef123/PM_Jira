package com.misrshoryanelataa.misr_shoryan_elataa.Services;

import com.misrshoryanelataa.misr_shoryan_elataa.Dtos.DashboardDTO;
import com.misrshoryanelataa.misr_shoryan_elataa.Dtos.InterviewDTO;
import com.misrshoryanelataa.misr_shoryan_elataa.Dtos.SlotDTO;
import com.misrshoryanelataa.misr_shoryan_elataa.Dtos.StaffDTO;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.InterviewStatus;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.Role;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.volunteerStatus;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.*;
import com.misrshoryanelataa.misr_shoryan_elataa.Repos.*;

import org.springframework.transaction.annotation.Transactional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HRService {



    @Autowired
    LEPRepo lepRepo;

    @Autowired
    InterviewRepo interviewRepo;

    @Autowired
    HrRepo hrRepo;

    @Autowired
    InterviewSlotsRepo interviewSlotRepo;

    @Autowired
    StaffRepo staffRepo;

    @Autowired
    VolunteerRepo volunteerRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    UserRepo userRepo;

    @Autowired
    PrRepo prRepo;


    @Autowired
ChildRepo childRepo;

@Autowired
CampaignRepo campaignRepo;
   


    private boolean isAdmin(int hrId) {
        return hrRepo.findById(hrId)
                .map(hr -> hr.getIsAdmin())
                .orElse(false);
    }
    // interview,interview slot-----------------
    public void addInterviewSlot(InterviewSlotEntity interviewSlot, int interviewId) {

        InterviewEntity interview = interviewRepo.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        interviewSlot.setInterview(interview);
        interviewSlot.setStatus(InterviewStatus.AVAILABLE);
        interviewSlot.setVolunteer(null);


        interviewSlotRepo.save(interviewSlot);
    }
    public void createInterview(int hrId, InterviewEntity interview) {

        if (!isAdmin(hrId)) {
            throw new RuntimeException("Only admins can create interviews");
        }

        HREntity hr = hrRepo.findById(hrId)
                .orElseThrow(() -> new RuntimeException("HR not found"));
                interview.setName(interview.getName() == null ? "Interview " + (hr.getInterview().size() + 1) : interview.getName());

        interview.setHr(hr);
        interviewRepo.save(interview);
        hr.getInterview().add(interview);

        hrRepo.save(hr);
    }

    public List<InterviewSlotEntity> getAllInterviewSlots() {
        return interviewSlotRepo.findAll();
    }

    // public void deleteInterviewSlot(int id, int hrId) {

    //     if (!isAdmin(hrId)) {
    //         throw new RuntimeException("Only admins can delete interview slots");
    //     }

    //     InterviewSlotEntity slot = interviewSlotRepo.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Slot not found"));

    //     InterviewEntity interview = slot.getInterview();
    //     if (interview != null) {
            
    //         interview.getInterviewSlots().remove(slot);
    //         interviewRepo.save(interview);
    //     }
    //     interviewSlotRepo.delete(slot);
    // }
    @Transactional
public void deleteInterviewSlot(int slotId, int hrId) {
            if (!isAdmin(hrId)) {
            throw new RuntimeException("Only admins can delete interview slots");
        }


    InterviewSlotEntity slot = interviewSlotRepo.findById(slotId)
            .orElseThrow(() -> new RuntimeException("Slot not found"));

    // 🔥 Step 1: find volunteers using this slot
    List<VolunteerEntity> volunteers = volunteerRepo.findByInterviewSlot(slot);

    // 🔥 Step 2: unlink them
    for (VolunteerEntity v : volunteers) {
        v.setInterviewSlot(null);
    }

    volunteerRepo.saveAll(volunteers); // update FK → null
    volunteerRepo.flush();

    // 🔥 Step 3: now delete safely
    interviewSlotRepo.delete(slot);
}

public void updateInterviewSlot(int id, InterviewSlotEntity interviewSlot, int hrId) {

    if (!isAdmin(hrId)) {
        throw new RuntimeException("Only admins can update interview slots");
    }

    InterviewSlotEntity slot = interviewSlotRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Slot not found"));

    // 🔥 DETACH first (important)
    if (slot.getVolunteer() != null) {
        VolunteerEntity v = slot.getVolunteer();
        v.setInterviewSlot(null);
        volunteerRepo.save(v);
    }

    // update fields
    slot.setSlotDate(interviewSlot.getSlotDate());
    slot.setSlotTime(interviewSlot.getSlotTime());

    interviewSlotRepo.save(slot);
}

//------------

    // Staff-----------------
// public List<StaffDTO> getAllStaff(int hrId) {

//     HREntity hr = hrRepo.findById(hrId)
//             .orElseThrow(() -> new RuntimeException("HR not found"));

//     List<StaffEntity> staffList;

//     if (hr.getIsAdmin()) {
//         staffList = staffRepo.findAll().stream().filter(staff -> staff.getRole() != Role.HR_ADMIN).toList();
//     } else {
//         staffList = staffRepo.findAll()
//                 .stream()
//                 .filter(staff -> staff.getRole() != Role.HR_ADMIN
//                         && staff.getRole() != Role.HR_MEMBER)
//                 .toList();
//     }

//     return staffList.stream()
//             .map(s -> new StaffDTO(
//                     s.getId(),
//                     s.getName(),
//                     s.getEmail(),
//                     s.getRole().name(),
//                     s.getPhoneNumber(), 
//                     s.getOfficialEmail()
//             ))
//             .toList();
// }
public List<StaffDTO> getAllStaff(int hrId) {

    HREntity hr = hrRepo.findById(hrId)
            .orElseThrow(() -> new RuntimeException("HR not found"));

    List<StaffEntity> staffList;

    if (hr.getIsAdmin()) {
        staffList = staffRepo.findAll().stream()
                .filter(s -> s.getRole() != null && s.getRole() != Role.HR_ADMIN)
                .toList();
    } else {
        staffList = staffRepo.findAll().stream()
                .filter(s -> s.getRole() != null
                        && s.getRole() != Role.HR_ADMIN
                        && s.getRole() != Role.HR_MEMBER)
                .toList();
    }

    return staffList.stream()
            .map(s -> new StaffDTO(
                    s.getId(),
                    s.getName(),
                    s.getEmail(),
                    s.getRole().name(),
                    s.getPhoneNumber(),
                    s.getOfficialEmail()
            ))
            .toList();
}
@Transactional
public void deleteStaff(int staffId, int hrId) {

    HREntity requester = hrRepo.findById(hrId)
            .orElseThrow(() -> new RuntimeException("HR not found"));

    StaffEntity staff = staffRepo.findById(staffId)
            .orElseThrow(() -> new RuntimeException("Staff not found"));

    if (!requester.getIsAdmin() && staff instanceof HREntity) {
        throw new RuntimeException("Only admins can delete HR staff");
    }

    Role role = staff.getRole();

    if (role == Role.HR_ADMIN || role == Role.HR_MEMBER) {

        HREntity hrToDelete = hrRepo.findById(staffId)
                .orElseThrow(() -> new RuntimeException("HR entity not found"));

        // STEP 1: Handle each volunteer owned by this HR
        if (hrToDelete.getVolunteers() != null) {
            List<Integer> volunteerIds = hrToDelete.getVolunteers()
                    .stream()
                    .map(v -> v.getId())
                    .collect(Collectors.toList());

            for (int vid : volunteerIds) {
                interviewSlotRepo.detachVolunteerFromSlot(vid);
                volunteerRepo.deleteVolunteerRowById(vid);
                userRepo.deleteById(vid);
            }
        }

        // STEP 2: Delete interview slots and interviews
        if (hrToDelete.getInterview() != null) {
            for (InterviewEntity interview : hrToDelete.getInterview()) {
                if (interview.getInterviewSlots() != null) {
                    for (InterviewSlotEntity slot : interview.getInterviewSlots()) {
                        interviewSlotRepo.deleteById(slot.getSlotID());
                    }
                }
                interviewRepo.delete(interview);
            }
        }

        // STEP 3: Delete HR's own rows
        // ⚠️ HR was promoted from volunteer — his volunteer_entity row may still exist
        hrRepo.deleteById(staffId);
        staffRepo.deleteById(staffId);
        if (volunteerRepo.existsVolunteerRowById(staffId) > 0) {
            interviewSlotRepo.detachVolunteerFromSlot(staffId);
            volunteerRepo.deleteVolunteerRowById(staffId);
        }
        userRepo.deleteById(staffId);
    }

    else if (role == Role.LEP) {
        lepRepo.deleteById(staffId);
        staffRepo.deleteById(staffId);
        // ⚠️ LEP was promoted from volunteer — check and clean
        if (volunteerRepo.existsVolunteerRowById(staffId) > 0) {
            interviewSlotRepo.detachVolunteerFromSlot(staffId);
            volunteerRepo.deleteVolunteerRowById(staffId);
        }
        userRepo.deleteById(staffId);
    }

    else if (role == Role.PR_ADMIN) {
        prRepo.deleteById(staffId);
        staffRepo.deleteById(staffId);
        // ⚠️ PR was promoted from volunteer — check and clean
        if (volunteerRepo.existsVolunteerRowById(staffId) > 0) {
            interviewSlotRepo.detachVolunteerFromSlot(staffId);
            volunteerRepo.deleteVolunteerRowById(staffId);
        }
        userRepo.deleteById(staffId);
    }

    else {
        // PR_MEMBER / MEDIA / DECORATION
        staffRepo.deleteById(staffId);
        // ⚠️ check here too
        if (volunteerRepo.existsVolunteerRowById(staffId) > 0) {
            interviewSlotRepo.detachVolunteerFromSlot(staffId);
            volunteerRepo.deleteVolunteerRowById(staffId);
        }
        userRepo.deleteById(staffId);
    }
}
public void updateStaff(int staffId, StaffEntity newData, int hrId) {

    HREntity requester = hrRepo.findById(hrId)
            .orElseThrow(() -> new RuntimeException("HR not found"));

    StaffEntity existingStaff = staffRepo.findById(staffId)
            .orElseThrow(() -> new RuntimeException("Staff not found"));

    if (!requester.getIsAdmin() &&
        (existingStaff.getRole() == Role.HR_ADMIN || existingStaff.getRole() == Role.HR_MEMBER)) {
        throw new RuntimeException("Only admins can update HR");
    }

    StaffEntity emailOwner = staffRepo.findByEmail(newData.getEmail());
    if (emailOwner != null && emailOwner.getId() != staffId) {
        throw new RuntimeException("Email already exists");
    }

    existingStaff.setId(staffId);
    existingStaff.setName(newData.getName());
    existingStaff.setEmail(newData.getEmail());
    existingStaff.setRole(newData.getRole());

    if (newData.getPhoneNumber() != null)
        existingStaff.setPhone(newData.getPhoneNumber());

    if (newData.getPassword() != null)
        existingStaff.setPassword(newData.getPassword());
    else
        existingStaff.setPassword(existingStaff.getPassword());

    staffRepo.save(existingStaff);
}

    public void createStaff(StaffEntity staff, int hrId) {

        HREntity requester = hrRepo.findById(hrId)
                .orElseThrow(() -> new RuntimeException(" HR not found"));

        if (!requester.getIsAdmin() && (staff.getRole() == Role.HR_ADMIN||staff.getRole() == Role.HR_MEMBER)) {
            throw new RuntimeException("Only admins can create HR");
        }
        if (staffRepo.existsByEmail(staff.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (staff.getRole() == Role.HR_ADMIN || staff.getRole() == Role.HR_MEMBER) {

            HREntity newHr = new HREntity();
            newHr.setName(staff.getName());
            newHr.setPhone(staff.getPhoneNumber());
            newHr.setEmail(staff.getEmail());
            newHr.setPassword(staff.getPassword());
            newHr.setOfficialEmail(staff.getOfficialEmail());
            newHr.setRole(staff.getRole());
            newHr.setIsAdmin(staff.getRole() == Role.HR_ADMIN);

            hrRepo.save(newHr);
            return;
        }

        if (staff.getRole() == Role.PR_ADMIN) {
            PREntity newPr = new PREntity();
            newPr.setName(staff.getName());
            newPr.setEmail(staff.getEmail());
            newPr.setPassword(staff.getPassword());
            newPr.setPhone(staff.getPhoneNumber());
            newPr.setOfficialEmail(staff.getOfficialEmail());
            newPr.setRole(staff.getRole());
            newPr.setIsAdmin(staff.getRole() == Role.PR_ADMIN);
            prRepo.save(newPr);
            return;
        }

        staffRepo.save(staff);
    }
//-------------------------

    // public List<VolunteerEntity> getAllVolunteers(int hrId) {

    //     HREntity hr = hrRepo.findById(hrId)
    //             .orElseThrow(() -> new RuntimeException("HR not found"));
    //     if(hr.getIsAdmin()){
    //         return volunteerRepo.findAll();
    //     }
    //     else{

    //         return volunteerRepo.findAll()
    //                 .stream()
    //                 .filter(volunteer -> volunteer.getHr() != null && volunteer.getHr().getId() == hrId)
    //                 .collect(Collectors.toList());
    //     }

    //     // if (!hrRepo.findAll().stream().filter(hr -> hr.getId() == hrId).findFirst().map(hr -> hr.getIsAdmin())
    //     //         .orElse(false)) {
    //     //     throw new RuntimeException("Only admins can get volunteers");
    //     // }
    //     // return volunteerRepo.findAll();
    // }

   public List<Object> getVolunteersEditable(int hrId){
            HREntity hr = hrRepo.findById(hrId)
                .orElseThrow(() -> new RuntimeException("HR not found"));
                        if(!hr.getIsAdmin()){
                LocalDate today = LocalDate.now();
             
       return interviewSlotRepo.findAll().stream()
                .filter(slot -> slot.getSlotDate().isBefore(today) && slot.getVolunteer() != null && slot.getVolunteer().getHr() != null && slot.getVolunteer().getHr().getId() == hrId)
                .map(slot -> {
                    VolunteerEntity volunteer = slot.getVolunteer();
                    
                    if (!hr.getVolunteers().contains(volunteer)) {
                        hr.getVolunteers().add(volunteer);
                    }
                    return volunteer;
                }).collect(Collectors.toList());}
                else{
                    return new ArrayList<>();
                }
   }

    // public void assignVolunteerToHR(int assignerHrId, int volunteerId, int targetHrId) {

    //     HREntity assigner = hrRepo.findById(assignerHrId)
    //             .orElseThrow(() -> new RuntimeException("HR not found"));

    //     if (!assigner.getIsAdmin()) {
    //         throw new RuntimeException("Only admins can assign volunteers");
    //     }

    //     VolunteerEntity volunteer = volunteerRepo.findById(volunteerId)
    //             .orElseThrow(() -> new RuntimeException("Volunteer not found"));

    //     HREntity targetHr = hrRepo.findById(targetHrId)
    //             .orElseThrow(() -> new RuntimeException("Target HR not found"));

    //     targetHr.getVolunteers().add(volunteer);
    //     volunteer.setHr(targetHr);

    //     hrRepo.save(targetHr);
    //     volunteerRepo.save(volunteer);
    // }
@Transactional
public void assignVolunteerToHR(int assignerHrId, int volunteerId, int targetHrId) {

    HREntity assigner = hrRepo.findById(assignerHrId)
            .orElseThrow(() -> new RuntimeException("HR not found"));

    if (!Boolean.TRUE.equals(assigner.getIsAdmin())) {
        throw new RuntimeException("Only admins can assign volunteers");
    }

    VolunteerEntity volunteer = volunteerRepo.findById(volunteerId)
            .orElseThrow(() -> new RuntimeException("Volunteer not found"));

    HREntity targetHr = hrRepo.findById(targetHrId)
            .orElseThrow(() -> new RuntimeException("Target HR not found"));

    // 🔥 clean previous relation (VERY IMPORTANT)
    HREntity oldHr = volunteer.getHr();
    if (oldHr != null) {
        oldHr.getVolunteers().remove(volunteer);
    }

    // 🔥 set new relation (owning side)
    volunteer.setHr(targetHr);

    // optional: keep consistency on inverse side
    targetHr.getVolunteers().add(volunteer);
}
    // public void sendEmailToVolunteer(int volunteerId, int hrId) {
    //     HREntity hr = hrRepo.findById(hrId)
    //             .orElseThrow(() -> new RuntimeException("HR not found"));

    //     VolunteerEntity volunteer = volunteerRepo.findById(volunteerId)
    //             .orElseThrow(() -> new RuntimeException("Volunteer not found"));

    //     String toEmail = volunteer.getUniversityEmail();
    //     String subject = "Update on Your Volunteer Application";
    //     String emailContent;
    //     Role assignedDepartment = volunteer.getAssignedDepartment();

    //     if (volunteer.getStatus() == volunteerStatus.REJECTED) {
    //         emailContent = "Dear " + volunteer.getName() +
    //                 ",\n\nWe regret to inform you that your application has been rejected.";

    //     } else {
    //         if (assignedDepartment == Role.PR_ADMIN ||
    //                 assignedDepartment == Role.LEP ||
    //                 assignedDepartment == Role.HR_ADMIN ||
    //                 assignedDepartment == Role.HR_MEMBER) {

    //             String cleanName = volunteer.getName().toLowerCase().replaceAll("\\s+", "");
    //             String generated;

    //             do {
    //                 generated = cleanName
    //                         + volunteer.getId()
    //                         + new Random().nextInt(1000)
    //                         + "@misrshoryanelataa.com";
    //             } while (staffRepo.existsByEmail(generated));

    //             String emailGenerated = generated;
    //             String passwordGenerated = UUID.randomUUID().toString().substring(0, 8);
    //             StaffEntity staff = new StaffEntity();
    //             staff.setName(volunteer.getName());
    //             staff.setEmail(volunteer.getEmail());
    //             staff.setOfficialEmail(emailGenerated);
    //             staff.setPassword(passwordGenerated);
    //             staff.setRole(assignedDepartment);
    //             staffRepo.save(staff);

    //             emailContent = "Dear " + volunteer.getName() +
    //                     ",\n\nCongratulations! Your application has been accepted.\n" +
    //                     "You have been assigned to the " + assignedDepartment + " department.\n\n" +
    //                     "Your email is: " + emailGenerated + "\n" +
    //                     "Your password is: " + passwordGenerated + "\n\n" +
    //                     "Please use these credentials to log in to the volunteer portal.";

    //         } else {
    //             emailContent = "Dear " + volunteer.getName() +
    //                     ",\n\nCongratulations! Your application has been accepted.\n" +
    //                     "You have been assigned to the " + assignedDepartment + " department.\n\n";
    //         }
    //     }

    //     SimpleMailMessage message = new SimpleMailMessage();
    //     message.setTo(toEmail);
    //     message.setSubject(subject);
    //     message.setText(emailContent);
    //     message.setFrom("misrshoryanelataa@gmail.com");
    //     mailSender.send(message);
    // }
   
    public List<VolunteerEntity> getVolunteersAssignedToHR(int hrId) {
        HREntity hr = hrRepo.findById(hrId)
                .orElseThrow(() -> new RuntimeException("HR not found"));
        return hr.getVolunteers().stream()
                .filter(volunteer -> volunteer.getHr() != null && volunteer.getHr().getId() == hrId&& volunteer.getStatus() == volunteerStatus.PENDING)
                .collect(Collectors.toList());
    }

    @Transactional
public void acceptVolunteer(int volunteerId, int hrId, Role assignedDepartment) {

    HREntity hr = hrRepo.findById(hrId)
            .orElseThrow(() -> new RuntimeException("HR not found"));

    VolunteerEntity volunteer = volunteerRepo.findById(volunteerId)
            .orElseThrow(() -> new RuntimeException("Volunteer not found"));

    // Update volunteer status
    volunteer.setStatus(volunteerStatus.ACCEPTED);
    volunteer.setAssignedDepartment(assignedDepartment);
    volunteerRepo.save(volunteer);

    String generatedEmail = null;
    String generatedPassword = null;

    boolean needsCredentials = assignedDepartment == Role.HR_ADMIN
            || assignedDepartment == Role.HR_MEMBER
            || assignedDepartment == Role.PR_ADMIN
            || assignedDepartment == Role.LEP;

    if (needsCredentials) {
        // Generate unique official email
        String cleanName = volunteer.getName().toLowerCase().replaceAll("\\s+", "");
        do {
            generatedEmail = cleanName + volunteerId + new Random().nextInt(1000) + "@misrshoryanelataa.com";
        } while (staffRepo.existsByOfficialEmail(generatedEmail));

        generatedPassword = UUID.randomUUID().toString().substring(0, 8);

        // Insert into staff_entity only (user_entity row already exists)
        staffRepo.insertStaffRow(volunteerId, assignedDepartment.name(), generatedEmail);

        // Update password in user_entity
        userRepo.updatePassword(volunteerId, generatedPassword);
    }

    // Insert into subclass table only
    if (assignedDepartment == Role.HR_ADMIN || assignedDepartment == Role.HR_MEMBER) {
        hrRepo.insertHrRow(volunteerId, assignedDepartment == Role.HR_ADMIN);
    } else if (assignedDepartment == Role.LEP) {
        lepRepo.insertLepRow(volunteerId);
    } else if (assignedDepartment == Role.PR_ADMIN) {
        prRepo.insertPrRow(volunteerId, true);
    }
    else{
        staffRepo.insertStaffRow(volunteerId, assignedDepartment.name(), null);
    }

    sendEmailToVolunteer(volunteerId, hrId, generatedEmail, generatedPassword);
}

public void sendEmailToVolunteer(int volunteerId, int hrId, String generatedEmail, String generatedPassword) {

    VolunteerEntity volunteer = volunteerRepo.findById(volunteerId)
            .orElseThrow(() -> new RuntimeException("Volunteer not found"));

    String toEmail = volunteer.getEmail();
    String subject = "Update on Your Volunteer Application";
    String emailContent;
    Role assignedDepartment = volunteer.getAssignedDepartment();

    if (volunteer.getStatus() == volunteerStatus.REJECTED) {
        emailContent = "Dear " + volunteer.getName() +
                ",\n\nWe regret to inform you that your application has been rejected.";
    } else {
        if (generatedEmail != null && generatedPassword != null) {
            emailContent = "Dear " + volunteer.getName() +
                    ",\n\nCongratulations! Your application has been accepted.\n" +
                    "You have been assigned to the " + assignedDepartment + " department.\n\n" +
                    "Your email is: " + generatedEmail + "\n" +
                    "Your password is: " + generatedPassword + "\n\n" +
                    "Please use these credentials to log in to the volunteer portal.";
        } else {
            emailContent = "Dear " + volunteer.getName() +
                    ",\n\nCongratulations! Your application has been accepted.\n" +
                    "You have been assigned to the " + assignedDepartment + " department.\n\n";
        }
    }

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject(subject);
    message.setText(emailContent);
    message.setFrom("misrshoryanelataa@gmail.com");
    mailSender.send(message);
}

// public void rejectVolunteer(int volunteerId, int hrId) {

//     HREntity hr = hrRepo.findById(hrId)
//             .orElseThrow(() -> new RuntimeException("HR not found"));

//     VolunteerEntity volunteer = volunteerRepo.findById(volunteerId)
//             .orElseThrow(() -> new RuntimeException("Volunteer not found"));

//     volunteer.setStatus(volunteerStatus.REJECTED);
//     volunteerRepo.save(volunteer);

//     sendEmailToVolunteer(volunteerId, hrId, null, null);

//     if (!hr.getIsAdmin()) {
//         volunteer.setHr(null);
//         volunteerRepo.save(volunteer);
//         hr.getVolunteers().remove(volunteer);
//         hrRepo.save(hr);
//         volunteerRepo.delete(volunteer);
//         userRepo.delete(volunteer);
//     }
// }
// @Transactional
// public void acceptVolunteer(int volunteerId, int hrId, Role assignedDepartment) {

//     HREntity hr = hrRepo.findById(hrId)
//             .orElseThrow(() -> new RuntimeException("HR not found"));

//     VolunteerEntity volunteer = volunteerRepo.findById(volunteerId)
//             .orElseThrow(() -> new RuntimeException("Volunteer not found"));

//     // ================= UPDATE VOLUNTEER =================
//     volunteer.setStatus(volunteerStatus.ACCEPTED);
//     volunteer.setAssignedDepartment(assignedDepartment);
//     volunteerRepo.save(volunteer);

//     // ================= GET EXISTING USER DATA =================
//     UserEntity user = userRepo.findByEmail(volunteer.getEmail())
//             .orElseThrow(() -> new RuntimeException("User not found"));

//     // ================= HR =================
//     if (assignedDepartment == Role.HR_ADMIN || assignedDepartment == Role.HR_MEMBER) {

//         if (!hrRepo.existsById(user.getId())) {

//             HREntity newHr = new HREntity();

//             newHr.setId(user.getId()); // ✅ IMPORTANT FIX
//             newHr.setName(user.getName());
//             newHr.setEmail(user.getEmail());
//             newHr.setPhone(user.getPhoneNumber());
//             newHr.setPassword(user.getPassword());

//             newHr.setIsAdmin(assignedDepartment == Role.HR_ADMIN);
//             newHr.setRole(assignedDepartment);

//             hrRepo.save(newHr);
//         }
//     }

//     // ================= LEP =================
//     else if (assignedDepartment == Role.LEP) {

//         if (!lepRepo.existsById(user.getId())) {

//             LEPEntity lep = new LEPEntity();

//             lep.setId(user.getId()); // ✅ IMPORTANT FIX
//             lep.setName(user.getName());
//             lep.setEmail(user.getEmail());
//             lep.setPhone(user.getPhoneNumber());
//             lep.setPassword(user.getPassword());

//             lep.setRole(assignedDepartment);

//             lepRepo.save(lep);
//         }
//     }

//     // ================= PR =================
//     else if (assignedDepartment == Role.PR_ADMIN) {

//         if (!prRepo.existsById(user.getId())) {

//             PREntity pr = new PREntity();

//             pr.setId(user.getId()); // ✅ IMPORTANT FIX
//             pr.setName(user.getName());
//             pr.setEmail(user.getEmail());
//             pr.setPhone(user.getPhoneNumber());
//             pr.setPassword(user.getPassword());

//             pr.setIsAdmin(true);
//             pr.setRole(assignedDepartment);

//             prRepo.save(pr);
//         }
//     }

//     sendEmailToVolunteer(volunteerId, hrId);
// }
  

// public void acceptVolunteer(int volunteerId, int hrId, Role assignedDepartment) {
    //     HREntity hr = hrRepo.findById(hrId)
    //             .orElseThrow(() -> new RuntimeException("HR not found"));

    //     VolunteerEntity volunteer = volunteerRepo.findById(volunteerId)
    //             .orElseThrow(() -> new RuntimeException("Volunteer not found"));

    //     volunteer.setStatus(volunteerStatus.ACCEPTED);
    //     volunteer.setAssignedDepartment(assignedDepartment);
      
    //     volunteerRepo.save(volunteer);
    //       if(assignedDepartment==Role.HR_ADMIN|| assignedDepartment==Role.HR_MEMBER){
    //         HREntity newHr=new HREntity();
    //         newHr.setIsAdmin(assignedDepartment==Role.HR_ADMIN);
    //         // newHr.setId(volunteerId);
    //         hrRepo.save(newHr);

    //       }

    //     if(assignedDepartment==Role.LEP){
    //         LEPEntity lep =new LEPEntity();
    //         // lep.setId(volunteerId);
    //         LepRepo.save(lep);
    //     }
    //     if(assignedDepartment==Role.PR_ADMIN){
    //         PREntity pr =new PREntity();
    //         prRepo.save(pr);

    //     }
    //     sendEmailToVolunteer(volunteerId, hrId);
    // }


    //     public void acceptVolunteer(int volunteerId, int hrId, Role assignedDepartment) {
//     HREntity hr = hrRepo.findById(hrId)
//             .orElseThrow(() -> new RuntimeException("HR not found"));

//     VolunteerEntity volunteer = volunteerRepo.findById(volunteerId)
//             .orElseThrow(() -> new RuntimeException("Volunteer not found"));


//     volunteer.setStatus(volunteerStatus.ACCEPTED);
//     volunteer.setAssignedDepartment(assignedDepartment);
//     volunteerRepo.save(volunteer);

//     if (assignedDepartment == Role.HR_ADMIN || assignedDepartment == Role.HR_MEMBER) {
//         HREntity newHr = new HREntity();
//         newHr.setName(volunteer.getName());
//         newHr.setEmail(volunteer.getEmail());
//         newHr.setPhone(volunteer.getPhoneNumber());
//         newHr.setPassword(volunteer.getPassword());
//         newHr.setIsAdmin(assignedDepartment == Role.HR_ADMIN);
//         newHr.setRole(assignedDepartment);
//         hrRepo.save(newHr);
//     }

//     if (assignedDepartment == Role.LEP) {
//         LEPEntity lep = new LEPEntity();
//         lep.setName(volunteer.getName());
//         lep.setEmail(volunteer.getEmail());
//         lep.setPhone(volunteer.getPhoneNumber());
//         lep.setPassword(volunteer.getPassword());
//         lep.setRole(assignedDepartment);
//         LepRepo.save(lep);
//     }

//     if (assignedDepartment == Role.PR_ADMIN) {
//         PREntity pr = new PREntity();
//         pr.setName(volunteer.getName());
//         pr.setEmail(volunteer.getEmail());
//         pr.setPhone(volunteer.getPhoneNumber());
//         pr.setPassword(volunteer.getPassword());
//         pr.setIsAdmin(true);
//         pr.setRole(assignedDepartment);
//         prRepo.save(pr);
//     }

//     sendEmailToVolunteer(volunteerId, hrId);
// }

    public void chooseDepartmentForVolunteer(int volunteerId, int hrId, Role department) {
        HREntity hr = hrRepo.findById(hrId)
                .orElseThrow(() -> new RuntimeException("HR not found"));

        VolunteerEntity volunteer = volunteerRepo.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        if (!hr.getVolunteers().contains(volunteer)) {
            throw new RuntimeException("Volunteer is not assigned to this HR");
        }
        if (!hr.getIsAdmin() && volunteer.getStatus() == volunteerStatus.ACCEPTED) {
            volunteer.setAssignedDepartment(department);
            volunteerRepo.save(volunteer);
        }
    }
    // public void rejectVolunteer(int volunteerId, int hrId) {
    //     HREntity hr = hrRepo.findById(hrId)
    //             .orElseThrow(() -> new RuntimeException("HR not found"));

    //     VolunteerEntity volunteer = volunteerRepo.findById(volunteerId)
    //             .orElseThrow(() -> new RuntimeException("Volunteer not found"));

    //     volunteer.setStatus(volunteerStatus.REJECTED);
    //     volunteerRepo.save(volunteer);
      
    //     if (!hr.getIsAdmin()) {
    //         // sendEmailToVolunteer(volunteerId, hrId);
    //         // interviewSlotRepo.clearVolunteerReference(volunteerId);
            
    //         volunteerRepo.deleteByHrId(volunteerId);
    //         volunteerRepo.delete(volunteer);
    //           userRepo.delete(volunteer);
    //     }
    // }
    public void rejectVolunteer(int volunteerId, int hrId) {
    HREntity hr = hrRepo.findById(hrId)
            .orElseThrow(() -> new RuntimeException("HR not found"));

    VolunteerEntity volunteer = volunteerRepo.findById(volunteerId)
            .orElseThrow(() -> new RuntimeException("Volunteer not found"));

    volunteer.setStatus(volunteerStatus.REJECTED);
    volunteerRepo.save(volunteer);
sendEmailToVolunteer( volunteerId, hrId, null,null);
    if (!hr.getIsAdmin()) {
        // Detach from HR before deleting
        volunteer.setHr(null);
        volunteerRepo.save(volunteer);

        // Remove from HR's list
        hr.getVolunteers().remove(volunteer);
        hrRepo.save(hr);

        volunteerRepo.delete(volunteer);
        userRepo.delete(volunteer); // only if UserEntity has its own table
    }
}

       public List<InterviewDTO> getAllInterviewsForHR(int hrId) {
        HREntity hr = hrRepo.findById(hrId)
                .orElseThrow(() -> new RuntimeException("HR not found"));

        return interviewRepo.findAll().stream()
                .filter(interview -> interview.getHr() != null
                        && interview.getHr().getId() == hrId)
                .map(i -> new InterviewDTO(
                        i.getId(),
                        i.getName(),
                        i.getInterviewSlots().stream()
                                .map(s -> {
                                    SlotDTO slotDTO = new SlotDTO();
                                    slotDTO.setId(s.getSlotID());
                                    slotDTO.setDate(s.getSlotDate() != null ? s.getSlotDate().toString() : null);
                                    slotDTO.setTime(s.getSlotTime() != null ? s.getSlotTime().toString() : null);
                                    return slotDTO;
                                })
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
    public void deleteInterview(int interviewId, int hrId) {
        HREntity hr = hrRepo.findById(hrId)
                .orElseThrow(() -> new RuntimeException("HR not found"));

        InterviewEntity interview = interviewRepo.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        if (interview.getHr() == null || interview.getHr().getId() != hrId) {
            throw new RuntimeException("Interview does not belong to this HR");
        }


        interviewRepo.findById(interviewId).ifPresent(i -> {
            i.getInterviewSlots().forEach(slot -> {
                slot.setInterview(null);
                interviewSlotRepo.save(slot);
            });
        });
        interviewRepo.delete(interview);
    }
    public List<SlotDTO> getInterviewSlots(int interviewId) {
        InterviewEntity interview = interviewRepo.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        return interview.getInterviewSlots().stream()
                .map(s -> {
                    SlotDTO slotDTO = new SlotDTO();
                    slotDTO.setId(s.getSlotID());
                    slotDTO.setDate(s.getSlotDate() != null ? s.getSlotDate().toString() : null);
                    slotDTO.setTime(s.getSlotTime() != null ? s.getSlotTime().toString() : null);
                    slotDTO.setStatus(s.getStatus());
                    return slotDTO;
                })
                .collect(Collectors.toList());
    }
// getPendingVolunteers
// public List<VolunteerEntity> getPendingVolunteers(int hrId) {
//     HREntity hr = hrRepo.findById(hrId)
//             .orElseThrow(() -> new RuntimeException("HR not found"));

//     if (hr.getIsAdmin()) {
//         return volunteerRepo.findPendingWithNoHR(volunteerStatus.PENDING); // ✅ no findAll()
//     }

//     return hr.getVolunteers().stream()
//             .filter(v -> v.getStatus() == volunteerStatus.PENDING)
//             .collect(Collectors.toList());
// }
@Transactional(readOnly = true)
public List<VolunteerEntity> getPendingVolunteers(int hrId) {

    HREntity hr = hrRepo.findById(hrId)
            .orElseThrow(() -> new RuntimeException("HR not found"));

    Boolean isAdmin = hr.getIsAdmin();

    // ✅ ADMIN: fetch directly from DB (best practice)
    if (Boolean.TRUE.equals(isAdmin)) {
        return volunteerRepo.findByStatusAndHrIsNull(volunteerStatus.PENDING);
    }

    // ✅ NON-ADMIN: also fetch from DB (NOT from entity list)
    return volunteerRepo.findByHrIdAndStatus(hrId, volunteerStatus.PENDING);
}

// getAllVolunteers
// public List<VolunteerEntity> getAllVolunteers(int hrId) {
//     HREntity hr = hrRepo.findById(hrId)
//             .orElseThrow(() -> new RuntimeException("HR not found"));

//     if (hr.getIsAdmin()) {
//         return volunteerRepo.findAll(); // ✅ safe
//     }

//     return hr.getVolunteers(); // ✅ correct
// }
@Transactional
public List<VolunteerEntity> getAllVolunteers(int hrId) {
    HREntity hr = hrRepo.findById(hrId)
            .orElseThrow(() -> new RuntimeException("HR not found"));

    if (Boolean.TRUE.equals(hr.getIsAdmin())) {
        return volunteerRepo.findAllVolunteers(); // safe query, not findAll()
    }

    List<VolunteerEntity> volunteers = hr.getVolunteers();
    return (volunteers != null) ? volunteers.stream()
            .filter(v -> v.getHr() != null && v.getHr().getId() == hrId)
            .collect(Collectors.toList()) : new ArrayList<>();
}


public List<StaffDTO> getHrMembers() {
  

        return staffRepo.findAll().stream()
                .filter(staff -> staff.getRole() == Role.HR_MEMBER)
                .map(s -> new StaffDTO(
                        s.getId(),
                        s.getName(),
                        s.getEmail(),
                        s.getRole().name(),
                        s.getPhoneNumber(), 
                        s.getOfficialEmail()
                ))
                .collect(Collectors.toList());
    }
    public List<InterviewDTO> getAllInterviews() {
        return interviewRepo.findAll().stream()
                .map(i -> new InterviewDTO(
                        i.getId(),
                        i.getName(),
                        i.getInterviewSlots().stream()
                                .map(s -> {
                                    SlotDTO slotDTO = new SlotDTO();
                                    slotDTO.setId(s.getSlotID());
                                    slotDTO.setDate(s.getSlotDate() != null ? s.getSlotDate().toString() : null);
                                    slotDTO.setTime(s.getSlotTime() != null ? s.getSlotTime().toString() : null);
                                    return slotDTO;
                                })
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    // public DashboardDTO buildDashboard(int hrId) {




    //     List<VolunteerEntity> volunteers = volunteerRepo.findByHrId(hrId);

    //     DashboardDTO dto = new DashboardDTO();

    //     dto.setTotalVolunteers(volunteers.size());

    //     dto.setApproved(
    //             (int) volunteers.stream()
    //                     .filter(v -> v.getStatus().name().equals("APPROVED"))
    //                     .count()
    //     );

    //     dto.setPending(
    //             (int) volunteers.stream()
    //                     .filter(v -> v.getStatus().name().equals("PENDING"))
    //                     .count()
    //     );

    //     dto.setRejected(
    //             (int) volunteers.stream()
    //                     .filter(v -> v.getStatus().name().equals("REJECTED"))
    //                     .count()
    //     );

    //     dto.setInterview(
    //             (int) volunteers.stream()
    //                     .filter(v -> v.getStatus().name().equals("INTERVIEW"))
    //                     .count()
    //     );

    //     dto.setStaffCount(
    //             staffRepo.findByHrId(hrId).size()
    //     );

    //     return dto;
    

    // }

}