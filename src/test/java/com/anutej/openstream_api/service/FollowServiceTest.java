package com.anutej.openstream_api.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.anutej.openstream_api.entity.Follow;
import com.anutej.openstream_api.entity.User;
import com.anutej.openstream_api.repository.FollowRepository;
import com.anutej.openstream_api.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FollowService followService;

    // test methods

    @Test
    public void followSelfThrows() {
        assertThrows(IllegalArgumentException.class, () -> followService.follow(1L, 1L));
    }

    @Test
    public void followAlreadyFollowingThrows() {

        when(followRepository.existsByFollowerIdAndFollowedId(1L, 2L)).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> followService.follow(1L, 2L));
    }

    @Test
    public void followNonExistentFollowerThrows() {
        assertThrows(IllegalArgumentException.class, () -> followService.follow(1L, 2L));
    }

    @Test
    public void followSavesRelationship() {
        User follower = new User();
        follower.setHandle("anutej");

        User followed = new User();
        followed.setHandle("maya");

        when(userRepository.findById(1L)).thenReturn(Optional.of(follower));
        when(userRepository.findById(2L)).thenReturn(Optional.of(followed));

        followService.follow(1L, 2L);

        verify(followRepository).save(any(Follow.class));
    }

    @Test
    public void unfollowFollowedPersonThrows() {
        assertThrows(IllegalArgumentException.class, () -> followService.unfollow(1L, 2L));
    }

    @Test
    public void unfollowDeletesRelationship() {
        Follow follow = new Follow();

        when(followRepository.findByFollowerIdAndFollowedId(1L, 2L)).thenReturn(Optional.of(follow));

        followService.unfollow(1L, 2L);

        verify(followRepository).delete(follow);
    }
}
