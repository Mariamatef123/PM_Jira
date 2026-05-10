package com.misrshoryanelataa.misr_shoryan_elataa.Repos;

import com.misrshoryanelataa.misr_shoryan_elataa.Models.LEPEntity;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LEPRepo extends JpaRepository<LEPEntity, Integer> {
    @Modifying
@Transactional
@Query(value = "INSERT INTO lepentity (id) VALUES (:id)", nativeQuery = true)
void insertLepRow(@Param("id") int id);

@Modifying
@Transactional
@Query(value = "DELETE FROM lepentity WHERE id = :id", nativeQuery = true)
void deleteById(@Param("id") int id);
}
