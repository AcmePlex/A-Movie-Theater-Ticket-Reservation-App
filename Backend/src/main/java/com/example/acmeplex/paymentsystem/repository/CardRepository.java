package com.example.acmeplex.paymentsystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.acmeplex.paymentsystem.entity.Card;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CardRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper for Card
    private final RowMapper<Card> cardRowMapper = new RowMapper<Card>() {
        @Override
        public Card mapRow(ResultSet rs, int rowNum) throws SQLException {
            Card card = new Card(
                rs.getString("email"),
                rs.getString("card_number"),
                rs.getInt("expire_year"),
                rs.getInt("expire_month"),
                rs.getString("cvv"),
                rs.getString("name"),
                rs.getString("type")
            );
            return card;
        }
    };

        /**
     * Insert a new Card.
     *
     * @param card the Card to insert
     * @return the inserted Card
     */
    public Card insert(Card card) {
        String sql = "INSERT INTO card (email, card_number, expire_year, expire_month, cvv, name, type) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, 
                card.getEmail(), 
                card.getCardNumber(), 
                card.getExpireYear(), 
                card.getExpireMonth(), 
                card.getCvv(),
                card.getName(), 
                card.getType());
        return card;
    }


}
