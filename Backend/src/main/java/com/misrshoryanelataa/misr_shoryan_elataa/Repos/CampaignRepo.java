package com.misrshoryanelataa.misr_shoryan_elataa.Repos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.CampaignEntity;

import jakarta.transaction.Transactional;

@Repository
public interface CampaignRepo extends JpaRepository<CampaignEntity, Integer> {
    @Modifying
@Transactional
@Query("UPDATE CampaignEntity c SET c.pr = null WHERE c.pr.id = :prId")
void detachByPrId(@Param("prId") int prId);
}