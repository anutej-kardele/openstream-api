package com.anutej.openstream_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.anutej.openstream_api.dto.response.UserResponse;
import com.anutej.openstream_api.entity.User;
import com.anutej.openstream_api.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // methods

    @Transactional
    public UserResponse createUser(String username, String handle) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (handle == null || handle.isBlank()) {
            throw new IllegalArgumentException("Handle cannot be empty");
        }

        username = username.trim();
        handle = handle.trim().toLowerCase();

        if (!handle.matches("^[a-z0-9_]{3,20}$")) {
            throw new IllegalArgumentException(
                    "Handle must be 3-20 characters long and can only contain letters, numbers, and underscores");
        }

        if (userRepository.existsByHandle(handle)) {
            throw new IllegalArgumentException("Handle already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setHandle(handle);

        var saved = userRepository.save(user);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public UserResponse getByHandle(String handle) {
        if (handle == null || handle.isBlank()) {
            throw new IllegalArgumentException("Handle cannot be empty");
        }

        return userRepository.findByHandle(handle.toLowerCase()).map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> search(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Query cannot be empty");
        }

        return userRepository
                .findByHandleContainingIgnoreCaseOrUsernameContainingIgnoreCase(query.toLowerCase(),
                        query.toLowerCase())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // --- mapping ---

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getHandle());
    }

}
