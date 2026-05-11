package com.misrshoryanelataa.misr_shoryan_elataa.Services;

import com.misrshoryanelataa.misr_shoryan_elataa.DTO.ChildDTO;
import com.misrshoryanelataa.misr_shoryan_elataa.DTO.DonorDTO;
import com.misrshoryanelataa.misr_shoryan_elataa.DTO.DonorGroupDTO;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.DonationType;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.volunteerStatus;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.ChildEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.DonGroupEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.DonorEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.HREntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.LEPEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.VolunteerEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Repos.ChildRepo;
import com.misrshoryanelataa.misr_shoryan_elataa.Repos.DonGroupRepo;
import com.misrshoryanelataa.misr_shoryan_elataa.Repos.DonorRepo;
import com.misrshoryanelataa.misr_shoryan_elataa.Repos.LEPRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LEPService {
    @Autowired
    private ChildRepo childRepo;

    @Autowired
    private DonorRepo donorRepo;

    @Autowired
    private DonGroupRepo donGroupRepo;

    @Autowired
    private DonorService donorService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private LEPRepo lepRepo;

    public ChildEntity createChild(ChildEntity child, int lepId) {
        LEPEntity lep = lepRepo.findById(lepId)
                .orElseThrow(() -> new RuntimeException("LEP not found"));
        child.setLep(lep);

        return childRepo.save(child);
    }
    public Object getChildById(int id) {
        try {
            return childRepo.findById(id).orElse(null);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
//    public List<ChildEntity> getAllChildren() {
//        return childRepo.findAll();
//    }
    public Object updateChild(int id, ChildEntity child, int lepId) {
        try {
            if (childRepo.existsById(id)) {
                child.setId(id);
                LEPEntity lep = lepRepo.findById(lepId)
                        .orElseThrow(() -> new RuntimeException("LEP not found"));;
                child.setLep(lep);
                return childRepo.save(child);
            } else {
                return "Child not found";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public Object deleteChild(int id) {

        ChildEntity child = childRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Child not found"));

        DonGroupEntity group = child.getDonorGroup();
        if (group != null) {
            // break relations first
            for (DonorEntity donor : group.getDonors()) {
                donor.setGroup(null);
            }

            donGroupRepo.delete(group);
        }
        childRepo.delete(child);
        return "Child and related group deleted successfully";
    }

    public List<ChildEntity> getChildrenForLep(int lepId) {
        return childRepo.findByLepId(lepId);
    }

    ///////DONORGROUP///////
    public Object createGroup(int childId, List<Integer> donorIds) {

        ChildEntity child = childRepo.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child not found"));

        if (child.getDonorGroup() != null) {
            throw new RuntimeException("Child already has a group");
        }

        if (donorIds.size() < 3 || donorIds.size() > 6) {
            throw new RuntimeException("Donors must be between 3 and 6");
        }

        List<DonorEntity> donors = donorRepo.findAllById(donorIds);

        for (DonorEntity donor : donors) {

            if (donor.getGroup() != null) {
                throw new RuntimeException("Donor already assigned");
            }

            if (!donor.getDonationType().equals(DonationType.REPEAT)) {
                throw new RuntimeException("Donor must be repeat");
            }

            if (!donor.getDonorstatus().equals(volunteerStatus.ACCEPTED)) {
                throw new RuntimeException("Donor must be accepted");
            }

            if (!donor.getBloodType().equals(child.getBloodType())) {
                throw new RuntimeException("Blood type mismatch");
            }
        }

        DonGroupEntity group = new DonGroupEntity();
        group.setChild(child);
        group.setLep(child.getLep());
        group.setOrderIndex(0);


        List<DonorEntity> orderedDonors = new ArrayList<>();

        for (int i = 0; i < donors.size(); i++) {
            DonorEntity donor = donors.get(i);

            donor.setGroup(group);
            donor.setDonationOrder(i);

            orderedDonors.add(donor);
        }

        group.setDonors(donors);
        group.setLastRotationDate(LocalDate.now());
        child.setDonorGroup(group);

        return donGroupRepo.save(group);
    }
    public Object addDonor(int groupId, int donorId) {

        DonGroupEntity group = donGroupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (group.getDonors().size() >= 6) {
            throw new RuntimeException("Max donors reached");
        }

        DonorEntity donor = donorRepo.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        if (donor.getGroup() != null) {
            throw new RuntimeException("Already assigned");
        }

        if (!donor.getDonationType().equals(DonationType.REPEAT)) {
            throw new RuntimeException("Must be repeat");
        }

        if (!donor.getDonorstatus().equals(volunteerStatus.ACCEPTED)) {
            throw new RuntimeException("Donor must be accepted");
        }

        if (!donor.getBloodType().equals(group.getChild().getBloodType())) {
            throw new RuntimeException("Blood type mismatch");
        }

        List<DonorEntity> donors = group.getDonors();

        int maxOrder = donors.stream()
                .mapToInt(DonorEntity::getDonationOrder)
                .max()
                .orElse(-1);

        donor.setDonationOrder(maxOrder + 1);

        donor.setGroup(group);
        donors.add(donor);

        return donGroupRepo.save(group);
    }
public Object removeDonor(int groupId, int donorId) {

    DonGroupEntity group = donGroupRepo.findById(groupId)
            .orElseThrow(() -> new RuntimeException("Group not found"));

    if (group.getDonors().size() <= 3) {
        throw new RuntimeException("Minimum 3 donors required");
    }

    DonorEntity donor = donorRepo.findById(donorId)
            .orElseThrow(() -> new RuntimeException("Donor not found"));

    group.getDonors().remove(donor);

    donor.setGroup(null);
    donorRepo.save(donor);

    List<DonorEntity> donors = group.getDonors()
            .stream()
            .sorted(Comparator.comparingInt(DonorEntity::getDonationOrder))
            .toList();

    for (int i = 0; i < donors.size(); i++) {
        donors.get(i).setDonationOrder(i);
    }

    return donGroupRepo.save(group);
}
    public List<DonorEntity> getMatchingDonors(int childId) {

        ChildEntity child = childRepo.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child not found"));
                System.out.println("Child blood type: " + child.getBloodType());

        return donorService.getAvailableDropDownDonors(child.getBloodType());
    }

    public Object getCurrentDonors(int groupId) {

        DonGroupEntity group = donGroupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<DonorEntity> donors = group.getDonors()
                .stream()
                .sorted(Comparator.comparingInt(DonorEntity::getDonationOrder))
                .toList();

        int index = group.getOrderIndex();

        List<DonorEntity> result = new ArrayList<>();

        result.add(donors.get(index)); // main

        int backupIndex = index + 3;
        if (backupIndex < donors.size()) {
            result.add(donors.get(backupIndex));
        }

        return result;
    }

    public void rotateIfDue(DonGroupEntity group) {

        LocalDate today = LocalDate.now();
        LocalDate lastDate = group.getLastRotationDate();


        if (lastDate == null) return;

        LocalDate nextRotationDate = lastDate.plusMonths(1);


        if (!nextRotationDate.isAfter(today)) {

            int next = (group.getOrderIndex() + 1) % 3;

            group.setOrderIndex(next);


            group.setLastRotationDate(nextRotationDate);

            donGroupRepo.save(group);
        }
    }
 public void sendEmailToDonor(int donorId) {
        DonorEntity donor = donorRepo.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        String toEmail = donor.getEmail();
        String subject = "Update on Your Donation Application";
        String emailContent;

        if (donor.getDonorstatus() == volunteerStatus.REJECTED) {
            emailContent = "Dear " + donor.getName() +
                    ",\n\nThank you for your willingness to donate." +
                    "\n\n After reviewing your hospital checkup results, " +
                    "we regret to inform you that you are not currently eligible to donate. " +
                    "This decision is made to ensure your safety and the safety of recipients."+
                    "\n\n We truly appreciate your generosity and understanding.";

        } else {
            emailContent = "Dear " + donor.getName() +
                    ",\n\nThank you for your willingness to donate.\n\n" +
                    "We are pleased to inform you that you have been accepted as a donor. " +
                    "Please wait for a call from the LEP, who will provide you with more information about the donation process." +
                    "\n\nWe appreciate your generosity and support. " ;

        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(emailContent);
        message.setFrom("misrshoryanelataa@gmail.com");
        mailSender.send(message);
    }

    public void acceptDonor(int donorId, int lepId) {
        DonorEntity donor = donorRepo.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        LEPEntity lep = lepRepo.findById(lepId)
                .orElseThrow(() -> new RuntimeException("LEP not found"));

        donor.setDonorstatus(volunteerStatus.ACCEPTED);
        donorRepo.save(donor);
        sendEmailToDonor(donorId);
    }

    public void rejectDonor(int donorId, int lepId) {
        DonorEntity donor = donorRepo.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        LEPEntity lep = lepRepo.findById(lepId)
                .orElseThrow(() -> new RuntimeException("LEP not found"));

        donor.setDonorstatus(volunteerStatus.REJECTED);
        donorRepo.save(donor);


        sendEmailToDonor(donorId);
        donorRepo.delete(donor);

    }
    public List<DonorEntity> getAcceptedRepeatDonors() {
        return donorService.getAcceptedRepeatDonors();
    }

    public List<DonorEntity> getAcceptedOneTimeDonors() {
        return donorService.getAcceptedOneTimeDonors();
    }
    public List<DonorEntity> getPendingDonors() {
        return donorService.getPendingDonors();
    }
      
    public List<Object> getDonorsEditable(int lepId){
            LEPEntity lep = lepRepo.findById(lepId)
                .orElseThrow(() -> new RuntimeException("LEP not found"));
            
                LocalDate today = LocalDate.now();
                Date date = java.sql.Date.valueOf(LocalDate.now());
            
             
       try {
        return donorRepo.findAll().stream()
                  .filter(donor -> 
        donor.getDonorstatus() == volunteerStatus.PENDING &&
        donor.getCheckUP() != null &&
        donor.getCheckUP().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().isBefore(today)
).collect(Collectors.toList());
    } catch (Exception e) {
        return new ArrayList<>();
    }}


public List<ChildEntity> getUnassignedChildren(int lepId) {
    return childRepo.findUnassignedChildren(lepId);
}
public List<DonorGroupDTO> getAllGroups(int lepId) {
   List<DonGroupEntity> groups = donGroupRepo.findByLepId(lepId);
    System.out.println("🔎 groups found = " + groups.size());
    return groups.stream().map(g -> {

        DonorGroupDTO dto = new DonorGroupDTO();
              ChildEntity c = g.getChild();

        ChildDTO childDTO = new ChildDTO(
                c.getId(),
                c.getName(),
                c.getAge(),
                c.getBloodType().name(),
                c.getParentNumber()
        );

        List<DonorDTO> donors = g.getDonors().stream().map(d -> {
            DonorDTO dd = new DonorDTO();
            dd.setId(d.getId());
            dd.setEmail(d.getEmail());
            dd.setPhone(d.getPhone());
            dd.setName(d.getName());
            dd.setBloodType(d.getBloodType().toString());
            return dd;
        }).toList();
        dto.setId(g.getId());
        dto.setChild(childDTO);
        dto.setDonors(donors);
        dto.setDonorsCount(donors.size());

        return dto;
    }).toList();

}

}



