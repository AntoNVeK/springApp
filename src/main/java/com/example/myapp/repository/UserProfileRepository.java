package com.example.myapp.repository;

import com.example.myapp.model.UserProfile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private UserProfile mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        UserProfile profile = new UserProfile();
        profile.setId(rs.getLong("id"));
        profile.setUserId(rs.getLong("user_id"));
        profile.setFullName(rs.getString("full_name"));
        profile.setBio(rs.getString("bio"));
        profile.setCity(rs.getString("city"));
        profile.setStreet(rs.getString("street"));
        profile.setZipCode(rs.getString("zip_code"));
        return profile;
    }

    public UserProfile save(UserProfile profile) {
        String sql = "INSERT INTO user_profiles(user_id, full_name, bio, city, street, zip_code) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                profile.getUserId(),
                profile.getFullName(),
                profile.getBio(),
                profile.getCity(),
                profile.getStreet(),
                profile.getZipCode()
        );
        return profile;
    }

    public Optional<UserProfile> findByUserId(Long userId) {
        try {
            UserProfile profile = jdbcTemplate.queryForObject("SELECT * FROM user_profiles WHERE user_id=?",
                    this::mapRow, userId);
            return Optional.of(profile);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<UserProfile> findAll() {
        return jdbcTemplate.query("SELECT * FROM user_profiles", this::mapRow);
    }
}
