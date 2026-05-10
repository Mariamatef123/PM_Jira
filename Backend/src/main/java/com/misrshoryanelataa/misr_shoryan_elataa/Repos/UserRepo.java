package com.misrshoryanelataa.misr_shoryan_elataa.Repos;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.UserEntity;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);

@Modifying(clearAutomatically = true, flushAutomatically = true)
@Query("UPDATE UserEntity u SET u.password = :password WHERE u.id = :id")
int updatePassword(@Param("id") int id,
                    @Param("password") String password);

    List<UserEntity> findAllByEmail(String email);


@Modifying
@Transactional
@Query(value = "DELETE FROM user_entity WHERE id = :id", nativeQuery = true)
void deleteById(@Param("id") int id);
}