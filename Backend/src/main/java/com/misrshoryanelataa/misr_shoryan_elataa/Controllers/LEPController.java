package com.misrshoryanelataa.misr_shoryan_elataa.Controllers;

import com.misrshoryanelataa.misr_shoryan_elataa.DTO.DonorGroupDTO;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.volunteerStatus;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.ChildEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.DonGroupEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.DonorEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Repos.DonGroupRepo;
import com.misrshoryanelataa.misr_shoryan_elataa.Services.LEPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;


@RestController
public class LEPController {
    @Autowired
    private LEPService lepService;

    @Autowired
    private DonGroupRepo donGroupRepo;

    @PostMapping("/child/{lepId}")
    public Object createChild(@RequestBody ChildEntity child, @PathVariable int lepId) {
        try {
            lepService.createChild(child, lepId);
            return ResponseEntity.ok(child);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/child/id/{id}")
    public Object getChildById(@PathVariable int id) {
        return lepService.getChildById(id);
    }
    // @GetMapping("/child/all")
    // public List<ChildEntity> getAllChildren() {
    // return lepService.getAllChildren();
    // }

    @PutMapping("/child/{id}/{lepId}")
    public Object updateChild(@PathVariable int id, @RequestBody ChildEntity child, @PathVariable int lepId) {
        return lepService.updateChild(id, child, lepId);
    }

    @DeleteMapping("/child/{id}")
    public Object deleteChild(@PathVariable int id) {

        return lepService.deleteChild(id);
    }

    @GetMapping("child/{lepId}")
    public List<ChildEntity> getChildren(@PathVariable int lepId) {
        List<ChildEntity> children = lepService.getChildrenForLep(lepId);

        if (children == null || children.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No children found for this LEP");
        }

        return children;
    }

    @PostMapping("donGroup/{childId}")
    public Object createGroup(
            @PathVariable int childId,
            @RequestBody List<Integer> donorIds) {

        return ResponseEntity.ok(
                lepService.createGroup(childId, donorIds));
    }

    @PostMapping("donGroup/{groupId}/add/{donorId}")
    public Object addDonor(
            @PathVariable int groupId,
            @PathVariable int donorId) {

        return ResponseEntity.ok(
                lepService.addDonor(groupId, donorId));
    }

    @PostMapping("donGroup/{groupId}/remove/{donorId}")
    public Object removeDonor(
            @PathVariable int groupId,
            @PathVariable int donorId) {

        return ResponseEntity.ok(
                lepService.removeDonor(groupId, donorId));
    }

    @GetMapping("donGroup/matching-donors/{childId}")
    public ResponseEntity<List<DonorEntity>> getMatchingDonors(@PathVariable int childId) {
        return ResponseEntity.ok(
                lepService.getMatchingDonors(childId));
    }

    @GetMapping("donGroup/getCurrentDonors/{groupId}")
    public Object getCurrentDonors(@PathVariable int groupId) {
        return ResponseEntity.ok(
                lepService.getCurrentDonors(groupId));
    }

    @GetMapping("donGroup/acceptedRepeat-donors")
    public ResponseEntity<List<DonorEntity>> getAcceptedRepeatDonors() {
        return ResponseEntity.ok(
                lepService.getAcceptedRepeatDonors());
    }

    @GetMapping("donGroup/acceptedOneTime-donors")
    public ResponseEntity<List<DonorEntity>> getAcceptedOneTimeDonors() {
        return ResponseEntity.ok(
                lepService.getAcceptedOneTimeDonors());
    }

    @GetMapping("donGroup/pending-donors")
    public ResponseEntity<List<DonorEntity>> getPendingDonors() {
        return ResponseEntity.ok(
                lepService.getPendingDonors());
    }

    // for testing
    @PostMapping("/{groupId}/rotate-if-due")
    public ResponseEntity<?> rotateIfDue(@PathVariable int groupId) {

        DonGroupEntity group = donGroupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        lepService.rotateIfDue(group);

        return ResponseEntity.ok("Checked rotation for group");
    }

    @PostMapping("/send-email-donor")
    public void sendEmailToDonor(
            @RequestParam int donorId) {
        lepService.sendEmailToDonor(donorId);
    }

    @GetMapping("/donors/editable/{lepId}")
    public List<Object> getDonorsEditable(@PathVariable int lepId) {
        return lepService.getDonorsEditable(lepId);
    }

    @PostMapping("/accept-donor/{donorId}/{lepId}")
    public ResponseEntity<String> acceptVolunteer(@PathVariable int donorId, @PathVariable int lepId) {
        try {
            lepService.acceptDonor(donorId, lepId);
            return ResponseEntity.ok("Donor accepted successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());
        }

    }

    @PostMapping("/reject-donor/{donorId}/{lepId}")
    public void rejectDonor(@PathVariable int donorId, @PathVariable int lepId) {
        lepService.rejectDonor(donorId, lepId);
    }

    @GetMapping("/child/unassigned/{lepId}")
public ResponseEntity<List<ChildEntity>> getUnassignedChildren(@PathVariable int lepId) {
    return ResponseEntity.ok(
            lepService.getUnassignedChildren(lepId)
    );
}

@GetMapping("/donGroup/all/{lepId}")
public ResponseEntity<List<DonorGroupDTO>> getAllGroups(@PathVariable int lepId) {
    return ResponseEntity.ok(lepService.getAllGroups(lepId));
}
@GetMapping("/status")
public List<volunteerStatus> getDonorStatus() {
    return Arrays.asList(volunteerStatus.values());
}
}
