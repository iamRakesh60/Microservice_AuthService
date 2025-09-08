package com.authService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/welcome")
public class WelcomeController {

    //http://localhost:8080/api/v1/welcome/welcome
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Auth Service";
    }

    // http://localhost:8080/api/v1/welcome/hello
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Auth Service";
    }
}
