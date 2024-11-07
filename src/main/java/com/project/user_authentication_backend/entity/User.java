package com.project.user_authentication_backend.entity;



import com.project.user_authentication_backend.entity.Enum.RoleEnum;
import com.project.user_authentication_backend.entity.entityListeners.UserEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(UserEntityListener.class)
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id",unique = true)
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
    @Column(name = "is_admin")
    private boolean isAdmin;


    @Column(name = "token_generated_at")
    private LocalDateTime tokenGeneratedAt;

    // Store password reset token
    @Column(name = "reset_token")
    private String resetToken;

    // Store expiration time for the reset token
    @Column(name = "reset_token_expiration")
    private LocalDateTime resetTokenExpiration;

    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "modified_by")
    private String modifiedBy;
    @Column(name = "modified_at")
    private Timestamp modifiedAt;


}
