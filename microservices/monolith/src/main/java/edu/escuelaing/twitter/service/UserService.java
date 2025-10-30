package edu.escuelaing.twitter.service;

import edu.escuelaing.twitter.model.User;
import edu.escuelaing.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByCognitoId(String cognitoId) {
        return userRepository.findByCognitoId(cognitoId).orElseThrow(
                () -> new RuntimeException("User not found with cognitoId: " + cognitoId));
    }
}
