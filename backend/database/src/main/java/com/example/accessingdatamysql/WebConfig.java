package com.example.accessingdatamysql;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configure CORS so that the front-end (http://localhost:3100) 
 * can call the backend (http://localhost:8080) without being blocked.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
            // Apply to all endpoints
            .addMapping("/**")
            // Allow requests from your front-end's origin
            .allowedOrigins("http://localhost:3100")
            // Allow any HTTP methods (GET, POST, PUT, DELETE, etc.)
            .allowedMethods("*")
            // Allow any custom headers
            .allowedHeaders("*")
            // Allow sending credentials (cookies, authorization headers, etc.)
            .allowCredentials(true);
    }
}