package com.nhan.sp2.controller;

import com.nhan.sp2.dto.request.AddUserRequest;
import com.nhan.sp2.dto.request.UserPasswordRequest;
import com.nhan.sp2.dto.request.UserRequest;
import com.nhan.sp2.dto.response.ResponseData;
import com.nhan.sp2.dto.response.UserResponse;
import com.nhan.sp2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j(topic = "USER-CONTROLLER")
@RequestMapping("/user")
@Tag(name = "User Controller")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user list" , description = "API retrieve user from db")
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ADMIN','OWNER')")
    public ResponseData<?> getList(@RequestParam(required = false) String keyword,
                                      @RequestParam(required = false) String sort,
                                      @RequestParam(defaultValue = "0") int pageNo,
                                      @RequestParam(defaultValue = "20") int pageSize) {

        log.info("Get user list");

        return new ResponseData<>(HttpStatus.OK.value(), "user list",userService.getListUser(keyword, sort, pageNo, pageSize));
    }
    @Operation(summary = "Get user detail" , description = "API retrieve user detail by id")
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','OWNER','USER')")

    public ResponseData<UserResponse> getUserDetail(@PathVariable @Min(value = 1, message = "userId must be equal or greater than 1") Long userId) {
        return new ResponseData<>(HttpStatus.OK.value(),"user ",userService.getUser(userId));
    }

    @Operation(summary = "Create user" , description = "API add new user to db")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMIN','OWNER')")
    public ResponseData<?> addUser(@RequestBody @Valid AddUserRequest userRequest) {
        log.info("Request add user with first name : {}",userRequest.getFirstName());
        Long userId = userService.addUser(userRequest);
        return new ResponseData<>(HttpStatus.CREATED.value(),"Add user successfully",userId);
    }

    @Operation(summary = "Confirm Email" , description = "API check secretCode from db")
    @GetMapping("/confirm-email")
    public void confirmEmail(@RequestParam String secretCode, HttpServletResponse response) throws IOException {
        log.info("Request confirm email with secretCode : {}",secretCode);
        try {
            // TODO check or compare secrectCode from database
        } catch (Exception e){
            log.error("Confirm email was failure!, errorMessage={}",e.getMessage());
        } finally {
            response.sendRedirect("https://www.google.com/");
        }
    }

    @Operation(summary = "Update user" , description = "API update user to db")
    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('ADMIN','OWNER')")
    public ResponseData<?> updateUser(@RequestBody @Valid UserRequest userRequest) {
        log.info("Request update user with user : {}",userRequest);
        userService.updateUser(userRequest);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Update user successfully");
    }

    @Operation(summary = "Change password" , description = "API change password user to db")
    @PatchMapping("/change-password")
    @PreAuthorize("hasAnyAuthority('ADMIN','OWNER')")
    public ResponseData<?> changePasswordUser(@RequestBody @Valid UserPasswordRequest userPasswordRequest) {
        log.info("Request change password user with userId : {}",userPasswordRequest.getId());
        userService.changePasswordUser(userPasswordRequest);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Password update user successfully");
    }

    @Operation(summary = "Delete user" , description = "API delete user to db")
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','OWNER')")
    public ResponseData<?> deleteUser(@PathVariable("userId") @Min(1) Long userId) {
        log.info("Request delete user with userId : {}",userId);
        //userService.deleteUser(userId);
        return new ResponseData<>(HttpStatus.RESET_CONTENT.value(), "Delete user successfully",userId);
    }
}
