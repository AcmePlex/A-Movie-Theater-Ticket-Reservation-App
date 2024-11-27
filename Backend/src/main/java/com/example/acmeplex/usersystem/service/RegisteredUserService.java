package com.example.acmeplex.usersystem.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import com.example.acmeplex.usersystem.dto.RegisteredUserDTO;
import com.example.acmeplex.usersystem.model.RegisteredUser;
import com.example.acmeplex.usersystem.repository.RegisteredUserRepository;

@Service
public class RegisteredUserService {

    // Logger for tracking events and debugging
    private static final Logger logger = LoggerFactory.getLogger(RegisteredUserService.class);

    // Injecting RegisteredUserRepository to interact with the database
    @Autowired
    private RegisteredUserRepository registeredUserRepository;
    
    // Injecting ModelMapper to handle DTO-Entity conversions
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Converts a RegisteredUser entity to its corresponding DTO.
     *
     * @param registeredUser the entity to convert
     * @return the converted RegisteredUserDTO
     */
    private RegisteredUserDTO convertToDTO(RegisteredUser registeredUser) {
        return modelMapper.map(registeredUser, RegisteredUserDTO.class);
    }

    /**
     * Converts a RegisteredUserDTO to its corresponding entity.
     *
     * @param registeredUserDTO the DTO to convert
     * @return the converted RegisteredUser entity
     */
    private RegisteredUser convertToEntity(RegisteredUserDTO registeredUserDTO) {
        return modelMapper.map(registeredUserDTO, RegisteredUser.class);
    }

    /**
     * Creates a new registered user in the system.
     *
     * @param registeredUserDTO the DTO containing registered user details
     * @return the created RegisteredUserDTO
     * @throws DuplicateKeyException if a user with the same email already exists
     */
    @Transactional
    public Map<String, Object> createRegisteredUser(RegisteredUserDTO registeredUserDTO) {
        Map<String, Object> response = new HashMap<>();
        try{
        logger.info("Attempting to create registered user with email: {}", registeredUserDTO.getEmail());

        // Check if a user with the given email already exists to prevent duplicates
        if (registeredUserRepository.existsByEmail(registeredUserDTO.getEmail())) {
            String duplicate="Email already in use: " + registeredUserDTO.getEmail();
            logger.error("Registered user creation failed: Email {} is already in use.", registeredUserDTO.getEmail());
            response.put("duplicate", true);
            response.put("message", duplicate);
            //throw new DuplicateKeyException("Email already in use: " + registeredUserDTO.getEmail());
            return response;
        }

        // Convert DTO to entity
        RegisteredUser registeredUser = convertToEntity(registeredUserDTO);

        Date today= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.YEAR, 1);
        Date expirationDate = cal.getTime();

        registeredUser.setSubscriptionExpirationDate(expirationDate);

        // Save the entity to the database
        RegisteredUser savedRegisteredUser = registeredUserRepository.save(registeredUser);

        String success="Registered user created successfully with email: {}"+ savedRegisteredUser.getEmail();

        logger.info(success);

        response.put("success", true);
        response.put("message", success);
        return response;
        }catch (RuntimeException exception) {
            response.put("error", true);
            response.put("message", exception.getMessage());
            return response;
        }
        // Convert the saved entity back to DTO

    }

    /**
     * Retrieves a registered user by their email.
     *
     * @param email the email of the registered user
     * @return the corresponding RegisteredUserDTO
     * @throws ResourceAccessException if registered user is not found
     */
    public RegisteredUserDTO getRegisteredUserByEmail(String email) {
        logger.info("Fetching registered user with email: {}", email);
        
        // Fetch the registered user or throw an exception if not found
        RegisteredUser registeredUser = registeredUserRepository.findByEmail(email)
                          .orElseThrow(() -> {
                              logger.error("Registered user not found with email: {}", email);
                              return new ResourceAccessException("Registered user not found with email: " + email);
                          });
        
        logger.info("Registered user fetched successfully with email: {}", email);
        
        // Convert the entity to DTO
        return convertToDTO(registeredUser);
    }

    /**
     * Retrieves all registered users in the system.
     *
     * @return a list of RegisteredUserDTOs
     */
    public List<RegisteredUserDTO> getAllRegisteredUsers() {
        logger.info("Fetching all registered users");
        
        // Fetch all registered users and convert each to DTO
        List<RegisteredUserDTO> registeredUsers = registeredUserRepository.findAll()
                             .stream()
                             .map(this::convertToDTO)
                             .collect(Collectors.toList());
        
        logger.info("Fetched {} registered users successfully", registeredUsers.size());
        
        return registeredUsers;
    }

    /**
     * Updates an existing registered user's details.
     *
     * @param email the email of the registered user to update
     * @param registeredUserDTO the DTO containing updated details
     * @return the updated RegisteredUserDTO
     * @throws ResourceAccessException if registered user is not found
     * @throws DuplicateKeyException if the updated email is already in use by another user
     */
    @Transactional
    public RegisteredUserDTO updateRegisteredUser(String email, RegisteredUserDTO registeredUserDTO) {
        logger.info("Attempting to update registered user with email: {}", email);
        
        // Fetch the existing registered user or throw an exception if not found
        RegisteredUser existingRegisteredUser = registeredUserRepository.findByEmail(email)
                                     .orElseThrow(() -> {
                                         logger.error("Registered user not found with email: {}", email);
                                         return new ResourceAccessException("Registered user not found with email: " + email);
                                     });
        
        // If email is being updated, check for duplicates
        if (!existingRegisteredUser.getEmail().equals(registeredUserDTO.getEmail()) &&
            registeredUserRepository.existsByEmail(registeredUserDTO.getEmail())) {
            logger.error("Registered user update failed: Email {} is already in use.", registeredUserDTO.getEmail());
            throw new DuplicateKeyException("Email already in use: " + registeredUserDTO.getEmail());
        }

        // Update the registered user's details
        existingRegisteredUser.setEmail(registeredUserDTO.getEmail()); // If email is updated as primary key
        existingRegisteredUser.setName(registeredUserDTO.getName());
        existingRegisteredUser.setAddress(registeredUserDTO.getAddress());
        existingRegisteredUser.setPassword(registeredUserDTO.getPassword());
        // Update other fields as necessary
        
        // Save the updated registered user to the database
        RegisteredUser updatedRegisteredUser = registeredUserRepository.save(existingRegisteredUser);
        
        logger.info("Registered user updated successfully with email: {}", email);
        
        // Convert the updated entity back to DTO
        return convertToDTO(updatedRegisteredUser);
    }

    /**
     * Deletes a registered user from the system.
     *
     * @param email the email of the registered user to delete
     * @throws ResourceAccessException if registered user is not found
     */
    @Transactional
    public void deleteRegisteredUser(String email) {
        logger.info("Attempting to delete registered user with email: {}", email);
        
        // Check if the registered user exists before attempting deletion
        if (!registeredUserRepository.existsByEmail(email)) {
            logger.error("Registered user deletion failed: User not found with email: {}", email);
            throw new ResourceAccessException("Registered user not found with email: " + email);
        }
        
        // Delete the registered user by email
        registeredUserRepository.deleteByEmail(email);
        
        logger.info("Registered user deleted successfully with email: {}", email);
    }

    // public RegisteredUserDTO getRegisteredUserByEmail(String email) {
    //     logger.info("Fetching user with email: {}", email);
    //     // Fetch the user by email or throw an exception if not found
    //     RegisteredUser user = registeredUserRepository.findByEmail(email).orElseThrow(() -> {
    //         logger.error("User not found with email: {}", email);
    //         return new ResourceAccessException("User not found with email: " + email);});
    //     logger.info("User fetched successfully with email: {}", email);
    //     // Convert the entity to DTO
    //     return convertToDTO(user);}

    public boolean validRegisteredUser(String email) {
        if (registeredUserRepository.existsByEmail(email)) {
            Date today=new Date();
            if (registeredUserRepository.getExpirationDate(email).compareTo(today) >= 0) {
                return true;
            }
        }            
        return false;
    }

    public void registrationRenewal(String email) {
        Date currentExpirationDate = registeredUserRepository.getExpirationDate(email);
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentExpirationDate);
        cal.add(Calendar.YEAR, 1);
        Date newExpirationDate = cal.getTime();

        registeredUserRepository.updateExpirationDate(email, newExpirationDate);
    }

    public Map<String, Object> login(String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            Date expirationDate = registeredUserRepository.getExpirationDate(email);
            response.put("success", true);
            response.put("expirationDate", expirationDate);
            return response;
        } catch (RuntimeException exception) {
            response.put("error", true);
            response.put("message", exception.getMessage());
            return response;
        }
    }
}

