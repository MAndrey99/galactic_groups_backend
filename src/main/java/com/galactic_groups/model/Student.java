package com.galactic_groups.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "student")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "full_name", nullable = false)
    @NonNull
    private String fullName;

    @Column(name = "group_name", nullable = false)
    @NonNull
    private String groupName;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phone;
}
