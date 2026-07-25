package com.nhan.sp2.controller;

import com.nhan.sp2.dto.request.UserPasswordRequest;
import com.nhan.sp2.dto.request.UserRequest;
import com.nhan.sp2.dto.response.ResponseData;
import com.nhan.sp2.dto.response.UserResponse;
import com.nhan.sp2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
@Tag(name = "User Controller")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user list" , description = "API retrieve user from db")
    @GetMapping("/list")
    public ResponseData<?> getList(@RequestParam(required = false) String keyword,
                                      @RequestParam(defaultValue = "0") int pageNo,
                                      @RequestParam(defaultValue = "20") int pageSize) {

        return new ResponseData<>(HttpStatus.OK.value(), "user list",userService.getListUser());
    }
    @Operation(summary = "Get user detail" , description = "API retrieve user detail by id")
    @GetMapping("/{userId}")
    public ResponseData<UserResponse> getUserDetail(@PathVariable @Min(1) Long userId) {
        return new ResponseData<>(HttpStatus.OK.value(),"user ",userService.getUser(userId));
    }

    @Operation(summary = "Create user" , description = "API add new user to db")
    @PostMapping("/add")
    public ResponseData<?> addUser(UserRequest userRequest) {
        Long userId = userService.addUser(userRequest);
        return new ResponseData<>(HttpStatus.CREATED.value(),"Add user successfully",userId);
    }
    @Operation(summary = "Update user" , description = "API update user to db")
    @PutMapping("/update/{userId}")
    public ResponseData<?> updateUser(@PathVariable("userId") @Min(1) Long userId,UserRequest userRequest) {
        userService.updateUser(userId,userRequest);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Update user successfully");
    }

    @Operation(summary = "Change password" , description = "API change password user to db")
    @PatchMapping("/{userId}/change-password")
    public ResponseData<?> changePasswordUser(@PathVariable("userId") @Min(1) Long userId, UserPasswordRequest userPasswordRequest) {
        userService.changePasswordUser(userId,userPasswordRequest);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Password update user successfully");
    }

    @Operation(summary = "Delete user" , description = "API delete user to db")
    @DeleteMapping("/delete/{userId}")
    public ResponseData<?> deleteUser(@PathVariable("userId") @Min(1) Long userId) {
        userService.deleteUser(userId);
        return new ResponseData<>(HttpStatus.RESET_CONTENT.value(), "Delete user successfully",userId);
    }
}
