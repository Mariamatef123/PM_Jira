package com.misrshoryanelataa.misr_shoryan_elataa.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.misrshoryanelataa.misr_shoryan_elataa.Models.CampaignEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.PREntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Services.PRService;

@RestController
public class PRController {

    @Autowired
    private PRService PRService;

    @PostMapping("/campaigns")
    public PREntity createCampaign(@RequestBody CampaignEntity campaign,@RequestParam int prId) {
        return PRService.createCampaign(campaign, prId);
    }

    @GetMapping("/campaigns")
    public List<CampaignEntity> getAllCampaigns() {
        return PRService.getAllCampaigns();
    }

    @DeleteMapping("/campaigns/{id}")
    public void deleteCampaign(@PathVariable int id) {
        PRService.deleteCampaign(id);
    }

    @PutMapping("/campaigns/{id}")
    public void updateCampaign(@PathVariable int id, @RequestBody CampaignEntity campaign) {
        PRService.updateCampaign(id, campaign);
    }

    @PostMapping("send-email-to-users")
    public void sendEmailToUsers(@RequestBody CampaignEntity campaign,String state) {
        PRService.sendEmailToUsers(campaign,state);
    }

    @GetMapping("/campaigns/{id}")
    public CampaignEntity getCampaignById(@PathVariable int id) {
        return PRService.getCampaignById(id);
    }
}