package com.example.acmeplex.usersystem.service.impl;

import com.example.acmeplex.usersystem.dto.RegisteredUserDTO;
import com.example.acmeplex.usersystem.model.RegisteredUser;
import com.example.acmeplex.usersystem.repository.RegisteredUserRepository;
import com.example.acmeplex.usersystem.service.RegisteredUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegisteredUserServiceImpl implements RegisteredUserService {

    // Injecting the repository to interact with the database
    @Autowired
    private RegisteredUserRepository registeredUserRepository;
    
    // Injecting ModelMapper to handle DTO-Entity conversions
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Converts a RegisteredUser entity to its corresponding DTO.
     *
     * @param registeredUser the entity to convert
     * @return the converted DTO
     */
    private RegisteredUserDTO convertToDTO(RegisteredUser registeredUser) {
        return modelMapper.map(registeredUser, RegisteredUserDTO.class);
    }

    /**
     * Converts a RegisteredUserDTO to its corresponding entity.
     *
     * @param registeredUserDTO the DTO to convert
     * @return the converted entity
     */
    private RegisteredUser convertToEntity(RegisteredUserDTO registeredUserDTO) {
        return modelMapper.map(registeredUserDTO, RegisteredUser.class);
    }

    /**
     * Creates a new registered user in the system.
     *
     * @param registeredUserDTO the DTO containing user details
     * @return the created RegisteredUserDTO
     */
    @Override
    public RegisteredUserDTO createRegisteredUser(RegisteredUserDTO registeredUserDTO) {
        // Convert DTO to entity
        RegisteredUser registeredUser = convertToEntity(registeredUserDTO);
        
        // Save the entity to the database
        RegisteredUser savedRegisteredUser = registeredUserRepository.save(registeredUser);
        
        // Convert the saved entity back to DTO
        return convertToDTO(savedRegisteredUser);
    }

    /**
     * Retrieves a registered user by their ID.
     *
     * @param id the ID of the user
     * @return the corresponding RegisteredUserDTO
     * @throws ResourceNotFoundException if user is not found
     */
    @Override
    public RegisteredUserDTO getRegisteredUserById(Long id) {
        // Fetch the user or throw an exception if not found
        RegisteredUser registeredUser = registeredUserRepository.findById(id)
                                  .orElseThrow(() -> new ResourceAccessException("Registered User not found with id: " + id));
        
        // Convert the entity to DTO
        return convertToDTO(registeredUser);
    }

    /**
     * Retrieves all registered users in the system.
     *
     * @return a list of RegisteredUserDTOs
     */
    @Override
    public List<RegisteredUserDTO> getAllRegisteredUsers() {
        // Fetch all users and convert each to DTO
        return registeredUserRepository.findAll()
                             .stream()
                             .map(this::convertToDTO)
                             .collect(Collectors.toList());
    }

    /**
     * Updates an existing registered user's details.
     *
     * @param id the ID of the user to update
     * @param registeredUserDTO the DTO containing updated details
     * @return the updated RegisteredUserDTO
     * @throws ResourceNotFoundException if user is not found
     */
    @Override
    public RegisteredUserDTO updateRegisteredUser(Long id, RegisteredUserDTO registeredUserDTO) {
        // Fetch the existing user or throw an exception if not found
        RegisteredUser existingRegisteredUser = registeredUserRepository.findById(id)
                                 .orElseThrow(() -> new ResourceAccessException("Registered User not found with id: " + id));
        
        // Update the user's details
        existingRegisteredUser.setName(registeredUserDTO.getName());
        existingRegisteredUser.setEmail(registeredUserDTO.getEmail());
        existingRegisteredUser.setAddress(registeredUserDTO.getAddress());
        existingRegisteredUser.setCreditCardInfo(registeredUserDTO.getCreditCardInfo());
        existingRegisteredUser.setActiveSubscription(registeredUserDTO.isActiveSubscription());
        
        // Save the updated user to the database
        RegisteredUser updatedRegisteredUser = registeredUserRepository.save(existingRegisteredUser);
        
        // Convert the updated entity back to DTO
        return convertToDTO(updatedRegisteredUser);
    }

    /**
     * Deletes a registered user from the system.
     *
     * @param id the ID of the user to delete
     * @throws ResourceNotFoundException if user is not found
     */
    @Override
    public void deleteRegisteredUser(Long id) {
        // Check if the user exists before attempting deletion
        if (!registeredUserRepository.existsById(id)) {
            throw new ResourceAccessException("Registered User not found with id: " + id);
        }
        
        // Delete the user by ID
        registeredUserRepository.deleteById(id);
    }
}
