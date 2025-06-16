package com.example.accessingdatamysql;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Sample interface: administrator-only dashboard content
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminDashboard() {
        return "This is the administrator dashboard";
    }

    /**
     * Query list of all users (returns JSON)
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    /**
     * :: Create user (pass username / password / role in JSON)
     * e.g：
     * {
     * "username": "testUser",
     * "password": "123456",
     * "role": "ADMIN"
     * }
     */
    @PostMapping("/createUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        // Calling the userService.createUser method automatically BCrypts the password.
        userService.createUser(
                user.getUsername(),
                user.getPassword(),
                user.getRole());
        return ResponseEntity.ok("User created successfully");
    }

    /**
     * :: Deletion of users: by user ID
     */
    @DeleteMapping("/deleteUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found, cannot delete");
        }
        userRepository.delete(userOpt.get());
        return ResponseEntity.ok("User deleted successfully");
    }

    /**
     * :: Update users
     * If the front-end wants to change the password, note that the new plaintext
     * password must be passed; it will be encrypted twice.
     * If you just want to keep the original password, you can leave the password
     * empty or not pass it in the request body, in this example, the update is
     * skipped.
     */
    @PutMapping("/updateUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> existingOpt = userRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found, cannot update");
        }
        User existing = existingOpt.get();

        // If the front-end passes a new username, update the
        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
            existing.setUsername(updatedUser.getUsername());
        }
        // If the frontend passes a new password, update it (see userService.saveUser
        // for BCrypt encryption)
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existing.setPassword(updatedUser.getPassword());
        }
        // Update if the front-end passes the role
        if (updatedUser.getRole() != null) {
            existing.setRole(updatedUser.getRole());
        }

        userService.saveUser(existing);// The new password is encrypted twice
        return ResponseEntity.ok("User updated successfully");
    }
}
