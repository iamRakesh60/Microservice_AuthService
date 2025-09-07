package com.authService.controller;

import com.authService.payload.APIResponse;
import com.authService.payload.UserDto;
import com.authService.repository.UserRepository;
import com.authService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    // http://localhost:8080/api/v1/auth/register
    @PostMapping("/register")
    public ResponseEntity<APIResponse<String>> register(@RequestBody UserDto dto) {
        APIResponse<String> registered = userService.register(dto);
        return new ResponseEntity<>(registered, HttpStatusCode.valueOf(registered.getStatus()));
    }

//  7no complete





















    //http://localhost:8080/api/v1/auth/welcome
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Auth Service";
    }

    // http://localhost:8080/api/v1/auth/hello
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Auth Service";
    }

}
