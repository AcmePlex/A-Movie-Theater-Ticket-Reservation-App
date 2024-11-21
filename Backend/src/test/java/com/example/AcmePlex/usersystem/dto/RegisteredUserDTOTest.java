package com.example.acmeplex.usersystem.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegisteredUserDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Test that validation fails when creditCardInfo is null.
     */
    @Test
    void whenCreditCardInfoIsNull_thenValidationFails() {
        RegisteredUserDTO user = new RegisteredUserDTO();
        user.setCreditCardInfo(null);
        // Set other mandatory fields
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setAddress("123 Main St");
        user.setActiveSubscription(true);

        Set<ConstraintViolation<RegisteredUserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Validation should fail when creditCardInfo is null");

        boolean hasCreditCardViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("creditCardInfo") &&
                        v.getMessage().equals("Credit Card Info is mandatory"));
        assertTrue(hasCreditCardViolation, "Should have creditCardInfo violation");
    }

    /**
     * Test that validation fails when creditCardInfo is blank.
     */
    @Test
    void whenCreditCardInfoIsBlank_thenValidationFails() {
        RegisteredUserDTO user = new RegisteredUserDTO();
        user.setCreditCardInfo("   "); // Blank spaces
        // Set other mandatory fields
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        user.setAddress("456 Elm St");
        user.setActiveSubscription(false);

        Set<ConstraintViolation<RegisteredUserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Validation should fail when creditCardInfo is blank");

        boolean hasCreditCardViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("creditCardInfo") &&
                        v.getMessage().equals("Credit Card Info is mandatory"));
        assertTrue(hasCreditCardViolation, "Should have creditCardInfo violation");
    }

    /**
     * Test that validation passes when all fields are valid.
     */
    @Test
    void whenAllFieldsAreValid_thenNoValidationErrors() {
        RegisteredUserDTO user = new RegisteredUserDTO();
        user.setId(1L);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setAddress("789 Oak St");
        user.setCreditCardInfo("4111111111111111");
        user.setActiveSubscription(true);

        Set<ConstraintViolation<RegisteredUserDTO>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Validation should pass when all fields are valid");
    }

    /**
     * Test the getters and setters.
     */
    @Test
    void testGettersAndSetters() {
        RegisteredUserDTO user = new RegisteredUserDTO();
        user.setId(2L);
        user.setName("Bob");
        user.setEmail("bob@example.com");
        user.setAddress("321 Pine St");
        user.setCreditCardInfo("5500000000000004");
        user.setActiveSubscription(false);

        assertEquals(2L, user.getId());
        assertEquals("Bob", user.getName());
        assertEquals("bob@example.com", user.getEmail());
        assertEquals("321 Pine St", user.getAddress());
        assertEquals("5500000000000004", user.getCreditCardInfo());
        assertFalse(user.isActiveSubscription());
    }

    /**
     * Test that null values in non-annotated fields are handled correctly.
     * Assuming 'address' can be null if not annotated.
     */
    @Test
    void whenOptionalFieldsAreNull_thenValidationPasses() {
        RegisteredUserDTO user = new RegisteredUserDTO();
        user.setCreditCardInfo("340000000000009");
        // Set mandatory fields
        user.setName("Charlie");
        user.setEmail("charlie@example.com");
        // Address is optional
        user.setActiveSubscription(false);

        Set<ConstraintViolation<RegisteredUserDTO>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Validation should pass when optional fields are null");
    }

    /**
     * Test that email format is validated if there's a constraint (assuming UserDTO has @Email).
     */
    @Test
    void whenEmailIsInvalid_thenValidationFails() {
        RegisteredUserDTO user = new RegisteredUserDTO();
        user.setCreditCardInfo("4111111111111111");
        user.setName("David");
        user.setEmail("invalid-email"); // Invalid email format
        user.setAddress("654 Maple St");
        user.setActiveSubscription(true);

        Set<ConstraintViolation<RegisteredUserDTO>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Validation should fail when email format is invalid");

        boolean hasEmailViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
        assertTrue(hasEmailViolation, "Should have email format violation");
    }
}