package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
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




    @PutMapping("/users/{userId}") // update user
    String updateUser(@PathVariable Long userId, @RequestBody User user){
        this.service.updateUser(userId, user);
        return "";
    }

    @PostMapping("/users") // create new user
    User createUser(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }

    @GetMapping("/users") // get all users
    Iterable<User> getAll() {
        return service.getUsers();
    }

    @PostMapping("/create") // add user or say that failed
    String loginUser(@RequestBody User newUser) {
        try {
            return this.service.loginUser(newUser);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not found");
        }


    }
}
