package com.anutej.openstream_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.anutej.openstream_api.dto.response.PostResponse;
import com.anutej.openstream_api.entity.Post;
import com.anutej.openstream_api.repository.PostRepository;
import com.anutej.openstream_api.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // methods

    @Transactional
    public PostResponse createPost(Long authorId, String content) {

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Post content cannot be empty");
        }
        content = content.trim();
        if (content.length() > 280) {
            throw new IllegalArgumentException("Post content cannot exceed 280 characters");
        }

        // Check if the user exists
        var user = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Create a new post
        var post = new Post();
        post.setAuthor(user);
        post.setContent(content);

        // Save the post
        var saved = postRepository.save(post);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getFeed(Long userId) {
        // Check if the user exists
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        // Fetch the feed for the user
        return postRepository.findFeedForUser(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    // --- mapping ---

    private PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getCreatedAt(),
                post.getAuthor().getUsername(),
                post.getAuthor().getHandle());
    }
}
