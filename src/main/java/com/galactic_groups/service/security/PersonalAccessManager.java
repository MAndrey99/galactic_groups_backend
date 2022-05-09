package com.galactic_groups.service.security;

import com.galactic_groups.data.model.Student;
import com.galactic_groups.data.view.OrganizationSecurityView;
import com.galactic_groups.data.view.UserSecurityView;
import lombok.NonNull;

public interface PersonalAccessManager {

    AccessCheckResult checkAccessTo(@NonNull OrganizationSecurityView organization, @NonNull AccessMode accessMode);

    AccessCheckResult checkAccessTo(@NonNull UserSecurityView otherUser, @NonNull AccessMode accessMode);

    AccessCheckResult checkAccessTo(@NonNull Student student, @NonNull AccessMode accessMode);
}
