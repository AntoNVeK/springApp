package com.example.myapp.service;

import com.example.myapp.dto.PostRequest;
import com.example.myapp.dto.PostWithCountComments;
import com.example.myapp.model.Post;
import com.example.myapp.repository.PostRepository;
import com.example.myapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Post createPost(PostRequest postRequest) {
        // Проверяем существование автора
        if (!userRepository.existsById(postRequest.getAuthorId())) {
            throw new RuntimeException("User not found with id: " + postRequest.getAuthorId());
        }

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setAuthorId(postRequest.getAuthorId());

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public List<PostWithCountComments> getPostsWithCommentCount() {
        return postRepository.findPostsWithCommentCount();
    }
}