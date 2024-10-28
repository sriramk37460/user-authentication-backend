package com.project.user_authentication_backend.entity;

import com.project.user_authentication_backend.entity.Enum.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_request_table")
public class UserRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_request_id",unique = true)
    private int userRequestId;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "email",unique = true)
    private String email;
    @Column(name = "phone_number",unique = true)
    private String phoneNumber;
    @Column(name = "password")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleEnum role;
    //    @Column(name = "first_time_login")
//    private boolean isFirstTimeLogin;
    @Column(name = "password_request")
    private boolean isPasswordRequest;
    @Column(name = "email_request")
    private boolean isEmailRequest;
    @Column(name = "access_given")
    private boolean accessGiven;
    @Column(name = "allow_request")
    private boolean allowRequest;

}

