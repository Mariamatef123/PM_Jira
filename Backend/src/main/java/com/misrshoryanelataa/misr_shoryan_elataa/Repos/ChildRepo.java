package com.misrshoryanelataa.misr_shoryan_elataa.Repos;

import com.misrshoryanelataa.misr_shoryan_elataa.Models.ChildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChildRepo extends JpaRepository <ChildEntity,Integer> {
    List<ChildEntity> findByLepId(int lepId);
@Query("""
SELECT c
FROM ChildEntity c
WHERE c.lep.id = :lepId
AND NOT EXISTS (
    SELECT g
    FROM DonGroupEntity g
    WHERE g.child.id = c.id
)
""")
List<ChildEntity> findUnassignedChildren(@Param("lepId") int lepId);

}
