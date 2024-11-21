package com.example.acmeplex.usersystem.service.impl;

import com.example.acmeplex.usersystem.dto.UserDTO;
import com.example.acmeplex.usersystem.model.User;
import com.example.acmeplex.usersystem.repository.UserRepository;
import com.example.acmeplex.usersystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private AutoCloseable closeable;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        // Initialize a sample UserDTO and User
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setAddress("123 Main St");

        user = new UserImpl(); // Assuming UserImpl is a concrete class extending User
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setAddress("123 Main St");
    }

    // Cleanup mocks after tests
    @org.junit.jupiter.api.AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    // A concrete implementation of the abstract User class for testing
    // This is necessary since User is abstract and cannot be instantiated directly
    private static class UserImpl extends User {
        private Long id;
        private String name;
        private String email;
        private String address;

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getEmail() {
            return email;
        }
        
        @Override
        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String getAddress() {
            return address;
        }
        
        @Override
        public void setAddress(String address) {
            this.address = address;
        }
    }

    @Nested
    @DisplayName("createUser Tests")
    class CreateUserTests {

        @Test
        @DisplayName("should create a new user successfully")
        void testCreateUserSuccess() {
            // Arrange
            when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
            when(modelMapper.map(userDTO, User.class)).thenReturn(user);
            when(userRepository.save(user)).thenReturn(user);
            when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

            // Act
            UserDTO createdUser = userService.createUser(userDTO);

            // Assert
            assertNotNull(createdUser);
            assertEquals(userDTO.getId(), createdUser.getId());
            assertEquals(userDTO.getName(), createdUser.getName());
            assertEquals(userDTO.getEmail(), createdUser.getEmail());
            assertEquals(userDTO.getAddress(), createdUser.getAddress());

            verify(userRepository, times(1)).existsByEmail(userDTO.getEmail());
            verify(modelMapper, times(1)).map(userDTO, User.class);
            verify(userRepository, times(1)).save(user);
            verify(modelMapper, times(1)).map(user, UserDTO.class);
        }

        @Test
        @DisplayName("should throw DuplicateKeyException when email is already in use")
        void testCreateUserDuplicateEmail() {
            // Arrange
            when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

            // Act & Assert
            DuplicateKeyException exception = assertThrows(DuplicateKeyException.class, () -> {
                userService.createUser(userDTO);
            });

            assertEquals("Email already in use: " + userDTO.getEmail(), exception.getMessage());

            verify(userRepository, times(1)).existsByEmail(userDTO.getEmail());
            verify(userRepository, never()).save(any(User.class));
            verify(modelMapper, never()).map(any(UserDTO.class), eq(User.class));
        }
    }

    @Nested
    @DisplayName("getUserById Tests")
    class GetUserByIdTests {

        @Test
        @DisplayName("should fetch user by ID successfully")
        void testGetUserByIdSuccess() {
            // Arrange
            Long userId = 1L;
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

            // Act
            UserDTO fetchedUser = userService.getUserById(userId);

            // Assert
            assertNotNull(fetchedUser);
            assertEquals(userDTO.getId(), fetchedUser.getId());
            assertEquals(userDTO.getName(), fetchedUser.getName());
            assertEquals(userDTO.getEmail(), fetchedUser.getEmail());
            assertEquals(userDTO.getAddress(), fetchedUser.getAddress());

            verify(userRepository, times(1)).findById(userId);
            verify(modelMapper, times(1)).map(user, UserDTO.class);
        }

        @Test
        @DisplayName("should throw ResourceAccessException when user not found")
        void testGetUserByIdNotFound() {
            // Arrange
            Long userId = 1L;
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // Act & Assert
            ResourceAccessException exception = assertThrows(ResourceAccessException.class, () -> {
                userService.getUserById(userId);
            });

            assertEquals("User not found with id: " + userId, exception.getMessage());

            verify(userRepository, times(1)).findById(userId);
            verify(modelMapper, never()).map(any(User.class), eq(UserDTO.class));
        }
    }

    @Nested
    @DisplayName("getAllUsers Tests")
    class GetAllUsersTests {

        @Test
        @DisplayName("should fetch all users successfully")
        void testGetAllUsersSuccess() {
            // Arrange
            UserDTO userDTO2 = new UserDTO();
            userDTO2.setId(2L);
            userDTO2.setName("Jane Smith");
            userDTO2.setEmail("jane.smith@example.com");
            userDTO2.setAddress("456 Elm St");

            User user2 = new UserImpl();
            user2.setId(2L);
            user2.setName("Jane Smith");
            user2.setEmail("jane.smith@example.com");
            user2.setAddress("456 Elm St");

            List<User> users = Arrays.asList(user, user2);
            List<UserDTO> userDTOList = Arrays.asList(userDTO, userDTO2);

            when(userRepository.findAll()).thenReturn(users);
            when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
            when(modelMapper.map(user2, UserDTO.class)).thenReturn(userDTO2);

            // Act
            List<UserDTO> fetchedUsers = userService.getAllUsers();

            // Assert
            assertNotNull(fetchedUsers);
            assertEquals(2, fetchedUsers.size());
            assertTrue(fetchedUsers.contains(userDTO));
            assertTrue(fetchedUsers.contains(userDTO2));

            verify(userRepository, times(1)).findAll();
            verify(modelMapper, times(1)).map(user, UserDTO.class);
            verify(modelMapper, times(1)).map(user2, UserDTO.class);
        }

        @Test
        @DisplayName("should return empty list when no users are found")
        void testGetAllUsersEmpty() {
            // Arrange
            when(userRepository.findAll()).thenReturn(Arrays.asList());

            // Act
            List<UserDTO> fetchedUsers = userService.getAllUsers();

            // Assert
            assertNotNull(fetchedUsers);
            assertTrue(fetchedUsers.isEmpty());

            verify(userRepository, times(1)).findAll();
            verify(modelMapper, never()).map(any(User.class), eq(UserDTO.class));
        }
    }

    @Nested
    @DisplayName("updateUser Tests")
    class UpdateUserTests {

        @Test
        @DisplayName("should update user successfully")
        void testUpdateUserSuccess() {
            // Arrange
            Long userId = 1L;
            UserDTO updatedDTO = new UserDTO();
            updatedDTO.setId(userId);
            updatedDTO.setName("John Updated");
            updatedDTO.setEmail("john.updated@example.com");
            updatedDTO.setAddress("789 Oak St");

            User updatedUser = new UserImpl();
            updatedUser.setId(userId);
            updatedUser.setName(updatedDTO.getName());
            updatedUser.setEmail(updatedDTO.getEmail());
            updatedUser.setAddress(updatedDTO.getAddress());

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.existsByEmail(updatedDTO.getEmail())).thenReturn(false);
            when(userRepository.save(any(User.class))).thenReturn(updatedUser);
            when(modelMapper.map(updatedUser, UserDTO.class)).thenReturn(updatedDTO);

            // Act
            UserDTO result = userService.updateUser(userId, updatedDTO);

            // Assert
            assertNotNull(result);
            assertEquals(updatedDTO.getId(), result.getId());
            assertEquals(updatedDTO.getName(), result.getName());
            assertEquals(updatedDTO.getEmail(), result.getEmail());
            assertEquals(updatedDTO.getAddress(), result.getAddress());

            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, times(1)).existsByEmail(updatedDTO.getEmail());
            verify(userRepository, times(1)).save(any(User.class));
            verify(modelMapper, times(1)).map(updatedUser, UserDTO.class);
        }

        @Test
        @DisplayName("should throw ResourceAccessException when user to update is not found")
        void testUpdateUserNotFound() {
            // Arrange
            Long userId = 1L;
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // Act & Assert
            ResourceAccessException exception = assertThrows(ResourceAccessException.class, () -> {
                userService.updateUser(userId, userDTO);
            });

            assertEquals("User not found with id: " + userId, exception.getMessage());

            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, never()).existsByEmail(anyString());
            verify(userRepository, never()).save(any(User.class));
            verify(modelMapper, never()).map(any(User.class), eq(UserDTO.class));
        }

        @Test
        @DisplayName("should throw DuplicateKeyException when updated email is already in use")
        void testUpdateUserDuplicateEmail() {
            // Arrange
            Long userId = 1L;
            String duplicateEmail = "duplicate@example.com";
            userDTO.setEmail(duplicateEmail);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.existsByEmail(duplicateEmail)).thenReturn(true);

            // Act & Assert
            DuplicateKeyException exception = assertThrows(DuplicateKeyException.class, () -> {
                userService.updateUser(userId, userDTO);
            });

            assertEquals("Email already in use: " + duplicateEmail, exception.getMessage());

            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, times(1)).existsByEmail(duplicateEmail);
            verify(userRepository, never()).save(any(User.class));
            verify(modelMapper, never()).map(any(User.class), eq(UserDTO.class));
        }

        @Test
        @DisplayName("should allow updating without changing email")
        void testUpdateUserWithoutChangingEmail() {
            // Arrange
            Long userId = 1L;

            UserDTO updatedDTO = new UserDTO();
            updatedDTO.setId(userId);
            updatedDTO.setName("John Updated");
            updatedDTO.setEmail(user.getEmail()); // Email remains the same
            updatedDTO.setAddress("789 Oak St");

            User updatedUser = new UserImpl();
            updatedUser.setId(userId);
            updatedUser.setName(updatedDTO.getName());
            updatedUser.setEmail(updatedDTO.getEmail());
            updatedUser.setAddress(updatedDTO.getAddress());

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            // Since email is not changed, existsByEmail should not be called
            when(userRepository.save(any(User.class))).thenReturn(updatedUser);
            when(modelMapper.map(updatedUser, UserDTO.class)).thenReturn(updatedDTO);

            // Act
            UserDTO result = userService.updateUser(userId, updatedDTO);

            // Assert
            assertNotNull(result);
            assertEquals(updatedDTO.getId(), result.getId());
            assertEquals(updatedDTO.getName(), result.getName());
            assertEquals(updatedDTO.getEmail(), result.getEmail());
            assertEquals(updatedDTO.getAddress(), result.getAddress());

            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, never()).existsByEmail(anyString());
            verify(userRepository, times(1)).save(any(User.class));
            verify(modelMapper, times(1)).map(updatedUser, UserDTO.class);
        }
    }

    @Nested
    @DisplayName("deleteUser Tests")
    class DeleteUserTests {

        @Test
        @DisplayName("should delete user successfully")
        void testDeleteUserSuccess() {
            // Arrange
            Long userId = 1L;
            when(userRepository.existsById(userId)).thenReturn(true);
            doNothing().when(userRepository).deleteById(userId);

            // Act
            assertDoesNotThrow(() -> {
                userService.deleteUser(userId);
            });

            // Assert
            verify(userRepository, times(1)).existsById(userId);
            verify(userRepository, times(1)).deleteById(userId);
        }

        @Test
        @DisplayName("should throw ResourceAccessException when user to delete is not found")
        void testDeleteUserNotFound() {
            // Arrange
            Long userId = 1L;
            when(userRepository.existsById(userId)).thenReturn(false);

            // Act & Assert
            ResourceAccessException exception = assertThrows(ResourceAccessException.class, () -> {
                userService.deleteUser(userId);
            });

            assertEquals("User not found with id: " + userId, exception.getMessage());

            verify(userRepository, times(1)).existsById(userId);
            verify(userRepository, never()).deleteById(anyLong());
        }
    }
}