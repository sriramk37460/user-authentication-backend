package com.project.user_authentication_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeEmailDTO {
    private String userName;
    private String phoneNumber;
    private String email;
    private String newEmail;
}