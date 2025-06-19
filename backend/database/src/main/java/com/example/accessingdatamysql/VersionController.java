package com.example.accessingdatamysql;

import java.util.Collections;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController {

    // Save the timestamp of when the backend started as the version number
    private final long startupTime = System.currentTimeMillis();

    // GET /api/version returns something like { “version”: 1680000000000 }
    @GetMapping("/api/version")
    public Map<String, Long> getVersion() {
        return Collections.singletonMap("version", startupTime);
    }
}
