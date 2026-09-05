package com.anutej.openstream_api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anutej.openstream_api.entity.Follow;
import com.anutej.openstream_api.entity.User;
import com.anutej.openstream_api.repository.FollowRepository;
import com.anutej.openstream_api.repository.UserRepository;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    // methods

    @Transactional()
    public void follow(Long followerId, Long followedId) {

        if (followerId.equals(followedId)) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }

        if (followRepository.existsByFollowerIdAndFollowedId(followerId, followedId)) {
            throw new IllegalArgumentException("Already following this user");
        }

        // Check if the user exists
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower not found"));

        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new IllegalArgumentException("Followed user not found"));

        // Create the follow relationship
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);
        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(Long followerId, Long followedId) {

        // Delete the follow relationship
        Follow follow = followRepository.findByFollowerIdAndFollowedId(followerId, followedId)
                .orElseThrow(() -> new IllegalArgumentException("Follow relationship not found"));
        followRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public long countFollowing(Long followerId) {
        // Check if the user exists
        if (!userRepository.existsById(followerId)) {
            throw new IllegalArgumentException("User not found");
        }

        return followRepository.countByFollowerId(followerId);
    }

    @Transactional(readOnly = true)
    public long countFollowers(Long followedId) {
        // Check if the user exists
        if (!userRepository.existsById(followedId)) {
            throw new IllegalArgumentException("User not found");
        }

        return followRepository.countByFollowedId(followedId);
    }

}
