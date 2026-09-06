package com.anutej.openstream_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.anutej.openstream_api.entity.User;
import com.anutej.openstream_api.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // test methods

    @Test
    public void createUserWithDuplicateHandleThrows() {

        // Add the user to the mock repository
        when(userRepository.existsByHandle("anutej")).thenReturn(true);

        // Check if the handle already exists
        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser("Anutej Kardele", "anutej"));
    }

    @Test
    public void handleGetsLowerCased() {

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.createUser("Anutej Kardele", "Anutej");

        assertEquals("anutej", result.getHandle());
    }

    @Test
    public void invalidHandleFormatThrows() {

        assertThrows(IllegalArgumentException.class, () -> userService.createUser("Anutej Kardele", "anutej kardele"));
        assertThrows(IllegalArgumentException.class, () -> userService.createUser("Anutej Kardele", "anutej!kardele"));
        assertThrows(IllegalArgumentException.class, () -> userService.createUser("Anutej Kardele",
                "anutejkardeleanutejkardeleanutejkardeleanutejkardeleanutejkardeleanutejkardele"));
    }

    @Test
    public void blankUserNameThrows() {

        assertThrows(IllegalArgumentException.class, () -> userService.createUser("    ", "anutej"));
    }

}
