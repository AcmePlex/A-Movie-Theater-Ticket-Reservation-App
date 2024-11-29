package com.example.acmeplex.usersystem.repository;

import com.example.acmeplex.usersystem.model.RegisteredUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class RegisteredUserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RegisteredUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper for RegisteredUser
    private final RowMapper<RegisteredUser> registeredUserRowMapper = new RowMapper<RegisteredUser>() {
        @Override
        public RegisteredUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            RegisteredUser registeredUser = new RegisteredUser();
            registeredUser.setEmail(rs.getString("email"));
            registeredUser.setName(rs.getString("name"));
            registeredUser.setAddress(rs.getString("address"));
            registeredUser.setPassword(rs.getString("password"));
            registeredUser.setSubscriptionExpirationDate(rs.getDate("subscription_expiration_date"));
            registeredUser.setCardNumber(rs.getString("card_number"));
            return registeredUser;
        }
    };

    /**
     * Find a RegisteredUser by email.
     *
     * @param email the email of the user to find
     * @return an Optional containing the RegisteredUser if found, or empty otherwise
     */
    public Optional<RegisteredUser> findByEmail(String email) {
        String sql = "SELECT * FROM registered_user WHERE email = ?";
        List<RegisteredUser> users = jdbcTemplate.query(sql, registeredUserRowMapper, email);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    /**
     * Check if a RegisteredUser exists by email.
     *
     * @param email the email to check for existence
     * @return true if a user with the given email exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM registered_user WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    /**
     * Retrieve all RegisteredUsers.
     *
     * @return a list of all RegisteredUsers
     */
    public List<RegisteredUser> findAll() {
        String sql = "SELECT * FROM registered_user";
        return jdbcTemplate.query(sql, registeredUserRowMapper);
    }

    /**
     * Save a new RegisteredUser.
     *
     * @param registeredUser the RegisteredUser to save
     * @return the Saved RegisteredUser
     */
    public RegisteredUser save(RegisteredUser registeredUser) {
        String sql = "INSERT INTO registered_user (email, name, address, password, subscription_expiration_date, card_number) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        System.out.println(registeredUser.getSubscriptionExpirationDate());
        System.out.println(registeredUser.getEmail());

        jdbcTemplate.update(sql, 
                registeredUser.getEmail(), 
                registeredUser.getName(), 
                registeredUser.getAddress(),
                registeredUser.getPassword(), 
                registeredUser.getSubscriptionExpirationDate(), 
                registeredUser.getCardNumber());
        return registeredUser;
    }

    /**
     * Update an existing RegisteredUser.
     *
     * @param registeredUser the RegisteredUser with updated information
     */
    public void update(RegisteredUser registeredUser) {
        String sql = "UPDATE registered_user SET name = ?, address = ?, password = ?, subscription_expiration_date = ?, card_number = ?" +
                     "WHERE email = ?";
        jdbcTemplate.update(sql, 
                registeredUser.getName(), 
                registeredUser.getAddress(), 
                registeredUser.getPassword(),
                registeredUser.getSubscriptionExpirationDate(), 
                registeredUser.getCardNumber(),
                registeredUser.getEmail()); 
    }

    /**
     * Delete a RegisteredUser by email.
     *
     * @param email the email of the user to delete
     */
    public void deleteByEmail(String email) {
        String sql = "DELETE FROM registered_user WHERE email = ?";
        jdbcTemplate.update(sql, email);
    }

    // Get the expiration date of a RegisteredUser
    public Date getExpirationDate(String email) {
        String sql="SELECT subscription_expiration_date FROM registered_user WHERE email=?";
        return jdbcTemplate.queryForObject(sql, Date.class, email);
    }

    // Update the expiration date of a RegisteredUser
    public void updateExpirationDate(String email, Date expirationDate) {
        String sql="UPDATE registered_user SET subscription_expiration_date=? WHERE email=?";
        jdbcTemplate.update(sql, expirationDate, email);
    }
}
