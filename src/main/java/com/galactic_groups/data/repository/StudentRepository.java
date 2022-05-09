package com.galactic_groups.data.repository;

import com.galactic_groups.data.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    // TODO: как-нибудь потом добавить индексы (group, organizationId)

    List<Student> findAllByGroupName(String group);

    @Query("select distinct groupName from Student where organizationId = :organizationId")
    List<String> findAllGroups(int organizationId);
}
