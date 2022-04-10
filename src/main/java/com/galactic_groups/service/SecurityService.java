package com.galactic_groups.service;

import com.galactic_groups.data.model.User;
import com.galactic_groups.data.view.UserRole;
import com.galactic_groups.data.view.UserSecurityView;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
class SecurityService {

    boolean checkAccessToUser(UserSecurityView user, User target) {
        return user.getRole() == UserRole.Admin
                || (user.getRole() == UserRole.Owner || checkAccessToOrganization(user, target.getOrganizationId()));
    }

    // TODO: потом лучше нормально расписать разные виды доступа (чтение/изменение) и не только к организациям
    boolean checkAccessToOrganization(UserSecurityView user, Integer organizationId) {
        return Objects.equals(user.getOrganizationId(), organizationId);
    }

    void require(boolean hasRequiredAuthority) {
        require(hasRequiredAuthority, null);
    }

    void require(boolean hasRequiredAuthority, String message) {
        if (!hasRequiredAuthority)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
    }

    UserSecurityView getUserWithOrganizationId() {
        var user = getAuthenticatedUserView();
        require(user.getOrganizationId() != null, "The user is not a member of an organization");
        return user;
    }

    UserSecurityView getAuthenticatedUserView() {
        try {
            return (UserSecurityView) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
