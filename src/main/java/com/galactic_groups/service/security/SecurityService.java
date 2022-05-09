package com.galactic_groups.service.security;

import com.galactic_groups.data.view.UserSecurityView;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityService {
    private final PasswordEncoder passwordEncoder;

    public String encodePassword(@NonNull String password) {
        return passwordEncoder.encode(password);
    }

    public void require(@NonNull Function<PersonalAccessManager, AccessCheckResult> checker) {
        require(checker.apply(getAccessManager()));
    }

    public void require(@NonNull AccessCheckResult checkResult) {
        require(checkResult.isAllowed(), checkResult.getDescription());
    }

    private static void require(boolean hasRequiredAuthority, String message) {
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
            log.error(e.toString());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
