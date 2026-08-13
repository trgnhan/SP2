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
@Entity(name = "Permission")
@Table(name = "tbl_permission")
public class Permission extends AbstractEntity<Integer> implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "permission")
    @Builder.Default
    private Set<RoleHasPermission> permissions = new HashSet<>();
}
