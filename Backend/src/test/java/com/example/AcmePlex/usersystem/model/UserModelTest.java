package com.example.acmeplex.usersystem.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the abstract User model.
 */
class UserModelTest {

    /**
     * A simple concrete subclass of User for testing purposes.
     */
    private static class TestUser extends User {
        public TestUser() {
            super();
        }

        public TestUser(String name, String email, String address) {
            super(name, email, address);
        }
    }

    private TestUser user;

    @BeforeEach
    void setUp() {
        user = new TestUser();
    }

    @Test
    @DisplayName("Test default constructor and getters")
    void testDefaultConstructor() {
        assertNull(user.getId(), "ID should be null by default");
        assertNull(user.getName(), "Name should be null by default");
        assertNull(user.getEmail(), "Email should be null by default");
        assertNull(user.getAddress(), "Address should be null by default");
    }

    @Test
    @DisplayName("Test all-args constructor and getters")
    void testAllArgsConstructor() {
        String name = "John Doe";
        String email = "john.doe@example.com";
        String address = "123 Main St";

        TestUser constructedUser = new TestUser(name, email, address);

        assertNull(constructedUser.getId(), "ID should be null for new user");
        assertEquals(name, constructedUser.getName(), "Names should match");
        assertEquals(email, constructedUser.getEmail(), "Emails should match");
        assertEquals(address, constructedUser.getAddress(), "Addresses should match");
    }

    @Test
    @DisplayName("Test setters and getters")
    void testSettersAndGetters() {
        Long id = 1L;
        String name = "Jane Smith";
        String email = "jane.smith@example.com";
        String address = "456 Elm St";

        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setAddress(address);

        assertEquals(id, user.getId(), "IDs should match");
        assertEquals(name, user.getName(), "Names should match");
        assertEquals(email, user.getEmail(), "Emails should match");
        assertEquals(address, user.getAddress(), "Addresses should match");
    }

    @Test
    @DisplayName("Test ID setter and getter")
    void testIdSetterGetter() {
        Long id = 100L;
        user.setId(id);
        assertEquals(id, user.getId(), "ID should be correctly set and retrieved");
    }

    @Test
    @DisplayName("Test Name setter and getter")
    void testNameSetterGetter() {
        String name = "Alice Johnson";
        user.setName(name);
        assertEquals(name, user.getName(), "Name should be correctly set and retrieved");
    }

    @Test
    @DisplayName("Test Email setter and getter")
    void testEmailSetterGetter() {
        String email = "alice.johnson@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail(), "Email should be correctly set and retrieved");
    }

    @Test
    @DisplayName("Test Address setter and getter")
    void testAddressSetterGetter() {
        String address = "789 Pine St";
        user.setAddress(address);
        assertEquals(address, user.getAddress(), "Address should be correctly set and retrieved");
    }

    @Test
    @DisplayName("Test setting and getting all properties")
    void testAllProperties() {
        Long id = 50L;
        String name = "Bob Williams";
        String email = "bob.williams@example.com";
        String address = "321 Oak St";

        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setAddress(address);

        assertAll("User Properties",
            () -> assertEquals(id, user.getId(), "ID should match"),
            () -> assertEquals(name, user.getName(), "Name should match"),
            () -> assertEquals(email, user.getEmail(), "Email should match"),
            () -> assertEquals(address, user.getAddress(), "Address should match")
        );
    }
}