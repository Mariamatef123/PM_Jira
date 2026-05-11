package com.misrshoryanelataa.misr_shoryan_elataa.Services;

import com.misrshoryanelataa.misr_shoryan_elataa.Enums.BloodType;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.DonationType;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.volunteerStatus;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.DonorEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Repos.DonorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonorService {
    @Autowired
    private DonorRepo donorRepo;

    public Object createDonor(DonorEntity donor) {
        return donorRepo.save(donor);
    }
    public Object getDonorById(int id) {
        try {
            return donorRepo.findById(id).orElse(null);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public List<DonorEntity> getAvailableDropDownDonors(BloodType bloodType) {
        return donorRepo.findByBloodTypeAndDonationTypeAndDonorStatusAndGroupIsNull(bloodType, DonationType.REPEAT, volunteerStatus.ACCEPTED);
    }

    public List<DonorEntity> getAcceptedRepeatDonors() {
        return donorRepo.findByDonationTypeAndDonorStatus(DonationType.REPEAT, volunteerStatus.ACCEPTED);
    }

    public List<DonorEntity> getAcceptedOneTimeDonors() {
        return donorRepo.findByDonationTypeAndDonorStatus(DonationType.ONE_TIME, volunteerStatus.ACCEPTED);
    }

    public List<DonorEntity> getPendingDonors() {
        return donorRepo.findByDonorStatus(volunteerStatus.PENDING);
    }

public Object updateDonor(int id, DonorEntity newDonorData) {
    try {
        DonorEntity existing = donorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        // update only allowed fields
        if (newDonorData.getName() != null)
            existing.setName(newDonorData.getName());

        if (newDonorData.getEmail() != null)
            existing.setEmail(newDonorData.getEmail());

        if (newDonorData.getBloodType() != null)
            existing.setBloodType(newDonorData.getBloodType());

        if (newDonorData.getDonationType() != null)
            existing.setDonationType(newDonorData.getDonationType());

        if (newDonorData.getDonorstatus() != null)
            existing.setDonorstatus(newDonorData.getDonorstatus());

        return donorRepo.save(existing);

    } catch (Exception e) {
        return e.getMessage();
    }
}

public Object updateDonorStatus(int id, DonationType type) {

    DonorEntity donor = donorRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Donor not found"));

    donor.setDonationType(type);

    return donorRepo.save(donor);
}

public Object deleteDonor(int id) {
        try {
            if (donorRepo.existsById(id)) {
                donorRepo.deleteById(id);
                return "Donor deleted successfully";
            } else {
                return "Donor not found";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public Object getDonorTypes() {
       return List.of(DonationType.values());
    }
    public Object getBloodTypes() {
        return List.of(BloodType.values());
    }

    
}
