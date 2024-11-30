package com.example.acmeplex.usersystem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.acmeplex.paymentsystem.dto.CardDTO;
import com.example.acmeplex.usersystem.dto.RegisteredUserDTO;
import com.example.acmeplex.usersystem.service.RegisteredUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/registered-users")
public class RegisteredUserController {

    @Autowired
    private RegisteredUserService registeredUserService;

    /**
     * Create a new registered user.
     *
     * @param registeredUserDTO DTO containing user information.
     * @return Created user as a response.
     */
    @PostMapping("/createregistereduser")
    public ResponseEntity<Map<String, Object>> createRegisteredUser(@RequestBody RegisteredUserDTO registeredUserDTO) {

        return ResponseEntity.ok(registeredUserService.createRegisteredUser(registeredUserDTO));
    }

    /**
     * Get registered user by email.
     *
     * @param email Email of the user.
     * @return User details as a response.
     */
    @GetMapping("/{email}")
    public ResponseEntity<RegisteredUserDTO> getRegisteredUserByEmail(@PathVariable String email) {
        RegisteredUserDTO registeredUser = registeredUserService.getRegisteredUserByEmail(email);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Get all registered users.
     *
     * @return List of all registered users.
     */
    @GetMapping
    public ResponseEntity<List<RegisteredUserDTO>> getAllRegisteredUsers() {
        List<RegisteredUserDTO> registeredUsers = registeredUserService.getAllRegisteredUsers();
        return ResponseEntity.ok(registeredUsers);
    }

    /**
     * Update a registered user by email.
     *
     * @param email               Email of the user to update.
     * @param registeredUserDTO   Updated user information.
     * @return Updated user details.
     */
    @PutMapping("/{email}")
    public ResponseEntity<RegisteredUserDTO> updateRegisteredUser(@PathVariable String email, @Valid @RequestBody RegisteredUserDTO registeredUserDTO) {
        RegisteredUserDTO updatedRegisteredUser = registeredUserService.updateRegisteredUser(email, registeredUserDTO);
        return ResponseEntity.ok(updatedRegisteredUser);
    }

    /**
     * Delete a registered user by email.
     *
     * @param email Email of the user to delete.
     * @return No content response if deletion is successful.
     */
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteRegisteredUser(@PathVariable String email) {
        registeredUserService.deleteRegisteredUser(email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/login/{email}/password/{password}")
    public ResponseEntity<Map<String, Object>> login(@PathVariable String email, @PathVariable String password) {
        return ResponseEntity.ok(registeredUserService.login(email, password));
    }


}