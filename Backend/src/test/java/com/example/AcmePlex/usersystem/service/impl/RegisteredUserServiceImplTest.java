package com.example.acmeplex.usersystem.service.impl;

import com.example.acmeplex.usersystem.dto.RegisteredUserDTO;
import com.example.acmeplex.usersystem.model.RegisteredUser;
import com.example.acmeplex.usersystem.repository.RegisteredUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.ResourceAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RegisteredUserServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
public class RegisteredUserServiceImplTest {

    @Mock
    private RegisteredUserRepository registeredUserRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RegisteredUserServiceImpl registeredUserService;

    private RegisteredUserDTO userDTO;
    private RegisteredUser user;

    @BeforeEach
    public void setUp() {
        // Initialize sample data for tests
        userDTO = new RegisteredUserDTO();
        userDTO.setId(1L);
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setAddress("123 Main St");
        userDTO.setCreditCardInfo("4111111111111111");
        userDTO.setActiveSubscription(true);

        user = new RegisteredUser();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setAddress("123 Main St");
        user.setCreditCardInfo("4111111111111111");
        user.setActiveSubscription(true);
    }

    /**
     * Test creating a registered user successfully.
     */
    @Test
    public void testCreateRegisteredUser_Success() {
        // Arrange
        when(modelMapper.map(userDTO, RegisteredUser.class)).thenReturn(user);
        when(registeredUserRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, RegisteredUserDTO.class)).thenReturn(userDTO);

        // Act
        RegisteredUserDTO createdUser = registeredUserService.createRegisteredUser(userDTO);

        // Assert
        assertNotNull(createdUser);
        assertEquals(userDTO.getId(), createdUser.getId());
        verify(registeredUserRepository, times(1)).save(user);
        verify(modelMapper, times(2)).map(any(), any());
    }

    /**
     * Test retrieving a registered user by ID successfully.
     */
    @Test
    public void testGetRegisteredUserById_Success() {
        // Arrange
        Long userId = 1L;
        when(registeredUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, RegisteredUserDTO.class)).thenReturn(userDTO);

        // Act
        RegisteredUserDTO foundUser = registeredUserService.getRegisteredUserById(userId);

        // Assert
        assertNotNull(foundUser);
        assertEquals(userDTO.getId(), foundUser.getId());
        verify(registeredUserRepository, times(1)).findById(userId);
        verify(modelMapper, times(1)).map(user, RegisteredUserDTO.class);
    }

    /**
     * Test retrieving a registered user by ID when not found.
     */
    @Test
    public void testGetRegisteredUserById_NotFound() {
        // Arrange
        Long userId = 1L;
        when(registeredUserRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceAccessException exception = assertThrows(ResourceAccessException.class, () -> {
            registeredUserService.getRegisteredUserById(userId);
        });

        assertEquals("Registered User not found with id: " + userId, exception.getMessage());
        verify(registeredUserRepository, times(1)).findById(userId);
        verify(modelMapper, never()).map(any(), any());
    }

    /**
     * Test retrieving all registered users successfully.
     */
    @Test
    public void testGetAllRegisteredUsers_Success() {
        // Arrange
        RegisteredUser user2 = new RegisteredUser();
        user2.setId(2L);
        user2.setName("Jane Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setAddress("456 Elm St");
        user2.setCreditCardInfo("4222222222222");
        user2.setActiveSubscription(false);

        RegisteredUserDTO userDTO2 = new RegisteredUserDTO();
        userDTO2.setId(2L);
        userDTO2.setName("Jane Smith");
        userDTO2.setEmail("jane.smith@example.com");
        userDTO2.setAddress("456 Elm St");
        userDTO2.setCreditCardInfo("4222222222222");
        userDTO2.setActiveSubscription(false);

        List<RegisteredUser> users = Arrays.asList(user, user2);
        List<RegisteredUserDTO> userDTOs = Arrays.asList(userDTO, userDTO2);

        when(registeredUserRepository.findAll()).thenReturn(users);
        when(modelMapper.map(user, RegisteredUserDTO.class)).thenReturn(userDTO);
        when(modelMapper.map(user2, RegisteredUserDTO.class)).thenReturn(userDTO2);

        // Act
        List<RegisteredUserDTO> allUsers = registeredUserService.getAllRegisteredUsers();

        // Assert
        assertNotNull(allUsers);
        assertEquals(2, allUsers.size());
        verify(registeredUserRepository, times(1)).findAll();
        verify(modelMapper, times(2)).map(any(RegisteredUser.class), eq(RegisteredUserDTO.class));
    }

    /**
     * Test updating a registered user successfully.
     */
    @Test
    public void testUpdateRegisteredUser_Success() {
        // Arrange
        Long userId = 1L;
        RegisteredUserDTO updatedDTO = new RegisteredUserDTO();
        updatedDTO.setName("John Updated");
        updatedDTO.setEmail("john.updated@example.com");
        updatedDTO.setAddress("789 Oak St");
        updatedDTO.setCreditCardInfo("4333333333333333");
        updatedDTO.setActiveSubscription(false);

        RegisteredUser updatedUser = new RegisteredUser();
        updatedUser.setId(userId);
        updatedUser.setName(updatedDTO.getName());
        updatedUser.setEmail(updatedDTO.getEmail());
        updatedUser.setAddress(updatedDTO.getAddress());
        updatedUser.setCreditCardInfo(updatedDTO.getCreditCardInfo());
        updatedUser.setActiveSubscription(updatedDTO.isActiveSubscription());

        RegisteredUserDTO returnedDTO = new RegisteredUserDTO();
        returnedDTO.setId(userId);
        returnedDTO.setName(updatedDTO.getName());
        returnedDTO.setEmail(updatedDTO.getEmail());
        returnedDTO.setAddress(updatedDTO.getAddress());
        returnedDTO.setCreditCardInfo(updatedDTO.getCreditCardInfo());
        returnedDTO.setActiveSubscription(updatedDTO.isActiveSubscription());

        when(registeredUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(registeredUserRepository.save(user)).thenReturn(updatedUser);
        when(modelMapper.map(updatedUser, RegisteredUserDTO.class)).thenReturn(returnedDTO);

        // Act
        RegisteredUserDTO result = registeredUserService.updateRegisteredUser(userId, updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updatedDTO.getName(), result.getName());
        assertEquals(updatedDTO.getEmail(), result.getEmail());
        assertEquals(updatedDTO.getAddress(), result.getAddress());
        assertEquals(updatedDTO.getCreditCardInfo(), result.getCreditCardInfo());
        assertEquals(updatedDTO.isActiveSubscription(), result.isActiveSubscription());

        // Verify that the user was fetched and saved
        verify(registeredUserRepository, times(1)).findById(userId);
        verify(registeredUserRepository, times(1)).save(user);
        verify(modelMapper, times(1)).map(updatedUser, RegisteredUserDTO.class);
    }

    /**
     * Test updating a registered user when the user is not found.
     */
    @Test
    public void testUpdateRegisteredUser_NotFound() {
        // Arrange
        Long userId = 1L;
        RegisteredUserDTO updatedDTO = new RegisteredUserDTO();
        updatedDTO.setName("John Updated");
        updatedDTO.setEmail("john.updated@example.com");
        updatedDTO.setAddress("789 Oak St");
        updatedDTO.setCreditCardInfo("4333333333333333");
        updatedDTO.setActiveSubscription(false);

        when(registeredUserRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceAccessException exception = assertThrows(ResourceAccessException.class, () -> {
            registeredUserService.updateRegisteredUser(userId, updatedDTO);
        });

        assertEquals("Registered User not found with id: " + userId, exception.getMessage());
        verify(registeredUserRepository, times(1)).findById(userId);
        verify(registeredUserRepository, never()).save(any());
        verify(modelMapper, never()).map(any(), any());
    }

    /**
     * Test deleting a registered user successfully.
     */
    @Test
    public void testDeleteRegisteredUser_Success() {
        // Arrange
        Long userId = 1L;
        when(registeredUserRepository.existsById(userId)).thenReturn(true);
        doNothing().when(registeredUserRepository).deleteById(userId);

        // Act
        registeredUserService.deleteRegisteredUser(userId);

        // Assert
        verify(registeredUserRepository, times(1)).existsById(userId);
        verify(registeredUserRepository, times(1)).deleteById(userId);
    }

    /**
     * Test deleting a registered user when the user is not found.
     */
    @Test
    public void testDeleteRegisteredUser_NotFound() {
        // Arrange
        Long userId = 1L;
        when(registeredUserRepository.existsById(userId)).thenReturn(false);

        // Act & Assert
        ResourceAccessException exception = assertThrows(ResourceAccessException.class, () -> {
            registeredUserService.deleteRegisteredUser(userId);
        });

        assertEquals("Registered User not found with id: " + userId, exception.getMessage());
        verify(registeredUserRepository, times(1)).existsById(userId);
        verify(registeredUserRepository, never()).deleteById(anyLong());
    }
}