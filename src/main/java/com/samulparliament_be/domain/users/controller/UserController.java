package com.samulparliament_be.domain.users.controller;

import com.samulparliament_be.domain.users.dto.UserResponse;
import com.samulparliament_be.domain.users.entity.User;
import com.samulparliament_be.domain.users.service.UserService;
import com.samulparliament_be.global.auth.details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        User user = userService.getById(id);

        return UserResponse.from(user);
    }

    @GetMapping("/me")
    public UserResponse getMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return UserResponse.from(userDetails.getUser());
    }
}
