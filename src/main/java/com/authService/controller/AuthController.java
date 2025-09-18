package com.authService.controller;

import com.authService.entity.User;
import com.authService.payload.APIResponse;
import com.authService.payload.LoginDto;
import com.authService.payload.UserDto;
import com.authService.repository.UserRepository;
import com.authService.service.JWTService;
import com.authService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authManager;
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


    // http://localhost:8080/api/v1/auth/login
    @PostMapping("/login")
    public ResponseEntity<APIResponse<String>> Login(@RequestBody LoginDto dto){
        APIResponse<String> response = new APIResponse<>();

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        try{
            Authentication authenticate = authManager.authenticate(token);
            if(authenticate.isAuthenticated()){

                String jwtToken = jwtService.generateToken(dto.getUsername(),
                        authenticate.getAuthorities().iterator().next().getAuthority());


                response.setMessage("User Logged In Successfully!");
                response.setStatus(200);
                response.setData(jwtToken);
                return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
            }
        }catch (Exception e){
            response.setMessage("Invalid Credentials!");
            response.setStatus(500);
            response.setData("Failed");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
        }
        response.setMessage("Something went wrong!");
        response.setStatus(500);
        response.setData("Failed");
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }

    @GetMapping("/get-user")
    // http://localhost:8080/api/v1/auth/get-user?name=admin
    public User getUserName(@RequestParam String username){
        User byUsername = userRepository.findByUsername(username);
        return byUsername;
    }
}
