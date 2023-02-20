package com.shiro.entity.Login;

import lombok.Data;

@Data
public class LoginRequest {
    private String userName;
    private String password;
}