package com.project.user_authentication_backend.dto;


import lombok.Data;

@Data
public class SignInResponseDTO {

    private String signInStatus;
    private String accessToken;
    private String userName;
    private int userId;
    private boolean firstTimeLogin;

}
