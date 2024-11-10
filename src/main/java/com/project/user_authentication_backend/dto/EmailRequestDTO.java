package com.project.user_authentication_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailRequestDTO {
    private String email;
    private boolean isEmailRequest;
}
