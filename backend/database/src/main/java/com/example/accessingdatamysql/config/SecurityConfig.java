package com.example.accessingdatamysql.config;

import com.example.accessingdatamysql.JWT.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Expose the AuthenticationManager bean for use by the login interface.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    /**
     * Configure Spring Security to use stateless JWT authentication and CORS
     * support.
     * Restrict database modification to ADMIN, allow database read for USER and
     * ADMIN.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests()
                // Public access
                .requestMatchers("/api/auth/login", "/api/version", "/api/stop", "/api/stop/status").permitAll()

                // Admin routes
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // User routes
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

                // Allow both USER and ADMIN to read from the database
                .requestMatchers(HttpMethod.GET, "/database/**").hasAnyRole("USER", "ADMIN")

                // Only ADMIN can perform write operations on the database
                .requestMatchers("/database/add", "/database/delete/**", "/database/update/**").hasRole("ADMIN")

                // USER and ADMIN can read Rock database
                .requestMatchers(HttpMethod.GET, "/rocks/**").hasAnyRole("USER", "ADMIN")

                // Only ADMIN can write to Rock database
                .requestMatchers("/rocks/add", "/rocks/delete/**", "/rocks/update/**").hasRole("ADMIN")

                // All other requests must be authenticated
                .anyRequest().authenticated()
                .and()

                // Add JWT authentication filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Use stateless session management
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

}
