package com.misrshoryanelataa.misr_shoryan_elataa.Repos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.PREntity;

import jakarta.transaction.Transactional;

@Repository
public interface PrRepo extends JpaRepository<PREntity, Integer> {
@Modifying
@Transactional
@Query(value = "INSERT INTO prentity (id, is_admin) VALUES (:id, :isAdmin)", nativeQuery = true)
void insertPrRow(@Param("id") int id, @Param("isAdmin") boolean isAdmin);
    boolean existsByEmail(String email);

    @Modifying
@Transactional
@Query(value = "DELETE FROM prentity WHERE id = :id", nativeQuery = true)
void deleteById(@Param("id") int id);
}