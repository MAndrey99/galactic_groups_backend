package com.galactic_groups.service.security;

import com.galactic_groups.data.view.UserSecurityView;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Function;

@Service
public class SecurityService {

    public void require(@NonNull Function<PersonalAccessManager, AccessCheckResult> checker) {
        require(checker.apply(getAccessManager()));
    }

    public void require(@NonNull AccessCheckResult checkResult) {
        require(checkResult.isAllowed(), checkResult.getDescription());
    }

    private void require(boolean hasRequiredAuthority, String message) {
        if (!hasRequiredAuthority)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
    }

    public PersonalAccessManager getAccessManager() {
        var user = getAuthenticatedUserView();
        return switch (user.getRole()) {
            case Admin -> new AdminAccessManager(user);
            case Owner -> new OwnerAccessManager(user);
            case Employee -> new EmployeeAccessManager(user);
        };
    }

    public UserSecurityView getAuthenticatedUserView() {
        try {
            return (UserSecurityView) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
