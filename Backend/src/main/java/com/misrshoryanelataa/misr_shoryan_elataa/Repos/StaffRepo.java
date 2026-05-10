package com.misrshoryanelataa.misr_shoryan_elataa.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.StaffEntity;

import jakarta.transaction.Transactional;

@Repository
public interface StaffRepo extends JpaRepository<StaffEntity, Integer> {

    boolean existsByEmail(String email);
    boolean existsByOfficialEmail(String OfficialEmail);
@Modifying
@Transactional
@Query(value = "INSERT INTO staff_entity (id, role, official_email) VALUES (:id, :role, :officialEmail)", nativeQuery = true)
void insertStaffRow(@Param("id") int id, @Param("role") String role, @Param("officialEmail") String officialEmail);
    StaffEntity findByOfficialEmail(String officialEmail);
    StaffEntity findByEmail(String email);

    @Modifying
@Query("DELETE FROM StaffEntity s WHERE s.id = :id")
void deleteStaffById(@Param("id") int id);

@Modifying
@Transactional
@Query(value = "DELETE FROM staff_entity WHERE id = :id", nativeQuery = true)
void deleteById(@Param("id") int id);
}