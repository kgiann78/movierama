package com.constantine.movierama;

import com.constantine.movierama.domain.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {

    @Test
    void userHasName() {
        User user = new User();
        user.setName("user1");

        assertEquals("user1", user.getName());
    }

    @Test
    void userHasSurname() {
        User user = new User();
        user.setSurname("surname");

        assertEquals("surname", user.getSurname());
    }
}
