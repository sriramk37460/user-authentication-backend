package com.project.user_authentication_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String userName;
    private String email;
    private String phoneNumber;
    private String password;
//    private boolean isFirstTimeLogin;
    private boolean isEmailRequest;
    private boolean isPasswordRequest;
    private boolean accessGiven;
}

























