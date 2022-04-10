package com.galactic_groups.service;

import com.galactic_groups.data.cache.OrganizationCache;
import com.galactic_groups.data.dto.OrganizationInfo;
import com.galactic_groups.data.model.Organization;
import com.galactic_groups.data.repository.OrganizationRepository;
import com.galactic_groups.data.view.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final OrganizationCache organizationCache;
    private final SecurityService securityService;
    private final UserService userService;

    @Transactional
    public Organization createOrganization(Organization org) {
        var user = securityService.getAuthenticatedUserView();
        securityService.require(user.getRole() == UserRole.Admin
                && user.getOrganizationId() == null);
        var created = organizationRepository.save(org);
        log.info("created new organization {}", org);
        return created;
    }

    @Transactional
    public void deleteOrganization(int id) {
        var user = securityService.getAuthenticatedUserView();
        securityService.require(user.getRole() == UserRole.Admin
                && (user.getOrganizationId() == null || user.getOrganizationId() == id));
        try {
            organizationRepository.deleteById(id);
            organizationCache.invalidateById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }

    @Transactional(readOnly = true)
    public OrganizationInfo getCurrentOrganizationInfo() {
        var organization = organizationCache.getById(
                securityService.getUserWithOrganizationId().getOrganizationId());
        var orgUsers = userService.getUsersByOrgId(organization.getId());
        return new OrganizationInfo(organization.getOrgName(), orgUsers);
    }
}
