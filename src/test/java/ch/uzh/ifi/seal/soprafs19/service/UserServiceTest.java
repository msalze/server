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
    } // delete all entries after test

    @Test
    public void createUser() {
        Assert.assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");

        // create new user
        User createdUser = userService.createUser(testUser);

        // test for characterization of creating a user
        Assert.assertNotNull(createdUser.getToken());
        Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE);
        Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));

        // if username is already taken, an error needs to be thrown when calling createUser()
        try {
            userService.createUser(testUser);
            Assert.fail();
        } catch (Exception ex) {
        }


    }

    @Test
    public void getUsers() {
        Assert.assertFalse(userService.getUsers().iterator().hasNext());
        User testUser1 = new User();
        testUser1.setPassword("testPassword");
        testUser1.setUsername("testUsername");

        User testUser2 = new User();
        testUser2.setPassword("password");
        testUser2.setUsername("username");

        testUser1 = userService.createUser(testUser1);

        Iterator<User> users = userService.getUsers().iterator();

        // assert that iterator has one element
        Assert.assertTrue(users.hasNext());
        users.next();
        Assert.assertFalse(users.hasNext());

        // add another user
        testUser2 = userService.createUser(testUser2);

        users = userService.getUsers().iterator();

        // assert that repository contains two users
        Assert.assertTrue(users.hasNext());
        users.next();
        Assert.assertTrue(users.hasNext());
        users.next();
        Assert.assertFalse(users.hasNext());

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


        // assert that error gets thrown if wrong password is used
        try {
            user.setPassword("wrongPassword");
            userService.loginUser(user);
            Assert.fail("Cannot log in with wrong password");
        } catch (Exception ex) {

        }
    }

    @Test
    public void getUserById() {
        User testUser = new User();
        testUser.setPassword("testPassword");
        testUser.setUsername("testUser");
        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(userService.getUserById(createdUser.getId()));

        // only one user was created, find id that is not in database and assert that error gets thrown if getUserById() is called
        long newLong = 123;
        if (createdUser.getId() != newLong) {
            try {
                userService.getUserById(newLong);
                Assert.fail();
            } catch (Exception ex) {

            }
        } else {
            try {
                long otherLong = 124;
                userService.getUserById(otherLong);
                Assert.fail();
            } catch (Exception ex) {

            }
        }
      /*  try {
            userService.getUserById()
        }*/
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
