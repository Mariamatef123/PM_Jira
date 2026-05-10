package com.misrshoryanelataa.misr_shoryan_elataa.Repos;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.InterviewSlotEntity;

import jakarta.transaction.Transactional;

@Repository
public interface InterviewSlotsRepo extends JpaRepository<InterviewSlotEntity, Integer> {
    @Modifying
@Transactional
@Query(value = "UPDATE interview_slot_entity SET volunteer_id = NULL WHERE volunteer_id = :volunteerId", nativeQuery = true)
void clearVolunteerReference(@Param("volunteerId") int volunteerId);
@Modifying
@Query("UPDATE InterviewSlotEntity s SET s.volunteer = null WHERE s.volunteer.hr.id = :hrId")
void detachSlotsFromHr(@Param("hrId") int hrId);
    List<InterviewSlotEntity> findByVolunteer_Hr_Id(int staffId);




@Modifying
@Transactional
@Query(value = "UPDATE interview_slot_entity SET volunteer_id = NULL WHERE volunteer_id = :volunteerId", nativeQuery = true)
void detachVolunteerFromSlot(@Param("volunteerId") int volunteerId);
}