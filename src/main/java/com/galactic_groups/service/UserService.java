package com.galactic_groups.service;

import com.galactic_groups.data.cache.OrganizationCache;
import com.galactic_groups.data.dto.OrganizationSecurityData;
import com.galactic_groups.data.dto.UserInfo;
import com.galactic_groups.data.model.User;
import com.galactic_groups.data.repository.UserRepository;
import com.galactic_groups.service.security.AccessMode;
import com.galactic_groups.service.security.SecurityService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final SecurityService securityService;
    private final OrganizationCache organizationCache;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfo getCurrentUserInfo() {
        return buildUserInfo((User) securityService.getAuthenticatedUserView());
    }

    @Transactional(readOnly = true)
    public List<UserInfo> getUsersByOrgId(int orgId) {
        var accessManager = securityService.getAccessManager();
        securityService.require(accessManager.checkAccessTo(
                new OrganizationSecurityData(orgId), AccessMode.READ));
        return userRepository.findByOrganizationId(orgId).stream()
                .filter(user -> accessManager.checkAccessTo(user, AccessMode.READ).isAllowed())
                .map(this::buildUserInfo)
                .toList();
    }

    @Transactional
    public UserInfo createUser(@NonNull User user) {
        securityService.require(accessManager ->
                accessManager.checkAccessTo(user, AccessMode.WRITE));
        userRepository.save(user);
        var result = buildUserInfo(user);
        log.info("created new user: {}", result);
        return result;
    }

    @Transactional
    public void deleteUserById(long id) {
        var toDelete = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));
        securityService.require(accessManager ->
                accessManager.checkAccessTo(toDelete, AccessMode.WRITE));
        userRepository.delete(toDelete);
    }

    private UserInfo buildUserInfo(@NonNull User user) {
        String orgName = null;
        if (user.getOrganizationId() != null) {
            orgName = organizationCache.getById(user.getOrganizationId()).getOrgName();
        }
        return UserInfo.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .mail(user.getMail())
                .orgName(orgName)
                .role(user.getRole().name())
                .build();
    }
}
