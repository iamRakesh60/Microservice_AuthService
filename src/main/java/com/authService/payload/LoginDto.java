package com.authService.payload;

import lombok.Data;

@Data
public class LoginDto {

    private String username;
    private String password;
}
