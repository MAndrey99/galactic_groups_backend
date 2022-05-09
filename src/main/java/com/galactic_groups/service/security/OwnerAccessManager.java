package com.galactic_groups.service.security;

import com.galactic_groups.data.model.Student;
import com.galactic_groups.data.view.OrganizationSecurityView;
import com.galactic_groups.data.view.UserRole;
import com.galactic_groups.data.view.UserSecurityView;
import lombok.NonNull;

import java.util.Objects;

public class OwnerAccessManager implements PersonalAccessManager {
    private final UserSecurityView ownerUser;

    public OwnerAccessManager(@NonNull UserSecurityView ownerUser) {
        if (ownerUser.getRole() != UserRole.Owner) {
            throw new IllegalArgumentException("OwnerAccessManager only works with owner users");
        }
        this.ownerUser = ownerUser;
    }

    @Override
    public AccessCheckResult checkAccessTo(@NonNull OrganizationSecurityView organization, @NonNull AccessMode accessMode) {
        if (Objects.equals(ownerUser.getOrganizationId(), organization.getId()))
            return AccessCheckResult.accessGranted();
        return AccessCheckResult.accessDenied(AccessDeniedCause.DIFFERENT_ORGANIZATIONS);
    }

    @Override
    public AccessCheckResult checkAccessTo(@NonNull UserSecurityView otherUser, @NonNull AccessMode accessMode) {
        return switch (otherUser.getRole()) {
            case Owner -> {
                if (Objects.equals(ownerUser.getId(), otherUser.getId()))
                    yield AccessCheckResult.accessGranted();
                yield AccessCheckResult.accessDenied(AccessDeniedCause.OWNER_ACCESS_OTHER_OWNERS);
            }
            case Admin -> AccessCheckResult.accessDenied(AccessDeniedCause.OWNER_ACCESS_ADMINS);
            case Employee -> {
                if (Objects.equals(ownerUser.getOrganizationId(), otherUser.getOrganizationId()))
                    yield AccessCheckResult.accessGranted();
                yield AccessCheckResult.accessDenied(AccessDeniedCause.DIFFERENT_ORGANIZATIONS);
            }
        };
    }

    @Override
    public AccessCheckResult checkAccessTo(@NonNull Student student, @NonNull AccessMode accessMode) {
        if (Objects.equals(ownerUser.getOrganizationId(), student.getOrganizationId()))
            return AccessCheckResult.accessGranted();
        return AccessCheckResult.accessDenied(AccessDeniedCause.DIFFERENT_ORGANIZATIONS);
    }
}
