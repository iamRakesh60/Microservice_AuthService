package com.authService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/welcome")
public class WelcomeController {

    // OPEN URL
    //http://localhost:8080/api/v1/welcome/welcome
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Auth Service";
    }


    // only for USER Role
    // http://localhost:8080/api/v1/welcome/hello
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Auth Service";
    }

    // Only Open for Admin
    // http://localhost:8080/api/v1/welcome/admin
    @GetMapping("/admin")
    public String admin(){
        return "Testing message for ADMIN";
    }
}
