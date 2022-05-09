package com.galactic_groups.data.model;

import com.galactic_groups.data.validation.OnCreate;
import com.galactic_groups.data.view.OrganizationSecurityView;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Entity
@Table(name = "organization")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Organization implements OrganizationSecurityView {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organization_seq_gen")
    @SequenceGenerator(name = "organization_seq_gen", sequenceName = "organization_id_seq", allocationSize = 1)
    @Null(groups = OnCreate.class)
    private Integer id;

    @Column(name = "org_name", nullable = false)
    @NonNull
    @NotNull
    @Size(min = 3)
    private String orgName;
}
