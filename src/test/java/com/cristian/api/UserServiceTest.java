package com.cristian.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class UserServiceTest {

    @Test
    public void testCacheAndFetch() {
        // Uses public JSON placeholder API for a deterministic response
        UserService svc = new UserService("https://jsonplaceholder.typicode.com");
        Optional<User> u1 = svc.getUser("1");
        Optional<User> u2 = svc.getUser("1");

        assertTrue(u1.isPresent());
        assertTrue(u2.isPresent());
        assertSame(u1.get().getId(), u2.get().getId());
    }
}
