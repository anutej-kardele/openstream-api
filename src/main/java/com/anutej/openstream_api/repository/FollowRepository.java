package com.anutej.openstream_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anutej.openstream_api.entity.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);

    Optional<Follow> findByFollowerIdAndFollowedId(Long followerId, Long followedId);

    List<Follow> findByFollowerId(Long followerId);

    List<Follow> findByFollowedId(Long followedId);

    long countByFollowerId(Long followerId);

    long countByFollowedId(Long followedId);
}
