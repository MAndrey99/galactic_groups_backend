package com.galactic_groups.data.repository;

import com.galactic_groups.data.model.Organization;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

    @Override
    @Cacheable(value = "organizations", key = "#id")
    Optional<Organization> findById(Integer id);

    @Override
    @CachePut(value = "organizations", key = "#entity.id")
    <S extends Organization> S save(S entity);

    @Override
    @CacheEvict(value = "organizations", key = "#id")
    void deleteById(Integer id);
}
