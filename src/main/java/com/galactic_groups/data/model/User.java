package com.galactic_groups.data.model;

import com.galactic_groups.data.validation.OnCreate;
import com.galactic_groups.data.view.UserRole;
import com.galactic_groups.data.view.UserSecurityView;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Entity
@Table(name = "service_user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserSecurityView {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Null(groups = OnCreate.class)
    private Long id;

    @Column(name = "full_name", nullable = false)
    @NonNull
    @NotNull
    @Size(min = 2, max = 64)
    private String fullName;

    @Column(name = "mail", unique = true, nullable = false)
    @NotNull(groups = OnCreate.class)
    @Email
    private String mail;

    @Column(name = "organization_id", nullable = false)
    @Setter
    private Long organizationId;  // TODO: связать бы с Organization, мб через flyway...

    @Column(name = "role_id", nullable = false)
    @Enumerated(value = EnumType.ORDINAL)
    @NonNull
    @NotNull
    private UserRole role;

    @Column(name = "password_hash", nullable = false)
    @NonNull
    @NotNull
    private String password;
}