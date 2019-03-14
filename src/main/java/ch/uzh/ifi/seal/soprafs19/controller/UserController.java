package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users/{userId}") // get user information by id
    User all(@PathVariable Long userId) {
        return service.getUserById(userId);
    }

    @GetMapping("/users") // get all users
    Iterable<User> getAll() {
        return service.getUsers();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateUser(@PathVariable long id, @RequestBody User user){ // use information from RequestBody to update user
        this.service.updateUser(id, user);
    }

    @PostMapping("/users") // create new user
    User createUser(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }

    @PostMapping("/login") // add user or say that failed
    ResponseEntity<User> loginUser(@RequestBody User user) {
        return this.service.loginUser(user);
    }

}
