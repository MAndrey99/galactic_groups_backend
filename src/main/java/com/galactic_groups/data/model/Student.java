package com.galactic_groups.data.model;

import com.galactic_groups.data.validation.OnCreate;
import com.galactic_groups.data.validation.annotation.PhoneNumber;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Entity
@Table(name = "student")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

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

    @Column(name = "group_name", nullable = false)
    @NonNull
    @NotNull
    @Size(min = 1, max = 64)
    private String groupName;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    @PhoneNumber
    private String phone;

    @Column(name = "organization_id", nullable = false)
    @NotNull
    private Long organizationId;
}
