package com.misrshoryanelataa.misr_shoryan_elataa.Repos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.InterviewEntity;

@Repository
public interface InterviewRepo extends JpaRepository<InterviewEntity, Integer> {
}