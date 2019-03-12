package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Optional;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserServiceTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @After
    public void after() {
        userRepository.deleteAll();
    }

    @Test
    public void createUser() {
        Assert.assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");

        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(createdUser.getToken());
        Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE);
        Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getUsers() {
        User testUser1 = new User();
        testUser1.setPassword("testPassword");
        testUser1.setUsername("testUsername");

        User testUser2 = new User();
        testUser2.setPassword("password");
        testUser2.setUsername("username");

        testUser1 = userService.createUser(testUser1);
        testUser2 = userService.createUser(testUser2);

        userRepository.save(testUser1);
        userRepository.save(testUser2);

        Iterable<User> output = userRepository.findAll();
    }


    @Test
    public void loginUser() {
        User user = new User();
        user.setUsername("testName");
        user.setPassword("password");
        User createdUser = userService.createUser(user);
        user = userService.loginUser(createdUser).getBody();
        Assert.assertEquals(UserStatus.ONLINE, user.getStatus());
        Assert.assertNotNull(user.getToken());
    }

    @Test
    public void getUserById() {

        User testUser = new User();
        testUser.setPassword("testPassword");
        testUser.setUsername("testUser");
        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(userRepository.findById(createdUser.getId()));

    }

    @Test
    public void updateUser() {
        User testUser = new User();
        testUser.setPassword("testPassword");
        testUser.setUsername("testUser1");
        User createdUser = userService.createUser(testUser);

        testUser.setUsername("changedUsername");
        LocalDate dateOfBirth = LocalDate.now().minusYears(20);
        testUser.setDateOfBirth(dateOfBirth);

        this.userService.updateUser(createdUser.getId(), testUser);
        Optional<User> foundUser = this.userRepository.findById(createdUser.getId());
        Assert.assertTrue(foundUser.isPresent());
        Assert.assertEquals(foundUser.get().getUsername(), testUser.getUsername());
        Assert.assertEquals(foundUser.get().getDateOfBirth(), testUser.getDateOfBirth());
    }
}
