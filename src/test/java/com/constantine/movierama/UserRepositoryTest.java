package com.constantine.movierama;

import com.constantine.movierama.domain.User;
import com.constantine.movierama.repositories.UserRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class UserRepositoryTest extends BaseRepositoryTest {

    private static Logger logger = LoggerFactory.getLogger(UserRepositoryTest.class);

    @Autowired
    private UserRepository userRepository;

    @Test
    public void userCRUDTest() {

            User user = new User();
            user.setName("test_user");
            user.setSurname("surname");
            user.setUsername("username1");
            user.setPassword("password");

            user = userRepository.save(user);
            assertNotEquals(0, user.getId());

            user = userRepository.findById(user.getId()).orElse(null);

            assertNotNull(user);
            assertEquals("test_user", user.getName());
            userRepository.delete(user);
    }
}
