package com.anutej.openstream_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.anutej.openstream_api.entity.Post;
import com.anutej.openstream_api.entity.User;
import com.anutej.openstream_api.repository.PostRepository;
import com.anutej.openstream_api.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    // test methods

    @Test
    public void createPostWithEmptyContentThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> postService.createPost(1L, ""));
    }

    @Test
    public void createPostWithValidContentSavesPost() {
        User user = new User();
        user.setUsername("Anutej");
        user.setHandle("anutej");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> inv.getArgument(0));

        // act
        Post result = postService.createPost(1L, "Hello world");

        // assert
        assertEquals("Hello world", result.getContent());
        assertEquals(user, result.getAuthor());
    }

    @Test
    public void findFeedForUser() {
        User user = new User();
        user.setUsername("Anutej");
        user.setHandle("anutej");

        User user1 = new User();
        user1.setUsername("Player2");
        user1.setHandle("player2");

        Post post1 = new Post();
        post1.setAuthor(user);
        post1.setContent("Post 1");

        Post post2 = new Post();
        post2.setAuthor(user1);
        post2.setContent("Post 2");

        Post post3 = new Post();
        post3.setAuthor(user);
        post3.setContent("Post 3");

        when(userRepository.existsById(1L)).thenReturn(true);
        when(postRepository.findFeedForUser(1L)).thenReturn(List.of(post1, post2, post3));

        List<Post> feed = postService.getFeed(1L);

        assertEquals(3, feed.size());
        assertEquals("Post 1", feed.get(0).getContent());
        assertEquals("Post 2", feed.get(1).getContent());
        assertEquals("player2", feed.get(1).getAuthor().getHandle());
        assertEquals("Post 3", feed.get(2).getContent());
    }
}
