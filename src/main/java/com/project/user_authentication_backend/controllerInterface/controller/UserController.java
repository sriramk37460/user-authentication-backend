package com.project.user_authentication_backend.controllerInterface.controller;

import com.project.user_authentication_backend.controllerInterface.UserInterface;
import com.project.user_authentication_backend.dto.*;
import com.project.user_authentication_backend.serviceInterface.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController implements UserInterface {
    private final UserService userService;

    @Override
    public ResponseEntity<ResponseDTO> createUser(UserRegisterDTO userRegisterDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO("Check The Data field",userService.createUser(userRegisterDTO)));
    }

    @Override
    public ResponseEntity<ResponseDTO> editUser(UserEditDTO userEditDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.editUser(userEditDTO)));
    }

    @Override
    public ResponseEntity<ResponseDTO> userLogin(UserLoginDTO userLoginDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.userLogin(userLoginDTO)));
    }

    @Override
    public ResponseEntity<ResponseDTO> deleteUser(int userId) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.deleteUser(userId)));
    }

    @Override
    public ResponseEntity<ResponseDTO> getProfile() {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> getNotAccessUser() {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> getPasswordRequestUser() {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> getEmailRequestUser() {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> resetPasswordRequest(PasswordRequestDTO passwordRequestDTO) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> resetPassword(PasswordResetDTO passwordResetDTO) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> editEmailRequest(EmailRequestDTO emailRequestDTO) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> editEmail(ChangeEmailDTO changeEmailDTO) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> resetPasswordPermission(List<UserEditDTO> userEditDTOs) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> editEmailPermission(List<UserEditDTO> userEditDTOs) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> accessPermission(List<UserEditDTO> userEditDTOs) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> removeAccessPermission(UserEditDTO userEditDTO) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> getUser() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.getAllUser()));
    }
}
