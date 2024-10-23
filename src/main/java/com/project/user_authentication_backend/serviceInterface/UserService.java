package com.project.user_authentication_backend.serviceInterface;

import com.project.user_authentication_backend.dto.*;

import java.util.List;

public interface UserService {
    String createUser(UserRegisterDTO userRegisterDTO);
    String editUser(UserEditDTO userEditDTO);
    String deleteUser(int userId);
    SignInResponseDTO userLogin(UserLoginDTO userLoginDTO);
    List<UserDTO> getAllUser();
}
