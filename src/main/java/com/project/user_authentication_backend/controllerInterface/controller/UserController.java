package com.project.user_authentication_backend.controllerInterface.controller;

import com.project.user_authentication_backend.controllerInterface.UserInterface;
import com.project.user_authentication_backend.dao.UserRepository;
import com.project.user_authentication_backend.dto.*;
import com.project.user_authentication_backend.serviceInterface.UserService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5500") // Allow this origin for this controller
public class UserController implements UserInterface {

    private final UserService userService;
    private final UserRepository userRepository;

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
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.getProfile()));
    }

    @Override
    public ResponseEntity<ResponseDTO> getUser() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.getAllUser()));
    }

    //need to implement
    @Override
    public ResponseEntity<ResponseDTO> getNotAccessUser() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.getNotAccessUser()));
    }

//    @Override
//    public ResponseEntity<ResponseDTO> getPasswordRequestUser() {
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.getPasswordRequestUser()));
//    }

//    @Override
//    public ResponseEntity<ResponseDTO> getPhoneNumberRequestUser() {
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.getPhoneNumberRequestUser()));
//    }

//    @Override
//    public ResponseEntity<ResponseDTO> getEmailRequestUser() {
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.getEmailRequestUser()));
//    }
//first time login

//    @Override
//    public ResponseEntity<ResponseDTO> editPassword(FirstTimeLoginDTO firstTimeLoginDTO) {
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.editPassword(firstTimeLoginDTO)));
//    }

    @Override
    public ResponseEntity<ResponseDTO> resetPasswordRequest(PasswordRequestDTO passwordRequestDTO) throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.resetPasswordRequest(passwordRequestDTO)));
    }

    @Override
    public ResponseEntity<ResponseDTO> resetPassword(PasswordResetDTO passwordResetDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.resetPassword(passwordResetDTO)));
    }

//    @Override
//    public ResponseEntity<ResponseDTO> editPhoneNumberRequest(PhoneNumberRequestDTO phoneNumberRequestDTO) {
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.editPhoneNumberRequest(phoneNumberRequestDTO)));
//    }
//
//    @Override
//    public ResponseEntity<ResponseDTO> editPhoneNumber(ChangePhoneNumberDTO changePhoneNumberDTO) {
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.editPhoneNumber(changePhoneNumberDTO)));
//    }
//   @Override
//   public ResponseEntity<ResponseDTO> editEmailRequest(EmailRequestDTO emailRequestDTO) {
//      return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.editEmailRequest(emailRequestDTO)));
//   }
//
//    @Override
//    public ResponseEntity<ResponseDTO> editEmail(ChangeEmailDTO changeEmailDTO) {
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.editEmail(changeEmailDTO)));
//    }

//    @Override
//    public ResponseEntity<ResponseDTO> resetPasswordPermission(List<UserEditDTO> userEditDTOs) throws MessagingException {
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.resetPasswordPermission(userEditDTOs)));
//    }

//    @Override
//    public ResponseEntity<ResponseDTO> editPhoneNumberPermission(UserEditDTO userEditDTO) {
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.editPhoneNumberPermission(userEditDTO)));
//    }
//   @Override
//   public ResponseEntity<ResponseDTO> editEmailPermission(List<UserEditDTO> userEditDTOs) throws MessagingException {
//      return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.editEmailPermission(userEditDTOs)));
//   }

    @Override
    public ResponseEntity<ResponseDTO> accessPermission(List<EmailDTO> emailDTOS) throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.accessPermission(emailDTOS)));
    }

    @Override
    public ResponseEntity<ResponseDTO> removeAccessPermission(EmailDTO emailDTO) throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("Check The Data field",userService.removeAccessPermission(emailDTO)));
    }


}
