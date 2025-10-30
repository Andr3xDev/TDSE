package edu.escuelaing.twitter.controller;

import edu.escuelaing.twitter.model.Post;
import edu.escuelaing.twitter.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "*")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping
    public Post createPost(@RequestBody Post post, @RequestParam String cognitoId) {
        return postService.createPost(post, cognitoId);
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }
}
