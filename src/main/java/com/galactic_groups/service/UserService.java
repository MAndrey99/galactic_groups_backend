package com.galactic_groups.service;

import com.galactic_groups.data.cache.OrganizationCache;
import com.galactic_groups.data.dto.UserInfo;
import com.galactic_groups.data.model.User;
import com.galactic_groups.data.repository.UserRepository;
import com.galactic_groups.data.view.UserRole;
import com.galactic_groups.data.view.UserSecurityView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<UserInfo> getUsersByOrgId(Integer orgId) {
        return userRepository.findByOrganizationId(orgId).stream()
                .map(this::buildUserInfo)
                .toList();
    }

    @Transactional
    public UserInfo createUser(User user) {
        var authenticatedUserInfo = securityService.getAuthenticatedUserView();
        if (user.getOrganizationId() == null && user.getRole() != UserRole.Admin) {
            if (authenticatedUserInfo.getOrganizationId() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to create user without id");
            else
                user.setOrganizationId(authenticatedUserInfo.getOrganizationId());
        }
        securityService.require(securityService.checkAccessToUser(authenticatedUserInfo, user));
        userRepository.save(user);
        var result = buildUserInfo(user);
        log.info("created new user: {}", result);
        return result;
    }

    @Transactional
    public void deleteUserById(long id) {
        var authenticatedUser = (User) securityService.getAuthenticatedUserView();
        if (authenticatedUser.getRole() != UserRole.Admin) {
            if (authenticatedUser.getId() != id && authenticatedUser.getRole() != UserRole.Owner) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            UserSecurityView user = userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));
            securityService.require(securityService.checkAccessToOrganization(authenticatedUser, user.getOrganizationId()));
        }
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }

    private UserInfo buildUserInfo(User user) {
        String orgName = null;
        if (user.getOrganizationId() != null) {
            orgName = organizationCache.getById(user.getOrganizationId()).getOrgName();
        }
        return new UserInfo(user.getId(), user.getFullName(), user.getMail(), orgName, user.getRole().name());
    }
}
