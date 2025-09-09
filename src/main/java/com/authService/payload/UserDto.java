package com.authService.payload;

import lombok.Data;

@Data
public class UserDto {

    private long id;
    private String username;
    private String email;
    private String password;
    private String role;
}
