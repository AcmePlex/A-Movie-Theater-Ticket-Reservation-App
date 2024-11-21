package com.example.acmeplex.usersystem.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the RegisteredUser model.
 */
class RegisteredUserModelTest {

    @Test
    @DisplayName("Test Default Constructor")
    void testDefaultConstructor() {
        RegisteredUser user = new RegisteredUser();

        assertNull(user.getName(), "Name should be null by default");
        assertNull(user.getEmail(), "Email should be null by default");
        assertNull(user.getAddress(), "Address should be null by default");
        assertNull(user.getCreditCardInfo(), "CreditCardInfo should be null by default");
        assertFalse(user.isActiveSubscription(), "ActiveSubscription should be false by default");
    }

    @Test
    @DisplayName("Test All-Args Constructor")
    void testAllArgsConstructor() {
        String name = "John Doe";
        String email = "john.doe@example.com";
        String address = "123 Main St";
        String creditCardInfo = "4111111111111111";
        boolean activeSubscription = true;

        RegisteredUser user = new RegisteredUser(name, email, address, creditCardInfo, activeSubscription);

        assertEquals(name, user.getName(), "Name should match the constructor argument");
        assertEquals(email, user.getEmail(), "Email should match the constructor argument");
        assertEquals(address, user.getAddress(), "Address should match the constructor argument");
        assertEquals(creditCardInfo, user.getCreditCardInfo(), "CreditCardInfo should match the constructor argument");
        assertTrue(user.isActiveSubscription(), "ActiveSubscription should match the constructor argument");
    }

    @Test
    @DisplayName("Test Getters and Setters")
    void testGettersAndSetters() {
        RegisteredUser user = new RegisteredUser();

        String name = "Jane Smith";
        String email = "jane.smith@example.com";
        String address = "456 Elm St";
        String creditCardInfo = "4222222222222";
        boolean activeSubscription = true;

        // Test setters
        user.setName(name);
        user.setEmail(email);
        user.setAddress(address);
        user.setCreditCardInfo(creditCardInfo);
        user.setActiveSubscription(activeSubscription);

        // Test getters
        assertEquals(name, user.getName(), "Name should match the set value");
        assertEquals(email, user.getEmail(), "Email should match the set value");
        assertEquals(address, user.getAddress(), "Address should match the set value");
        assertEquals(creditCardInfo, user.getCreditCardInfo(), "CreditCardInfo should match the set value");
        assertTrue(user.isActiveSubscription(), "ActiveSubscription should match the set value");
    }

    @Test
    @DisplayName("Test Active Subscription Toggle")
    void testActiveSubscriptionToggle() {
        RegisteredUser user = new RegisteredUser();

        // Initially false
        assertFalse(user.isActiveSubscription(), "ActiveSubscription should be false initially");

        // Set to true
        user.setActiveSubscription(true);
        assertTrue(user.isActiveSubscription(), "ActiveSubscription should be true after setting to true");

        // Set back to false
        user.setActiveSubscription(false);
        assertFalse(user.isActiveSubscription(), "ActiveSubscription should be false after setting to false");
    }

    @Test
    @DisplayName("Test Credit Card Info Handling")
    void testCreditCardInfoHandling() {
        RegisteredUser user = new RegisteredUser();

        String creditCardInfo = "378282246310005"; // Example of a valid AmEx card
        user.setCreditCardInfo(creditCardInfo);

        assertEquals(creditCardInfo, user.getCreditCardInfo(), "CreditCardInfo should match the set value");

        // Test with null
        user.setCreditCardInfo(null);
        assertNull(user.getCreditCardInfo(), "CreditCardInfo should be null after setting to null");
    }
}
