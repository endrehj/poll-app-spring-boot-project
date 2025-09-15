package com.endre.demo;

import com.endre.demo.domain.PollManager;
import com.endre.demo.domain.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@CrossOrigin(
        origins = "http://localhost:5173",
        allowedHeaders = "*",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS }
)
@RestController
@RequestMapping("/users")
public class UserController {
    private final PollManager pm;

    public UserController(PollManager pm) {
        this.pm = pm;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return pm.saveUser(user);
    }
    @GetMapping
    public Collection<User> getAllUsers() {
        return pm.findAllUsers();
    }
    @GetMapping("/{id}")
    public User getUser(@PathVariable UUID id) {
        return pm.findUser(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
