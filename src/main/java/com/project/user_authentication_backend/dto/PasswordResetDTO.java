package com.project.user_authentication_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetDTO {
    private String userName;
    private String email;
    private String phoneNumber;
    private String oldPassword;
    private String newPassword;
}