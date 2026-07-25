package com.nhan.sp2.service.impl;

import com.nhan.sp2.dto.request.UserPasswordRequest;
import com.nhan.sp2.dto.request.UserRequest;
import com.nhan.sp2.dto.response.UserResponse;
import com.nhan.sp2.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserResponse getUser(Long userId) {

        return new UserResponse(
                userId,
                "Jack2",
                "Smith","Nam",
                new Date(2,2,2000),
                "Jack",
                "jsmith@gmail.com",
                "0987654372");

    }

    @Override
    public List<UserResponse> getListUser() {
        UserResponse userResponse1 = new UserResponse(
                1l,
                "Jack",
                "Smith","Nam",
                new Date(2,2,2000),
                "Jack",
                "jsmith@gmail.com",
                "0987654372");
        UserResponse userResponse2 = new UserResponse(
                1l,"ro",
                "naldo",
                "Nam",
                new Date(9,9,1999),
                "ronaldo",
                "a7@gmail.com",
                "0001112223");

        return List.of(userResponse1, userResponse2);


    }

    @Override
    public Long addUser(UserRequest userRequest) {
        return 9l;
    }

    @Override
    public void updateUser(Long userId,UserRequest userRequest) {
    }

    @Override
    public Long deleteUser(Long userId) {
        return 555l;
    }

    @Override
    public void changePasswordUser(Long userId, UserPasswordRequest userPasswordRequest) {

    }
}
