package com.galactic_groups.service;

import com.galactic_groups.data.dto.OrganizationInfo;
import com.galactic_groups.data.dto.OrganizationSecurityData;
import com.galactic_groups.data.model.Organization;
import com.galactic_groups.data.repository.OrganizationRepository;
import com.galactic_groups.service.security.AccessMode;
import com.galactic_groups.service.security.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final SecurityService securityService;
    private final UserService userService;

    @Transactional
    public Organization createOrganization(Organization org) {
        securityService.require(accessManager ->
                accessManager.checkAccessTo(org, AccessMode.WRITE));
        var created = organizationRepository.save(org);
        log.info("created new organization {}", org);
        return created;
    }

    @Transactional
    @CacheEvict(value = "organizationCache", key = "#id")
    public void deleteOrganization(int id) {
        securityService.require(accessManager ->
                accessManager.checkAccessTo(new OrganizationSecurityData(id), AccessMode.WRITE));
        try {
            organizationRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@accessManager.checkAccessTo(#id, 'READ')")
    @Cacheable(value = "organizationCache", key = "#id")
    public OrganizationInfo getOrganizationInfo(int id) {
        securityService.require(accessManager ->
                accessManager.checkAccessTo(new OrganizationSecurityData(id), AccessMode.READ));
        var organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var orgUsers = userService.getUsersByOrgId(organization.getId());
        return new OrganizationInfo(organization.getOrgName(), orgUsers);
    }
}
