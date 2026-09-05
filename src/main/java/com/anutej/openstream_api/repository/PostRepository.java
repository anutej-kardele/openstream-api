package com.anutej.openstream_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.anutej.openstream_api.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    @Query("""
                SELECT p
            FROM Post
            p
            WHERE

            p.author.id IN (
                SELECT f.followed.id FROM Follow f WHERE f.follower.id = :userId
            )
            OR p.author.id = :userId
            ORDER BY p.createdAt DESC""")
    List<Post> findFeedForUser(@Param("userId") Long userId);

}
