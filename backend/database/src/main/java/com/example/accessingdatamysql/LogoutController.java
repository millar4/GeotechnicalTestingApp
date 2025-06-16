package com.example.accessingdatamysql;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/goodbye")
    public String displayGoodbyePage() {
        return "goodbye"; 
    }
}

