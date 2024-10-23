package com.project.user_authentication_backend.exception.customExceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){ super(message); }
}
