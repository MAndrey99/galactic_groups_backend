package com.galactic_groups.data.dto;

import com.galactic_groups.data.view.OrganizationSecurityView;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrganizationSecurityData implements OrganizationSecurityView {
    private final Integer id;
}
