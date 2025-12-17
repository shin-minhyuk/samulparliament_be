package com.samulparliament_be.domain.users.controller;

import com.samulparliament_be.domain.users.dto.UserResponse;
import com.samulparliament_be.domain.users.entity.User;
import com.samulparliament_be.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
}
