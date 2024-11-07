package com.project.user_authentication_backend.exception.customExceptions;

public class TokenInvalidException extends RuntimeException{
    public TokenInvalidException(String message){ super(message); }
}
