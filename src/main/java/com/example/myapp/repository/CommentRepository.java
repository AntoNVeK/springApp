package com.example.myapp.repository;

import com.example.myapp.dto.CommentWithPostInfo;
import com.example.myapp.model.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public CommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Comment mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Comment comment = new Comment();
        comment.setId(rs.getLong("id"));
        comment.setText(rs.getString("text"));
        comment.setPostId(rs.getLong("post_id"));
        comment.setUserId(rs.getLong("user_id"));
        long parentId = rs.getLong("parent_comment_id");
        if (!rs.wasNull()) comment.setParentCommentId(parentId);
        comment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return comment;
    }

    public Comment save(Comment comment) {
        String sql = "INSERT INTO comments(text, post_id, user_id, parent_comment_id, created_at) VALUES (?, ?, ?, ?, ?) RETURNING id";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, comment.getText());
            ps.setLong(2, comment.getPostId());
            ps.setLong(3, comment.getUserId());
            if (comment.getParentCommentId() != null) {
                ps.setLong(4, comment.getParentCommentId());
            } else {
                ps.setNull(4, java.sql.Types.BIGINT);
            }
            ps.setTimestamp(5, Timestamp.valueOf(now));
            return ps;
        }, keyHolder);

        comment.setId(keyHolder.getKey().longValue());
        comment.setCreatedAt(now);
        return comment;
    }


    public List<Comment> findByPostId(Long postId) {
        return jdbcTemplate.query("SELECT * FROM comments WHERE post_id=? ORDER BY created_at", this::mapRow, postId);
    }

    public List<CommentWithPostInfo> findCommentsWithPostInfo(int limit) {
        String sql = """
            SELECT 
                c.id AS comment_id,
                c.text AS comment_text,
                u.username AS commenter,
                p.title AS post_title,
                (SELECT COUNT(*) FROM comments WHERE post_id = p.id) AS total_comments
            FROM comments c
            INNER JOIN users u ON c.user_id = u.id
            INNER JOIN posts p ON c.post_id = p.id
            ORDER BY c.id
            LIMIT ?
        """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    CommentWithPostInfo c = new CommentWithPostInfo();
                    c.setId(rs.getLong("comment_id"));
                    c.setText(rs.getString("comment_text"));
                    c.setUsername(rs.getString("commenter"));
                    c.setPostTitle(rs.getString("post_title"));
                    c.setTotalComments(rs.getInt("total_comments"));
                    return c;
                },
                limit
        );
        }
}


