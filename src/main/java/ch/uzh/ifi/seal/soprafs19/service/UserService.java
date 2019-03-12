package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.apache.juli.OneLineFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User newUser) {
        //  TODO: get user and set status to online (insted of new user)
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);
        newUser.setDateOfCreation(LocalDate.now());
        if (userRepository.existsByUsername(newUser.getUsername())) {
            throw new UsernameTakenException();
        }
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public ResponseEntity<User> loginUser(User user){
        try {
            User search = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
            search.setStatus(UserStatus.ONLINE);
            search.setToken(UUID.randomUUID().toString());
            userRepository.save(search);
            // return value either: "/user/" + search.getId().toString() or token
            return new ResponseEntity<User>(search, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception ex) {
            throw new WrongLoginException();
        }
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).get();
    }

    public void updateUser(Long userId, User user) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameTakenException();
        }
        if (foundUser.isPresent()){
            User getUser = foundUser.get();
            if (user.getUsername() != null) {
                getUser.setUsername(user.getUsername());
            }
            if (user.getDateOfBirth() != null) {
                getUser.setDateOfBirth(user.getDateOfBirth());
            }
            getUser.setToken(foundUser.get().getToken());
            userRepository.save(getUser);
        } else {
            throw new UserDoesNotExistException();
        }

    }

}