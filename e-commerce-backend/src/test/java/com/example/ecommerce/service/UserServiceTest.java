package com.example.ecommerce.service;

import com.example.ecommerce.dto.UserRequest;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserRequest userRequest;
    private User user;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest();
        userRequest.setUsername("testuser");
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password");
        userRequest.setFirstName("Test");
        userRequest.setLastName("User");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
    }

    @Test
    void createUser_ShouldCreateUser_WhenUsernameAndEmailAreUnique() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        var created = userService.createUser(userRequest);

        // Then
        assertThat(created).isNotNull();
        assertThat(created.getUsername()).isEqualTo("testuser");
        assertThat(created.getEmail()).isEqualTo("test@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenUsernameAlreadyExists() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // When / Then
        assertThatThrownBy(() -> userService.createUser(userRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("already exists");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailAlreadyExists() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // When / Then
        assertThatThrownBy(() -> userService.createUser(userRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("already exists");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        var result = userService.getUserById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void getUserById_ShouldReturnEmpty_WhenUserDoesNotExist() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        var result = userService.getUserById(999L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Given
        var users = List.of(user, new User());
        when(userRepository.findAll()).thenReturn(users);

        // When
        var result = userService.getAllUsers();

        // Then
        assertThat(result).hasSize(2);
        verify(userRepository).findAll();
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsers() {
        // Given
        when(userRepository.findAll()).thenReturn(List.of());

        // When
        var result = userService.getAllUsers();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        // Given
        doNothing().when(userRepository).deleteById(1L);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).deleteById(1L);
    }
}
