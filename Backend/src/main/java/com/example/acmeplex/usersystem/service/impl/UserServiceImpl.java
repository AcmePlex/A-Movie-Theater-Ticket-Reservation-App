package com.example.acmeplex.usersystem.service.impl;

import com.example.acmeplex.usersystem.dto.UserDTO;
import com.example.acmeplex.usersystem.model.User;
import com.example.acmeplex.usersystem.repository.UserRepository;
import com.example.acmeplex.usersystem.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    // Logger for tracking events and debugging
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    // Injecting UserRepository to interact with the database
    @Autowired
    private UserRepository userRepository;
    
    // Injecting ModelMapper to handle DTO-Entity conversions
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Converts a User entity to its corresponding DTO.
     *
     * @param user the entity to convert
     * @return the converted UserDTO
     */
    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    /**
     * Converts a UserDTO to its corresponding entity.
     *
     * @param userDTO the DTO to convert
     * @return the converted User entity
     */
    private User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    /**
     * Creates a new user in the system.
     *
     * @param userDTO the DTO containing user details
     * @return the created UserDTO
     * @throws DuplicateResourceException if a user with the same email already exists
     */
    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        logger.info("Attempting to create user with email: {}", userDTO.getEmail());

        // Check if a user with the given email already exists to prevent duplicates
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            logger.error("User creation failed: Email {} is already in use.", userDTO.getEmail());
            throw new DuplicateKeyException("Email already in use: " + userDTO.getEmail());
        }

        // Convert DTO to entity
        User user = convertToEntity(userDTO);
        
        // Save the entity to the database
        User savedUser = userRepository.save(user);
        
        logger.info("User created successfully with ID: {}", savedUser.getId());
        
        // Convert the saved entity back to DTO
        return convertToDTO(savedUser);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return the corresponding UserDTO
     * @throws ResourceNotFoundException if user is not found
     */
    @Override
    public UserDTO getUserById(Long id) {
        logger.info("Fetching user with ID: {}", id);
        
        // Fetch the user or throw an exception if not found
        User user = userRepository.findById(id)
                          .orElseThrow(() -> {
                              logger.error("User not found with ID: {}", id);
                              return new ResourceAccessException("User not found with id: " + id);
                          });
        
        logger.info("User fetched successfully with ID: {}", id);
        
        // Convert the entity to DTO
        return convertToDTO(user);
    }

    /**
     * Retrieves all users in the system.
     *
     * @return a list of UserDTOs
     */
    @Override
    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users");
        
        // Fetch all users and convert each to DTO
        List<UserDTO> users = userRepository.findAll()
                             .stream()
                             .map(this::convertToDTO)
                             .collect(Collectors.toList());
        
        logger.info("Fetched {} users successfully", users.size());
        
        return users;
    }

    /**
     * Updates an existing user's details.
     *
     * @param id the ID of the user to update
     * @param userDTO the DTO containing updated details
     * @return the updated UserDTO
     * @throws ResourceNotFoundException if user is not found
     * @throws DuplicateResourceException if the updated email is already in use by another user
     */
    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        logger.info("Attempting to update user with ID: {}", id);
        
        // Fetch the existing user or throw an exception if not found
        User existingUser = userRepository.findById(id)
                                 .orElseThrow(() -> {
                                     logger.error("User not found with ID: {}", id);
                                     return new ResourceAccessException("User not found with id: " + id);
                                 });
        
        // If email is being updated, check for duplicates
        if (!existingUser.getEmail().equals(userDTO.getEmail()) && userRepository.existsByEmail(userDTO.getEmail())) {
            logger.error("User update failed: Email {} is already in use.", userDTO.getEmail());
            throw new DuplicateKeyException("Email already in use: " + userDTO.getEmail());
        }

        // Update the user's details
        existingUser.setName(userDTO.getName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setAddress(userDTO.getAddress());
        
        // Save the updated user to the database
        User updatedUser = userRepository.save(existingUser);
        
        logger.info("User updated successfully with ID: {}", id);
        
        // Convert the updated entity back to DTO
        return convertToDTO(updatedUser);
    }

    /**
     * Deletes a user from the system.
     *
     * @param id the ID of the user to delete
     * @throws ResourceNotFoundException if user is not found
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        logger.info("Attempting to delete user with ID: {}", id);
        
        // Check if the user exists before attempting deletion
        if (!userRepository.existsById(id)) {
            logger.error("User deletion failed: User not found with ID: {}", id);
            throw new ResourceAccessException("User not found with id: " + id);
        }
        
        // Delete the user by ID
        userRepository.deleteById(id);
        
        logger.info("User deleted successfully with ID: {}", id);
    }
}
