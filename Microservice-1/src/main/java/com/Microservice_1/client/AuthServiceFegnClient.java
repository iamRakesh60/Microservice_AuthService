package com.Microservice_1.client;

import com.Microservice_1.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "authservice")
public interface AuthServiceFegnClient {

    @GetMapping("/api/v1/auth/get-user")
    UserDto findByUsername(@RequestParam("username") String username, @RequestHeader("Authorization") String token);
}


//   22.55 min