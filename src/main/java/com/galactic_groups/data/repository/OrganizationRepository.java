package com.galactic_groups.data.repository;

import com.galactic_groups.data.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {}
