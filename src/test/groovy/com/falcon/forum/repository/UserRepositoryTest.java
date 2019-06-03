package com.falcon.forum.repository;

import com.falcon.forum.persist.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void save() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Falcon");
        user.setPassword("secret123");
        user.setEmail("mail@mail.com");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertEquals(savedUser.getUsername(), user.getUsername());
    }

    @Test
    public void getOne() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Falcon");
        user.setPassword("secret123");
        user.setEmail("mail@mail.com");

        User savedUser = userRepository.save(user);
        User userFromRepo = userRepository.getOne(savedUser.getId());

        assertNotNull(userFromRepo);
        assertEquals(userFromRepo.getUsername(), user.getUsername());
        assertEquals(savedUser.getId(), userFromRepo.getId());
        assertEquals(savedUser, userFromRepo);
    }

    @Test
    public void getAll() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Falcon");
        user.setPassword("secret123");
        user.setEmail("mail@mail.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("Falcon2");
        user2.setPassword("secret123");
        user2.setEmail("mail2@mail2.com");


        User savedUser = userRepository.save(user);
        User secondSavedUser = userRepository.save(user2);

        List<User> userList = userRepository.findAll();

        assertEquals(userList.size(), 2);
        assertTrue(userList.contains(savedUser));
        assertTrue(userList.contains(secondSavedUser));
    }

    @Test
    public void findByUsername() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Falcon");
        user.setPassword("secret123");
        user.setEmail("mail@mail.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("Falcon2");
        user2.setPassword("secret123");
        user2.setEmail("mail2@mail2.com");


        User savedUser = userRepository.save(user);
        User secondSavedUser = userRepository.save(user2);

        User userFromRepo = userRepository.findByUsername(savedUser.getUsername());

        assertNotNull(userFromRepo);
        assertEquals(userFromRepo, savedUser);
        assertNotEquals(userFromRepo, secondSavedUser);
    }

    @Test
    public void findUserByEmail() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Falcon");
        user.setPassword("secret123");
        user.setEmail("mail@mail.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("Falcon2");
        user2.setPassword("secret123");
        user2.setEmail("mail2@mail2.com");


        User savedUser = userRepository.save(user);
        User secondSavedUser = userRepository.save(user2);
        User userFromRepo = userRepository.findByEmail(savedUser.getEmail());

        assertNotNull(userFromRepo);
        assertNotEquals(userFromRepo, secondSavedUser);
        assertEquals(userFromRepo, savedUser);
    }

}