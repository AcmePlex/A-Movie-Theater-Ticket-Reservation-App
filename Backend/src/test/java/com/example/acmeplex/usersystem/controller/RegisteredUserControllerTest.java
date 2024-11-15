package com.example.acmeplex.usersystem.controller;

import com.example.acmeplex.usersystem.controller.RegisteredUserController;
import com.example.acmeplex.usersystem.dto.RegisteredUserDTO;
import com.example.acmeplex.usersystem.service.RegisteredUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisteredUserControllerTest {

    @Mock
    private RegisteredUserService registeredUserService;

    @InjectMocks
    private RegisteredUserController registeredUserController;

    private RegisteredUserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDTO = new RegisteredUserDTO();
        userDTO.setId(1L);
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@example.com");
        // Initialize other fields as necessary
    }

    @Test
    void testCreateRegisteredUser() {
        when(registeredUserService.createRegisteredUser(any(RegisteredUserDTO.class))).thenReturn(userDTO);

        ResponseEntity<RegisteredUserDTO> response = registeredUserController.createRegisteredUser(userDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(userDTO.getId(), response.getBody().getId());
        verify(registeredUserService, times(1)).createRegisteredUser(any(RegisteredUserDTO.class));
    }

    @Test
    void testGetRegisteredUserById() {
        when(registeredUserService.getRegisteredUserById(1L)).thenReturn(userDTO);

        ResponseEntity<RegisteredUserDTO> response = registeredUserController.getRegisteredUserById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(userDTO.getId(), response.getBody().getId());
        verify(registeredUserService, times(1)).getRegisteredUserById(1L);
    }

    @Test
    void testGetAllRegisteredUsers() {
        List<RegisteredUserDTO> users = Arrays.asList(userDTO);
        when(registeredUserService.getAllRegisteredUsers()).thenReturn(users);

        ResponseEntity<List<RegisteredUserDTO>> response = registeredUserController.getAllRegisteredUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        verify(registeredUserService, times(1)).getAllRegisteredUsers();
    }

    @Test
    void testUpdateRegisteredUser() {
        RegisteredUserDTO updatedUserDTO = new RegisteredUserDTO();
        updatedUserDTO.setId(1L);
        updatedUserDTO.setName("Jane Doe");
        updatedUserDTO.setEmail("jane.doe@example.com");
        // Initialize other fields as necessary

        when(registeredUserService.updateRegisteredUser(eq(1L), any(RegisteredUserDTO.class))).thenReturn(updatedUserDTO);

        ResponseEntity<RegisteredUserDTO> response = registeredUserController.updateRegisteredUser(1L, updatedUserDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Jane Doe", response.getBody().getName());
        verify(registeredUserService, times(1)).updateRegisteredUser(eq(1L), any(RegisteredUserDTO.class));
    }

    @Test
    void testDeleteRegisteredUser() {
        doNothing().when(registeredUserService).deleteRegisteredUser(1L);

        ResponseEntity<Void> response = registeredUserController.deleteRegisteredUser(1L);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(registeredUserService, times(1)).deleteRegisteredUser(1L);
    }
}
