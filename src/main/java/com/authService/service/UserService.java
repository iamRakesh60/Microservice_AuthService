package com.authService.service;

import com.authService.entity.User;
import com.authService.payload.APIResponse;
import com.authService.payload.UserDto;
import com.authService.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public APIResponse<String> register(UserDto userDto){
        // API Response obj
        APIResponse<String> apiResponse = new APIResponse<>();

        // check username
        if(userRepository.existsByUsername(userDto.getUsername())){
            apiResponse.setMessage("Username is already taken!");
            apiResponse.setStatus(500);
            apiResponse.setData("Failed");
            return apiResponse;
        }

        // check email
        if(userRepository.existsByEmail(userDto.getEmail())){
            apiResponse.setMessage("Email is already taken!");
            apiResponse.setStatus(500);
            apiResponse.setData("Failed");
            return apiResponse;
        }

        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());

        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setPassword(encryptedPassword);
        User savedUser = userRepository.save(user);

        if(savedUser == null){
            apiResponse.setMessage("User Registration Failed!");
            apiResponse.setStatus(500);
            apiResponse.setData("Please try again!");
            return apiResponse;
        }
        apiResponse.setMessage("User Registered Successfully!");
        apiResponse.setStatus(200);
        apiResponse.setData("Success");
        return apiResponse;
    }
}
