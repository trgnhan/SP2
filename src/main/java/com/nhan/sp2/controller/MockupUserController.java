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

import java.util.*;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/mockup/user")
@Tag(name = "Mockup User Controller")
public class MockupUserController {

    private final UserService userService;

    @Operation(summary = "Get user list" , description = "API retrieve user from db")
    @GetMapping("/list")
    public Map<String,Object> getList(@RequestParam(required = false) String keyword,
                                        @RequestParam(defaultValue = "0") int pageNo,
                                        @RequestParam(defaultValue = "20") int pageSize) {

        UserResponse userResponse1 = new UserResponse(
                1l,
                "list",
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
        List<UserResponse> userResponses = List.of(userResponse1, userResponse2);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status",HttpStatus.OK.value());
        result.put("message","list user");
        result.put("data",userResponses);
        return  result;
    }
    @Operation(summary = "Get user detail" , description = "API retrieve user detail by id")
    @GetMapping("/{userId}")
    public Map<String,Object>  getUserDetail(@PathVariable @Min(1) Long userId) {
        UserResponse userResponse2 = new UserResponse(
                1l,"detail",
                "naldo",
                "Nam",
                new Date(9,9,1999),
                "ronaldo",
                "a7@gmail.com",
                "0001112223");
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status",HttpStatus.OK.value());
        result.put("message","user");
        result.put("data",userResponse2);
        return  result;
    }

    @Operation(summary = "Create user" , description = "API add new user to db")
    @PostMapping("/add")
    public Map<String,Object>  addUser(UserRequest userRequest) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status",HttpStatus.CREATED.value());
        result.put("message","list user");
        result.put("data","");
        return  result;
        }
    @Operation(summary = "Update user" , description = "API update user to db")
    @PutMapping("/update/{userId}")
    public Map<String,Object>  updateUser(@PathVariable("userId") @Min(1) Long userId,UserRequest userRequest) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status",HttpStatus.NO_CONTENT.value());
        result.put("message","list user");
        result.put("data","");
        return  result;
    }

    @Operation(summary = "Change password" , description = "API change password user to db")
    @PatchMapping("/{userId}/change-password")
    public Map<String,Object>  changePasswordUser(@PathVariable("userId") @Min(1) Long userId, UserPasswordRequest userPasswordRequest) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status",HttpStatus.NO_CONTENT.value());
        result.put("message","list user");
        result.put("data","");
        return  result;
    }

    @Operation(summary = "Delete user" , description = "API delete user to db")
    @DeleteMapping("/delete/{userId}")
    public Map<String,Object>  deleteUser(@PathVariable("userId") @Min(1) Long userId) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status",HttpStatus.RESET_CONTENT.value());
        result.put("message","list user");
        result.put("data","");
        return  result;
    }
}
