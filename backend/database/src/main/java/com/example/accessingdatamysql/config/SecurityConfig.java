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

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .authorizeRequests()

            // Public authentication-related endpoints
            .requestMatchers("/api/auth/login", "/api/version", "/api/stop", "/api/stop/status").permitAll()

            // Public endpoints for database and tests
            .requestMatchers(HttpMethod.GET, "/database/all").permitAll()  
            .requestMatchers(HttpMethod.GET, "/database/search").permitAll()  
            .requestMatchers(HttpMethod.GET, "/database/test").permitAll() 
            .requestMatchers(HttpMethod.GET, "/database/groups").permitAll()  
            .requestMatchers(HttpMethod.GET, "/rocks/all").permitAll()  
            .requestMatchers(HttpMethod.GET, "/aggregate/all").permitAll() 
            .requestMatchers(HttpMethod.GET, "/inSituTest/all").permitAll()  
            .requestMatchers(HttpMethod.GET, "/concrete/all").permitAll()  
            .requestMatchers(HttpMethod.GET, "/earthworks/all").permitAll() 
            .requestMatchers(HttpMethod.GET, "/database/groups").permitAll()  
            .requestMatchers(HttpMethod.GET, "/database/id", "/database/group", "/database/classification", "/database/symbol", "/database/parameters").permitAll()

            // Admin-only for write operations
            .requestMatchers("/database/add", "/database/delete/**", "/database/update/**").hasRole("ADMIN")
            .requestMatchers("/aggregate/add", "/aggregate/delete/**", "/aggregate/update/**").hasRole("ADMIN")
            .requestMatchers("/rocks/add", "/rocks/delete/**", "/rocks/update/**").hasRole("ADMIN")
            // Add this under the admin-only section
            .requestMatchers("/edititem/**").hasRole("ADMIN")


            // User + Admin read access
            .requestMatchers(HttpMethod.GET, "/database/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.GET, "/aggregate/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.GET, "/rocks/**").hasAnyRole("USER", "ADMIN")


            // Admin routes
            .requestMatchers("/admin/**").hasRole("ADMIN")

            // User routes
            .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

            // Catch-all
            .anyRequest().authenticated()
            .and()

            // Add JWT filter before UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}
