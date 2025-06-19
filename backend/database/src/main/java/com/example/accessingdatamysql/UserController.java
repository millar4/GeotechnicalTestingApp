package com.example.accessingdatamysql;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.accessingdatamysql.JWT.JwtTokenProvider;
import com.example.accessingdatamysql.User.Role;

@Controller
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthenticationManager authenticationManager; 
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider; //token

    // =========================================
    // =========== Thymeleaf===========
    // =========================================
    
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register"; 
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, 
                               @RequestParam String password, 
                               @RequestParam String role) {
        Role userRole = Role.valueOf(role.toUpperCase()); 
        userService.createUser(username, password, userRole);
        return "redirect:/login"; 
    }

    // =========================================
    // =========== JSON===========
    // =========================================

    @PostMapping("/api/auth/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty() ||
            loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Username and password must not be empty."));
        }
        try {
            // Try to authenticate the username and password
            var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );
            //Record authentication information
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3.make JWT
            var userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(userDetails);

            //Query user entities in the database for user roles
            var dbUserOpt = userService.findByUsername(loginRequest.getUsername());
            if (dbUserOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "User not found in DB."));
            }
            var dbUser = dbUserOpt.get();
            String role = dbUser.getRole().name(); //ADMIN / USER

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("role", role);
            return ResponseEntity.ok(responseBody);

        } catch (BadCredentialsException ex) {
    
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Invalid username or password."));
        } catch (Exception e) {
         
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Login failed: " + e.getMessage()));
        }
    }

    public static class LoginRequest {
        private String username;
        private String password;
        
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }
}
