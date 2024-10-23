package com.project.user_authentication_backend.exception.customExceptions;

public class UserAlreadyFoundException extends RuntimeException{
    public UserAlreadyFoundException(String message){ super(message); }
}
