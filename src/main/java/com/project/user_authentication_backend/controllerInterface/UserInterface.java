package com.project.user_authentication_backend.controllerInterface;

import com.project.user_authentication_backend.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/authentication/api/v1")
public interface UserInterface {

    @PostMapping("/public/register")
    ResponseEntity<ResponseDTO> createUser(@RequestBody UserRegisterDTO userRegisterDTO);
    @GetMapping("/admin/getUserList")
    ResponseEntity<ResponseDTO> getUser();
    @PutMapping("/edit")
    ResponseEntity<ResponseDTO> editUser(@RequestBody UserEditDTO userEditDTO);
    @PostMapping("/public/login")
    ResponseEntity<ResponseDTO> userLogin(@RequestBody UserLoginDTO userLoginDTO);
    @DeleteMapping("/admin/remove/{userId}")
    ResponseEntity<ResponseDTO> deleteUser(@PathVariable int userId);
    @GetMapping("/getProfile")
    ResponseEntity<ResponseDTO> getProfile();

    //need to implement
    @GetMapping("/admin/getAccessNotAllowedUserList")
    ResponseEntity<ResponseDTO> getNotAccessUser();
    @GetMapping("/admin/getPasswordRequestUserList")
    ResponseEntity<ResponseDTO> getPasswordRequestUser();
    //    @GetMapping("/admin/getPhoneNumberRequestUserList")
//    ResponseEntity<ResponseDTO> getPhoneNumberRequestUser();
    @GetMapping("/admin/getEmailRequestUserList")
    ResponseEntity<ResponseDTO> getEmailRequestUser();
    //    @PutMapping("/updatePassword")
//    ResponseEntity<ResponseDTO> editPassword(@RequestBody FirstTimeLoginDTO firstTimeLoginDTO);
    @PostMapping("/public/resetPasswordRequest")
    ResponseEntity<ResponseDTO> resetPasswordRequest(@RequestBody PasswordRequestDTO passwordRequestDTO);
    @PutMapping("/resetPassword")
    ResponseEntity<ResponseDTO> resetPassword(@RequestBody PasswordResetDTO passwordResetDTO);
    //    @PutMapping("/phoneNumberChangeRequest")
//    ResponseEntity<ResponseDTO> editPhoneNumberRequest(@RequestBody PhoneNumberRequestDTO phoneNumberRequestDTO);
//    @PutMapping("/changePhoneNumber")
//    ResponseEntity<ResponseDTO> editPhoneNumber(@RequestBody ChangePhoneNumberDTO changePhoneNumberDTO);
    @PostMapping("/emailChangeRequest")
    ResponseEntity<ResponseDTO> editEmailRequest(@RequestBody EmailRequestDTO emailRequestDTO);
    @PutMapping("/changeEmail")
    ResponseEntity<ResponseDTO> editEmail(@RequestBody ChangeEmailDTO changeEmailDTO);
    @PutMapping("/admin/allowResetPassword")
    ResponseEntity<ResponseDTO> resetPasswordPermission(@RequestBody List<UserEditDTO> userEditDTOs);
    //    @PutMapping("/admin/allowChangePhoneNumber")
//    ResponseEntity<ResponseDTO> editPhoneNumberPermission(@RequestBody UserEditDTO userEditDTO);
    @PutMapping("/admin/allowChangeEmail")
    ResponseEntity<ResponseDTO> editEmailPermission(@RequestBody List<UserEditDTO> userEditDTOs);
    @PutMapping("/admin/allowAccess")
    ResponseEntity<ResponseDTO> accessPermission(@RequestBody List<UserEditDTO> userEditDTOs);
    @PutMapping("/admin/removeAccess")
    ResponseEntity<ResponseDTO> removeAccessPermission(@RequestBody UserEditDTO userEditDTO);
}
