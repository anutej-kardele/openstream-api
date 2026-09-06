package com.anutej.openstream_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anutej.openstream_api.dto.request.CreateUserRequest;
import com.anutej.openstream_api.dto.response.UserResponse;
import com.anutej.openstream_api.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse create(@RequestBody CreateUserRequest request) {
        return userService.createUser(request.username(), request.handle());
    }

    @GetMapping("/{handle}")
    public UserResponse getByHandle(@PathVariable String handle) {
        return userService.getByHandle(handle);
    }
}