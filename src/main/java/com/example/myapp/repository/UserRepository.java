package com.example.myapp.repository;

import com.example.myapp.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUserName(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        int age = rs.getInt("age");
        if (!rs.wasNull()) {
            user.setAge(age);
        }
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return user;
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, this::mapRow);
    }

    public Optional<User> findById(Long id) {
        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            User user = jdbcTemplate.queryForObject(sql, this::mapRow, id);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public User save(User user) {
        String sql = "INSERT INTO users(username, email, age, created_at) VALUES (?, ?, ?, ?) RETURNING id";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            if (user.getAge() != null) {
                ps.setInt(3, user.getAge());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setTimestamp(4, Timestamp.valueOf(now));
            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        user.setCreatedAt(now);
        return user;
    }



    public User update(User user) {
        String sql = "UPDATE users SET username=?, email=?, age=? WHERE id=?";
        jdbcTemplate.update(sql,
                user.getUserName(),
                user.getEmail(),
                user.getAge(),
                user.getId()
        );
        return user;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM users";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public List<User> findByUserNameContainingIgnoreCase(String name) {
        String sql = "SELECT * FROM users WHERE LOWER(username) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, this::mapRow, "%" + name + "%");
    }

    public List<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email=?";
        return jdbcTemplate.query(sql, this::mapRow, email);
    }
}
