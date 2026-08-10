package com.nhan.sp2.service;

import com.nhan.sp2.dto.request.UserPasswordRequest;
import com.nhan.sp2.dto.request.UserRequest;
import com.nhan.sp2.dto.response.PageResponse;
import com.nhan.sp2.dto.response.UserResponse;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface UserService {
    UserResponse getUser(Long userId);

    PageResponse<?> getListUser(String keyword, String sort, int pageNo, int pageSize);

    long addUser(UserRequest userRequest);

    void updateUser(UserRequest userRequest);

    void deleteUser(@Min(1) Long userId);

    void changePasswordUser(UserPasswordRequest userPasswordRequest);
}
