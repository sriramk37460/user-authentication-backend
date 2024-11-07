package com.project.user_authentication_backend.dto;

import com.project.user_authentication_backend.entity.Enum.RoleEnum;
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
    private RoleEnum role;
    private String password;
    private boolean isEmailRequest;
    private boolean accessGiven;
}

























