package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
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
import java.util.Iterator;

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

    @Test
    public void createUser() {
        Assert.assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");

        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(createdUser.getToken());
        Assert.assertEquals(createdUser.getStatus(),UserStatus.ONLINE);
        Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getUsers() {
        Assert.assertNull(userRepository.findAll());
        User testUser1 = new User();
        testUser1.setPassword("testPassword");
        testUser1.setUsername("testUsername");

        User testUser2 = new User();
        testUser2.setPassword("password");
        testUser2.setUsername("username");

        userRepository.save(testUser1);
        userRepository.save(testUser2);

        Iterable<User> output = userRepository.findAll();
    }


    @Test
    public void loginUser() {
    }

    @Test
    public void getUserById() {
        Assert.assertNull(userRepository.findByUsername("testUser"));
        Assert.assertTrue(userRepository.findById(new Long("1")).isEmpty());

        User testUser = new User();
        testUser.setPassword("testPassword");
        testUser.setUsername("testUser");
        testUser.setId(new Long("1"));

        userRepository.save(testUser);

        Assert.assertNotNull(userRepository.findById(new Long("1")));

    }

    @Test
    public void updateUser() {
    }
}
