package com.nhan.sp2.dto.request;

import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
public class UserRequest implements Serializable {
    private String firstName;
    private String lastName;
    private String gender;
    private Date birthday;
    private String userName;
    private String email;
    private String phone;
}
