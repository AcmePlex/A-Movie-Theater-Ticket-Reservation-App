package com.example.acmeplex.usersystem.service;

import com.example.acmeplex.usersystem.dto.UserDTO;

import java.util.List;

/**
 * Service interface for managing Users.
 * Provides methods to perform CRUD operations and other business logic related to User entities.
 */
public interface UserService {
    
    /**
     * Creates a new user in the system.
     *
     * @param userDTO the DTO containing details of the user to be created
     * @return the created UserDTO with updated information (e.g., generated ID)
     * @throws DuplicateResourceException if a user with the same email already exists
     */
    UserDTO createUser(UserDTO userDTO);
    
    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the unique identifier of the user
     * @return the UserDTO corresponding to the provided ID
     * @throws ResourceNotFoundException if no user is found with the given ID
     */
    UserDTO getUserById(Long id);
    
    /**
     * Retrieves all users in the system.
     *
     * @return a list of all UserDTOs
     */
    List<UserDTO> getAllUsers();
    
    /**
     * Updates the details of an existing user.
     *
     * @param id the unique identifier of the user to be updated
     * @param userDTO the DTO containing updated user details
     * @return the updated UserDTO
     * @throws ResourceNotFoundException if no user is found with the given ID
     * @throws DuplicateResourceException if the updated email is already in use by another user
     */
    UserDTO updateUser(Long id, UserDTO userDTO);
    
    /**
     * Deletes a user from the system.
     *
     * @param id the unique identifier of the user to be deleted
     * @throws ResourceNotFoundException if no user is found with the given ID
     */
    void deleteUser(Long id);
}
