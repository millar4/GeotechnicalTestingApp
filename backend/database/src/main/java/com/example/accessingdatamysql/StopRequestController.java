package com.example.accessingdatamysql;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StopRequestController {

    // A simple in-memory flag indicating a stop has been requested.
    private volatile boolean stopRequested = false;

    // Called by the frontend (POST /api/stop) to request stopping containers.
    @PostMapping("/api/stop")
    public String requestStop() {
        stopRequested = true; 
        return "Stop request recorded.";
    }

    // Script on the host polls this endpoint to see if stop was requested.
    @GetMapping("/api/stop/status")
    public boolean isStopRequested() {
        return stopRequested;
    }
}
