package com.misrshoryanelataa.misr_shoryan_elataa.Controllers;

import com.misrshoryanelataa.misr_shoryan_elataa.Enums.DonationType;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.DonorEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Services.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class DonorController {

    @Autowired
    private DonorService donorService;

    @PostMapping("/donor")
    public Object createDonor(@RequestBody DonorEntity donor) {
        try {
            donorService.createDonor(donor);
            return ResponseEntity.ok("donor is created successfully");

        } catch (RuntimeException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());
        }
    }

    @GetMapping("/donor/{id}")
    public Object getDonorById(@PathVariable int id) {
        return donorService.getDonorById(id);
    }

//    @GetMapping("/donor/blood-type/{bloodType}")
//    public List<DonorEntity> getDonorsByBloodType(@PathVariable BloodType bloodType) {
//
//        List<DonorEntity> donors = donorService.findByBloodTypeAndStateAndGroupIsNull(bloodType, DonationType.REPEAT);
//
//        if (donors.isEmpty()) {
//            throw new RuntimeException("No donors found");
//        }
//
//        return donors;
//    }

    @PutMapping("/donor/{id}")
    public Object updateDonor(@PathVariable int id, @RequestBody DonorEntity donor) {
        return donorService.updateDonor(id, donor);
    }

    @DeleteMapping("/donor/{id}")
    public Object deleteDonor(@PathVariable int id) {

        return donorService.deleteDonor(id);
    }

    @PutMapping("update-donor-status/{id}")
    public Object updateDonorStatus(@PathVariable int id, @RequestBody DonationType type) {
     
        
        return donorService.updateDonorStatus(id,type);
    }

   @GetMapping("/donorTypes")
    public Object getDonorTypes() {
        return donorService.getDonorTypes();
    }

    @GetMapping("/BloodTypes")
    public Object getBloodTypes() {
        return donorService.getBloodTypes();
    }
}

