package com.example.acmeplex.usersystem.service;

import com.example.acmeplex.usersystem.dto.RegisteredUserDTO;

import java.util.List;

/**
 * Service interface for managing Registered Users.
 * Provides methods to perform CRUD operations and other business logic related to RegisteredUser entities.
 */
public interface RegisteredUserService {
    
    /**
     * Creates a new registered user in the system.
     *
     * @param registeredUserDTO the DTO containing details of the user to be created
     * @return the created RegisteredUserDTO with updated information (e.g., generated ID)
     * @throws DuplicateResourceException if a user with the same email already exists
     */
    RegisteredUserDTO createRegisteredUser(RegisteredUserDTO registeredUserDTO);
    
    /**
     * Retrieves a registered user by their unique ID.
     *
     * @param id the unique identifier of the user
     * @return the RegisteredUserDTO corresponding to the provided ID
     * @throws ResourceNotFoundException if no user is found with the given ID
     */
    RegisteredUserDTO getRegisteredUserById(Long id);
    
    /**
     * Retrieves all registered users in the system.
     *
     * @return a list of all RegisteredUserDTOs
     */
    List<RegisteredUserDTO> getAllRegisteredUsers();
    
    /**
     * Updates the details of an existing registered user.
     *
     * @param id the unique identifier of the user to be updated
     * @param registeredUserDTO the DTO containing updated user details
     * @return the updated RegisteredUserDTO
     * @throws ResourceNotFoundException if no user is found with the given ID
     * @throws DuplicateResourceException if the updated email is already in use by another user
     */
    RegisteredUserDTO updateRegisteredUser(Long id, RegisteredUserDTO registeredUserDTO);
    
    /**
     * Deletes a registered user from the system.
     *
     * @param id the unique identifier of the user to be deleted
     * @throws ResourceNotFoundException if no user is found with the given ID
     */
    void deleteRegisteredUser(Long id);
}
