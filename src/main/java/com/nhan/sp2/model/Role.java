package com.nhan.sp2.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Role")
@Table(name = "tbl_role")
public class Role extends AbstractEntity<Integer> implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "role")
    @Builder.Default
    private Set<RoleHasPermission> permissions = new HashSet<>();
}
