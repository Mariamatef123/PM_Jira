package com.misrshoryanelataa.misr_shoryan_elataa.Repos;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.misrshoryanelataa.misr_shoryan_elataa.Enums.volunteerStatus;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.InterviewSlotEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.VolunteerEntity;

import jakarta.transaction.Transactional;

@Repository
public interface VolunteerRepo extends JpaRepository<VolunteerEntity, Integer> {
      boolean existsByEmail(String email);

      boolean existsByUniversityEmail(String universityEmail);
@Modifying
@Transactional
@Query("DELETE FROM VolunteerEntity v WHERE v.hr.id = :hrId")
void deleteByHrId(@Param("hrId") int hrId);

@Query("SELECT v FROM VolunteerEntity v WHERE v.hr IS NULL OR TYPE(v.hr) = HREntity")
List<VolunteerEntity> findAllVolunteers();

@Query("SELECT v FROM VolunteerEntity v WHERE (v.hr IS NULL OR TYPE(v.hr) = HREntity) AND v.status = :status AND v.hr IS NULL")
List<VolunteerEntity> findPendingWithNoHR(@Param("status") volunteerStatus status);

List<VolunteerEntity> findByInterviewSlot(InterviewSlotEntity slot);

List<VolunteerEntity> findByStatus(volunteerStatus pending);

List<VolunteerEntity> findByHrIdAndStatus(int hrId, volunteerStatus pending);

List<VolunteerEntity> findByStatusAndHrIsNull(volunteerStatus pending);

List<VolunteerEntity> findByHrId(int id);



@Transactional
@Query("UPDATE VolunteerEntity v SET v.hr = null WHERE v.hr.id = :hrId")
void detachVolunteersFromHr(@Param("hrId") int hrId);


@Modifying
@Transactional
@Query(value = "DELETE FROM volunteer_entity WHERE id = :id", nativeQuery = true)
void deleteVolunteerRowById(@Param("id") int id);



@Query(value = "SELECT COUNT(*) FROM volunteer_entity WHERE id = :id", nativeQuery = true)
int existsVolunteerRowById(@Param("id") int id);
}