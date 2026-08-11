package com.nhan.sp2.service;

import com.nhan.sp2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public record UserServiceDetail(UserRepository userRepository) {
    // record in java 17

    public UserDetailsService UserServiceDetail() {
        return userRepository::findByUsername;
    }

}
