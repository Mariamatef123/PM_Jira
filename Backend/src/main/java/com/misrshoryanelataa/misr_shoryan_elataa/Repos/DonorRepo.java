package com.misrshoryanelataa.misr_shoryan_elataa.Repos;

import com.misrshoryanelataa.misr_shoryan_elataa.Enums.BloodType;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.DonationType;
import com.misrshoryanelataa.misr_shoryan_elataa.Enums.volunteerStatus;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.DonorEntity;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonorRepo extends JpaRepository<DonorEntity, Integer> {
    List<DonorEntity> findByBloodTypeAndDonationTypeAndDonorStatusAndGroupIsNull(
            BloodType bloodType,
            DonationType state,
            volunteerStatus accepted);

    List<DonorEntity> findByDonationTypeAndDonorStatus(
            DonationType state,
            volunteerStatus accepted);
    List<DonorEntity> findByDonorStatus(
            volunteerStatus pending);

            @Modifying
@Transactional
@Query("UPDATE DonorEntity d SET d.group = null WHERE d.group.id = :groupId")
void detachByGroup(@Param("groupId") int groupId);


// @Modifying
// @Query("""
// UPDATE DonorEntity d
// SET d.group = null, d.staff = null
// WHERE d.group.id = :id OR d.staff.id = :id
// """)
// void detachByGroupOrStaff(@Param("id") int id);
}

