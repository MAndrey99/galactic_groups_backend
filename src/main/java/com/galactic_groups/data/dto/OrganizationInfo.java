package com.galactic_groups.data.dto;

import java.util.List;

public record OrganizationInfo(String orgName, List<UserInfo> members) {}
