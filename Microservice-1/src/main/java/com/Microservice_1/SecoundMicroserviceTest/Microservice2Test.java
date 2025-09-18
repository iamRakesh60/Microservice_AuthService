package com.Microservice_1.SecoundMicroserviceTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/m2/test")
public class Microservice2Test {

    // OPEN URL
    // http://localhost:8082/api/m2/test/welcome
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Microservice 2";
    }


    // only for USER Role
    // http://localhost:8082/api/m2/test/hello
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Microservice2";
    }

    // Only Open for Admin
    // http://localhost:8082/api/m2/test/admin
    @GetMapping("/admin")
    public String admin(){
        return "Testing message for ADMIN from Microservice 2";
    }
}
