package com.galactic_groups.data.dto;

import com.galactic_groups.data.view.UserRole;
import com.galactic_groups.data.view.UserSecurityView;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class NewUserData implements UserSecurityView {

    @NotNull
    @Size(min = 2, max = 64)
    private String fullName;

    @NotNull
    @Length(min = 4)
    private String password;

    @NotNull
    @Email
    private String mail;

    @NotNull
    private Integer organizationId;

    @NotNull
    private UserRole role;

    @Override
    public Long getId() {
        return null;
    }
}
