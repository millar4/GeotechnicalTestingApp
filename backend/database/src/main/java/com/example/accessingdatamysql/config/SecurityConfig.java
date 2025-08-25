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
            .authorizeHttpRequests(auth -> auth
                // ===== Public authentication endpoints =====
                .requestMatchers("/api/auth/login", "/api/version", "/api/stop", "/api/stop/status").permitAll()

                // ===== Public GET requests =====
                .requestMatchers(HttpMethod.GET,
                    "/database/**",
                    "/rocks/**",
                    "/aggregate/**",
                    "/inSituTest/**",
                    "/concrete/**",
                    "/earthworks/**"
                ).permitAll()

                // ===== Admin-only write operations =====
                // ===== Admin-only write operations =====
                .requestMatchers(
                    "/database/add", "/database/delete/**", "/database/update/**", "/database/update-with-image/**",
                    "/rocks/add", "/rocks/delete/**", "/rocks/update/**", "/rocks/update-with-image/**",
                    "/aggregate/add", "/aggregate/delete/**", "/aggregate/update/**", "/aggregate/update-with-image/**",
                    "/inSituTest/add", "/inSituTest/delete/**", "/inSituTest/update/**", "/inSituTest/update-with-image/**",
                    "/concrete/add", "/concrete/delete/**", "/concrete/update/**", "/concrete/update-with-image/**",
                    "/earthworks/add", "/earthworks/delete/**", "/earthworks/update/**", "/earthworks/update-with-image/**",
                    "/edititem/**", "/additem", "/additem/**"
                ).hasRole("ADMIN")

                // NOTE: use hasAuthority("ADMIN") if your JWT doesn't include ROLE_ prefix
   

                // ===== Admin routes =====
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // ===== User routes =====
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

                // ===== Catch-all =====
                .anyRequest().authenticated()
            )
            // ===== JWT filter =====
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}

