package com.misrshoryanelataa.misr_shoryan_elataa.Repos;

import com.misrshoryanelataa.misr_shoryan_elataa.Models.DonGroupEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DonGroupRepo extends JpaRepository <DonGroupEntity, Integer> {

    List<DonGroupEntity> findByLepId(int lepId);
}
