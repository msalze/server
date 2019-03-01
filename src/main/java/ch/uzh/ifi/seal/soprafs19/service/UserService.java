package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        newUser.setStatus(UserStatus.ONLINE);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public String loginUser(User user){

        User search = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (search != null && search.equals(user)) {
            user.setStatus(UserStatus.ONLINE);
            userRepository.save(user);
            return "/user/" + user.getId().toString();
        }
        return ""; // cannot return null

    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).get();
    }

    public void updateUser(Long userId, User user) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()){

            user.setToken(foundUser.get().getToken());
            userRepository.save(user);
        }

    }

}