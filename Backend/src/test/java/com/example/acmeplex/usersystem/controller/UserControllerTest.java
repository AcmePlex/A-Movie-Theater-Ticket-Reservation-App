package com.example.acmeplex.usersystem.controller;

import com.example.acmeplex.usersystem.dto.UserDTO;
import com.example.acmeplex.usersystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        // Initialize mocks created above
        MockitoAnnotations.openMocks(this);

        // Set up a sample UserDTO
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@example.com");
        // Initialize other fields as necessary
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        // Arrange
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        // Act
        ResponseEntity<UserDTO> response = userController.createUser(userDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDTO, response.getBody());
        verify(userService, times(1)).createUser(userDTO);
    }

    @Test
    void getUserById_ShouldReturnUser() {
        // Arrange
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(userDTO);

        // Act
        ResponseEntity<UserDTO> response = userController.getUserById(userId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDTO, response.getBody());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Arrange
        List<UserDTO> users = Arrays.asList(userDTO);
        when(userService.getAllUsers()).thenReturn(users);

        // Act
        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(users, response.getBody());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        // Arrange
        Long userId = 1L;
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(userId);
        updatedUserDTO.setName("Jane Doe");
        updatedUserDTO.setEmail("jane.doe@example.com");
        // Initialize other fields as necessary

        when(userService.updateUser(eq(userId), any(UserDTO.class))).thenReturn(updatedUserDTO);

        // Act
        ResponseEntity<UserDTO> response = userController.updateUser(userId, updatedUserDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedUserDTO, response.getBody());
        verify(userService, times(1)).updateUser(userId, updatedUserDTO);
    }

    @Test
    void deleteUser_ShouldReturnNoContent() {
        // Arrange
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        // Act
        ResponseEntity<Void> response = userController.deleteUser(userId);

        // Assert
        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void createUser_InvalidInput_ShouldThrowException() {
        // Arrange
        UserDTO invalidUserDTO = new UserDTO();
        // Missing required fields, e.g., name or email

        // Simulate validation failure by throwing ConstraintViolationException
        when(userService.createUser(any(UserDTO.class))).thenThrow(new ConstraintViolationException("Invalid input", null));

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> {
            userController.createUser(invalidUserDTO);
        });

        verify(userService, times(1)).createUser(invalidUserDTO);
    }

    // Additional tests can be added here, such as testing exception handling, etc.
}