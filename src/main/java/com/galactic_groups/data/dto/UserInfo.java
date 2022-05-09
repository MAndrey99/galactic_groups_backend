package com.galactic_groups.data.dto;

import lombok.Builder;

@Builder
public record UserInfo(
        Long id,
        String fullName,
        String mail,
        String orgName,
        String role
) {}
