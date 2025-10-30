package edu.escuelaing.twitter.controller;

import edu.escuelaing.twitter.model.User;
import edu.escuelaing.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{cognitoId}")
    public User getUser(@PathVariable String cognitoId) {
        return userService.getUserByCognitoId(cognitoId);
    }
}
