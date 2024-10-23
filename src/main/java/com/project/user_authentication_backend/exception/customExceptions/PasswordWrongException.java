package com.project.user_authentication_backend.exception.customExceptions;

public class PasswordWrongException extends RuntimeException {
    public PasswordWrongException(String message)
    {
        super(message);
    }
}
