package com.nhan.sp2.service;

import com.nhan.sp2.dto.request.UserPasswordRequest;
import com.nhan.sp2.dto.request.UserRequest;
import com.nhan.sp2.dto.response.UserResponse;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface UserService {
    UserResponse getUser(Long userId);

    List<UserResponse> getListUser();

    Long addUser(UserRequest userRequest);

    void updateUser(Long userId,UserRequest userRequest);

    Long deleteUser(@Min(1) Long userId);

    void changePasswordUser(@Min(1) Long userId, UserPasswordRequest userPasswordRequest);
}
