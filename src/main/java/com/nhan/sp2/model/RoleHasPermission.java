package com.nhan.sp2.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "RoleHasPermission")
@Table(name = "tbl_role_has_permission")
public class RoleHasPermission extends AbstractEntity<Integer> implements Serializable {

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;
}
