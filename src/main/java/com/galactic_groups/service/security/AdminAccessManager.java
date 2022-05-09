package com.galactic_groups.service.security;

import com.galactic_groups.data.model.Student;
import com.galactic_groups.data.view.OrganizationSecurityView;
import com.galactic_groups.data.view.UserRole;
import com.galactic_groups.data.view.UserSecurityView;
import lombok.NonNull;

public class AdminAccessManager implements PersonalAccessManager {

    public AdminAccessManager(@NonNull UserSecurityView adminUser) {
        if (adminUser.getRole() != UserRole.Admin) {
            throw new IllegalArgumentException("AdminAccessManager only works with admin users");
        }
    }

    @Override
    public AccessCheckResult checkAccessTo(@NonNull OrganizationSecurityView organization, @NonNull AccessMode accessMode) {
        return AccessCheckResult.accessGranted();
    }

    @Override
    public AccessCheckResult checkAccessTo(@NonNull UserSecurityView otherUser, @NonNull AccessMode accessMode) {
        return AccessCheckResult.accessGranted();
    }

    @Override
    public AccessCheckResult checkAccessTo(@NonNull Student student, @NonNull AccessMode accessMode) {
        return switch (accessMode) {
            case READ -> AccessCheckResult.accessGranted();
            case WRITE -> AccessCheckResult.accessDenied(AccessDeniedCause.ADMIN_EDITING_STUDENTS);
        };
    }
}
