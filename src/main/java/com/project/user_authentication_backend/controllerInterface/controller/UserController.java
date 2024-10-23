package com.project.user_authentication_backend.controllerInterface.controller;

import com.project.user_authentication_backend.controllerInterface.UserInterface;
import com.project.user_authentication_backend.dto.ResponseDTO;
import com.project.user_authentication_backend.dto.UserEditDTO;
import com.project.user_authentication_backend.dto.UserLoginDTO;
import com.project.user_authentication_backend.dto.UserRegisterDTO;
import com.project.user_authentication_backend.serviceInterface.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ResponseDTO> getUser() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.getAllUser()));
    }
}
