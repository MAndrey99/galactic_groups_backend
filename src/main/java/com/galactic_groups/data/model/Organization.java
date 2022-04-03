package com.galactic_groups.data.model;

import com.galactic_groups.data.validation.OnCreate;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Entity
@Table(name = "organization")
@Getter
@ToString
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Null(groups = OnCreate.class)
    private Long id;

    @Column(name = "name", nullable = false)
    @NonNull
    @NotNull
    @Size(min = 3)
    private String orgName;
}
