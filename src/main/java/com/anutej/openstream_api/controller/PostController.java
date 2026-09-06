package com.anutej.openstream_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anutej.openstream_api.dto.request.CreatePostRequest;
import com.anutej.openstream_api.dto.response.PostResponse;
import com.anutej.openstream_api.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public PostResponse create(@RequestBody CreatePostRequest request) {
        return postService.createPost(request.authorId(), request.content());
    }

    @GetMapping("/feed/{userId}")
    public List<PostResponse> feed(@PathVariable Long userId) {
        return postService.getFeed(userId);
    }
}
