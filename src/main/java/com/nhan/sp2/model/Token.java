package com.nhan.sp2.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Token")
@Table(name = "tbl_token")
public class Token extends AbstractEntity<Integer> implements Serializable {

    @Column(name = "username", length = 255)
    private String username;

    // Sử dụng columnDefinition là "TEXT" như trong db
    @Column(name = "access_token", columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;
}