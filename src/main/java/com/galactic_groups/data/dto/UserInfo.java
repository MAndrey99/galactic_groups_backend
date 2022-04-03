package com.galactic_groups.data.dto;

public record UserInfo(
        Long id,
        String fullName,
        String mail,
        String orgName,
        String role
) {}
