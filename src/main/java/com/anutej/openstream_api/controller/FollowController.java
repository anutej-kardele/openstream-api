package com.anutej.openstream_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anutej.openstream_api.dto.response.FollowCountsResponse;
import com.anutej.openstream_api.dto.response.UserResponse;
import com.anutej.openstream_api.service.FollowService;

@RestController
@RequestMapping("/api/users")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{followerId}/follow/{followedId}")
    public void follow(@PathVariable Long followerId, @PathVariable Long followedId) {
        followService.follow(followerId, followedId);
    }

    @DeleteMapping("/{followerId}/follow/{followedId}")
    public void unfollow(@PathVariable Long followerId, @PathVariable Long followedId) {
        followService.unfollow(followerId, followedId);
    }

    @GetMapping("/{userId}/following")
    public List<UserResponse> following(@PathVariable Long userId) {
        return followService.getFollowing(userId);
    }

    @GetMapping("/{userId}/followers")
    public List<UserResponse> followers(@PathVariable Long userId) {
        return followService.getFollowers(userId);
    }

    @GetMapping("/{userId}/follow-counts")
    public FollowCountsResponse counts(@PathVariable Long userId) {
        return followService.getCounts(userId);
    }
}