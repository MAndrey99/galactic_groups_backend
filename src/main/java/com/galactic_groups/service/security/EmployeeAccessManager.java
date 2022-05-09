package com.galactic_groups.service.security;

import com.galactic_groups.data.model.Student;
import com.galactic_groups.data.view.OrganizationSecurityView;
import com.galactic_groups.data.view.UserRole;
import com.galactic_groups.data.view.UserSecurityView;
import lombok.NonNull;

import java.util.Objects;

import static com.galactic_groups.service.security.AccessDeniedCause.*;

public class EmployeeAccessManager implements PersonalAccessManager {
    private final UserSecurityView employeeUser;

    public EmployeeAccessManager(@NonNull UserSecurityView employeeUser) {
        if (employeeUser.getRole() != UserRole.Employee)
            throw new IllegalArgumentException("EmployeeAccessManager only works with employee users");
        this.employeeUser = employeeUser;
    }

    @Override
    public AccessCheckResult checkAccessTo(@NonNull OrganizationSecurityView organization, @NonNull AccessMode accessMode) {
        if (accessMode == AccessMode.WRITE)
            return AccessCheckResult.accessDenied(EMPLOYEE_EDITING_ORGANIZATION);
        if (!Objects.equals(organization.getId(), employeeUser.getOrganizationId()))
            return AccessCheckResult.accessDenied(DIFFERENT_ORGANIZATIONS);
        return AccessCheckResult.accessGranted();
    }

    @Override
    public AccessCheckResult checkAccessTo(@NonNull UserSecurityView otherUser, @NonNull AccessMode accessMode) {
        if (Objects.equals(employeeUser.getId(), otherUser.getId()))
            return AccessCheckResult.accessGranted();
        return AccessCheckResult.accessDenied(EMPLOYEE_ACCESS_OTHER_USERS);
    }

    @Override
    public AccessCheckResult checkAccessTo(@NonNull Student student, @NonNull AccessMode accessMode) {
        if (!Objects.equals(student.getOrganizationId(), employeeUser.getOrganizationId()))
            return AccessCheckResult.accessDenied(DIFFERENT_ORGANIZATIONS);
        return AccessCheckResult.accessGranted();
    }
}
