package com.nhan.sp2.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "GroupHasUser")
@Table(name = "tbl_user_has_group")
public class GroupHasUser extends AbstractEntity<Integer> implements Serializable {

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
