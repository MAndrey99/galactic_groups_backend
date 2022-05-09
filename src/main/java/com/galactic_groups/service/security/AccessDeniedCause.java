package com.galactic_groups.service.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccessDeniedCause {
    DIFFERENT_ORGANIZATIONS("Different organizations"),
    EMPLOYEE_EDITING_ORGANIZATION("Employee cannot get access to organization for editing"),
    EMPLOYEE_ACCESS_OTHER_USERS("Employee cannot get access to other users"),
    ADMIN_EDITING_STUDENTS("Admin cannot edit students, only organizations and users"),
    OWNER_ACCESS_OTHER_OWNERS("Owner cannot get access to other owners"),
    OWNER_ACCESS_ADMINS("Owner cannot get access to admins");

    private final String description;
}
