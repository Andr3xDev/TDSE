package edu.escuelaing.twitter.service;

import edu.escuelaing.twitter.model.Post;
import edu.escuelaing.twitter.model.Stream;
import edu.escuelaing.twitter.model.User;
import edu.escuelaing.twitter.repository.PostRepository;
import edu.escuelaing.twitter.repository.StreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private StreamRepository streamRepository;
    @Autowired
    private UserService userService;

    private static final String GLOBAL_STREAM_NAME = "Global Feed";

    public Post createPost(Post post, String cognitoId) {
        if (post.getContent().length() > 140) {
            throw new IllegalArgumentException("Post exceeds 140 characters");
        }
        User user = userService.getUserByCognitoId(cognitoId);
        post.setUser(user);
        Post savedPost = postRepository.save(post);

        // Agregar al stream global
        Stream globalStream = streamRepository.findByName(GLOBAL_STREAM_NAME).orElseGet(() -> {
            Stream newStream = new Stream();
            newStream.setName(GLOBAL_STREAM_NAME);
            return streamRepository.save(newStream);
        });
        globalStream.getPosts().add(savedPost);
        streamRepository.save(globalStream);

        return savedPost;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
