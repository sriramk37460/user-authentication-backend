package com.project.user_authentication_backend.controllerInterface;

import com.project.user_authentication_backend.dto.ResponseDTO;
import com.project.user_authentication_backend.dto.UserEditDTO;
import com.project.user_authentication_backend.dto.UserLoginDTO;
import com.project.user_authentication_backend.dto.UserRegisterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
