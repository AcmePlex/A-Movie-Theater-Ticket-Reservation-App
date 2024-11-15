package com.example.acmeplex.usersystem.controller;

import com.example.acmeplex.usersystem.dto.RegisteredUserDTO;
import com.example.acmeplex.usersystem.service.RegisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/registered-users")
public class RegisteredUserController {

    @Autowired
    private RegisteredUserService registeredUserService;

    @PostMapping
    public ResponseEntity<RegisteredUserDTO> createRegisteredUser(@Valid @RequestBody RegisteredUserDTO registeredUserDTO){
        RegisteredUserDTO createdRegisteredUser = registeredUserService.createRegisteredUser(registeredUserDTO);
        return ResponseEntity.ok(createdRegisteredUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisteredUserDTO> getRegisteredUserById(@PathVariable Long id){
        RegisteredUserDTO registeredUser = registeredUserService.getRegisteredUserById(id);
        return ResponseEntity.ok(registeredUser);
    }

    @GetMapping
    public ResponseEntity<List<RegisteredUserDTO>> getAllRegisteredUsers(){
        List<RegisteredUserDTO> registeredUsers = registeredUserService.getAllRegisteredUsers();
        return ResponseEntity.ok(registeredUsers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegisteredUserDTO> updateRegisteredUser(@PathVariable Long id, @Valid @RequestBody RegisteredUserDTO registeredUserDTO){
        RegisteredUserDTO updatedRegisteredUser = registeredUserService.updateRegisteredUser(id, registeredUserDTO);
        return ResponseEntity.ok(updatedRegisteredUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegisteredUser(@PathVariable Long id){
        registeredUserService.deleteRegisteredUser(id);
        return ResponseEntity.noContent().build();
    }
}