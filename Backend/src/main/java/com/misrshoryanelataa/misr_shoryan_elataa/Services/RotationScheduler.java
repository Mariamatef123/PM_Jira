package com.misrshoryanelataa.misr_shoryan_elataa.Services;

import com.misrshoryanelataa.misr_shoryan_elataa.Models.DonGroupEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Repos.DonGroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RotationScheduler {

    @Autowired
    private DonGroupRepo groupRepo;

    @Autowired
    private LEPService lepService;


    @Scheduled(cron = "0 0 0 * * ?") // every day
    // @Scheduled(cron = "0 * * * * ?")//every min
    public void checkRotation() {

        List<DonGroupEntity> groups = groupRepo.findAll();

        for (DonGroupEntity group : groups) {
            lepService.rotateIfDue(group);
            System.out.println(group.getOrderIndex());
        }
    }
}

