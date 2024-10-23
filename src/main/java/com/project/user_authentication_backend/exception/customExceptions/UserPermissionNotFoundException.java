package com.project.user_authentication_backend.exception.customExceptions;

public class UserPermissionNotFoundException extends RuntimeException {
    public UserPermissionNotFoundException(String message) {
        super(message);
    }
}
