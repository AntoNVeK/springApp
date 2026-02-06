package com.example.myapp.repository;

import com.example.myapp.dto.PostWithCountComments;
import com.example.myapp.model.Post;
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
public class PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Post mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Post post = new Post();
        post.setId(rs.getLong("id"));
        post.setTitle(rs.getString("title"));
        post.setContent(rs.getString("content"));
        post.setAuthorId(rs.getLong("author_id"));
        post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return post;
    }

    public Post save(Post post) {
        String sql = "INSERT INTO posts(title, content, author_id, created_at) VALUES (?, ?, ?, ?) RETURNING id";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            if (post.getContent() != null) {
                ps.setString(2, post.getContent());
            } else {
                ps.setNull(2, java.sql.Types.VARCHAR);
            }
            ps.setLong(3, post.getAuthorId());
            ps.setTimestamp(4, Timestamp.valueOf(now));
            return ps;
        }, keyHolder);

        post.setId(keyHolder.getKey().longValue());
        post.setCreatedAt(now);

        return post;
    }


    public List<Post> findAll() {
        return jdbcTemplate.query("SELECT * FROM posts ORDER BY created_at DESC", this::mapRow);
    }

    public Optional<Post> findById(Long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject("SELECT * FROM posts WHERE id=?", this::mapRow, id));
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<PostWithCountComments> findPostsWithCommentCount() {
        String sql = """
            SELECT p.id, p.title, COUNT(c.id) AS comment_count
            FROM posts p
            LEFT JOIN comments c ON p.id = c.post_id
            GROUP BY p.id, p.title
            ORDER BY comment_count DESC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new PostWithCountComments(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getLong("comment_count")
        ));
    }

}


