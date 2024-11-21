package com.example.acmeplex.usersystem.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidatorFactory() {
        validatorFactory.close();
    }

    @Test
    @DisplayName("Valid UserDTO should have no violations")
    void validUserDTO() {
        UserDTO user = new UserDTO(1L, "John Doe", "john.doe@example.com", "123 Main St");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "There should be no validation violations for a valid UserDTO.");
    }

    @Test
    @DisplayName("UserDTO with blank name should have NotBlank violation")
    void userDTOWithBlankName() {
        UserDTO user = new UserDTO(2L, "   ", "jane.doe@example.com", "456 Elm St");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Validation should fail when name is blank.");

        ConstraintViolation<UserDTO> violation = violations.iterator().next();
        assertEquals("Name is mandatory", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    @DisplayName("UserDTO with null name should have NotBlank violation")
    void userDTOWithNullName() {
        UserDTO user = new UserDTO(3L, null, "jane.doe@example.com", "789 Oak St");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Validation should fail when name is null.");

        ConstraintViolation<UserDTO> violation = violations.iterator().next();
        assertEquals("Name is mandatory", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    @DisplayName("UserDTO with invalid email should have Email violation")
    void userDTOWithInvalidEmail() {
        UserDTO user = new UserDTO(4L, "Alice Smith", "invalid-email", "321 Pine St");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Validation should fail when email is invalid.");

        ConstraintViolation<UserDTO> violation = violations.iterator().next();
        assertEquals("Email should be valid", violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    @DisplayName("UserDTO with null email should pass validation")
    void userDTOWithNullEmail() {
        UserDTO user = new UserDTO(5L, "Bob Johnson", null, "654 Maple St");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Validation should pass when email is null as @Email does not enforce non-null.");
    }

    @Test
    @DisplayName("UserDTO with empty address should pass validation")
    void userDTOWithEmptyAddress() {
        UserDTO user = new UserDTO(6L, "Charlie Brown", "charlie.brown@example.com", "");

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Validation should pass when address is empty as it is not constrained.");
    }

    @Test
    @DisplayName("UserDTO with all fields null should fail for name")
    void userDTOAllFieldsNull() {
        UserDTO user = new UserDTO(null, null, null, null);

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Validation should fail when name is null even if other fields are null.");

        // Since name is null, there should be at least one violation for name
        boolean hasNameViolation = violations.stream()
                .anyMatch(v -> "name".equals(v.getPropertyPath().toString()) && "Name is mandatory".equals(v.getMessage()));
        assertTrue(hasNameViolation, "There should be a NotBlank violation for the name field.");
    }
}