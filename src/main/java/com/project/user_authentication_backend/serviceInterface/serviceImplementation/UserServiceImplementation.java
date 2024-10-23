package com.project.user_authentication_backend.serviceInterface.serviceImplementation;

import com.project.user_authentication_backend.dao.UserRepository;
import com.project.user_authentication_backend.dto.*;
import com.project.user_authentication_backend.entity.Enum.RoleEnum;
import com.project.user_authentication_backend.entity.User;
import com.project.user_authentication_backend.exception.customExceptions.PasswordWrongException;
import com.project.user_authentication_backend.exception.customExceptions.UserAlreadyFoundException;
import com.project.user_authentication_backend.exception.customExceptions.UserNotFoundException;
import com.project.user_authentication_backend.serviceInterface.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    @Override
    public String createUser(UserRegisterDTO userRegisterDTO) {

        User userCheck = userRepository.getByEmail(userRegisterDTO.getEmail());
        if (userCheck != null) {
            throw new UserAlreadyFoundException("Already Registered");
        } else {

            User user = new User();
            // user.setUserId(randomUUID());
            user.setUserName(userRegisterDTO.getUserName());
            user.setEmail(userRegisterDTO.getEmail());
            user.setPhoneNumber(userRegisterDTO.getPhoneNumber());
            user.setPassword(userRegisterDTO.getPassword());
            user.setRole(RoleEnum.ROLE_EMPLOYEE);
//            user.setFirstTimeLogin(false);
            user.setAdmin(false);
            user.setAccessGiven(false);
            userRepository.save(user);


        }
        return "Register Successfully";
    }

    public static String randomUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @Override
    public String editUser(UserEditDTO userEditDTO) {
        User userCheck = userRepository.getByEmail(userEditDTO.getEmail());
        if (userCheck == null) {
            throw new UserNotFoundException("User Not Found With the Email");
        } else {
            if (userEditDTO.getUserName() != null) {
                if (userCheck.getCreatedBy().equals(userCheck.getUserName())) {
                    userCheck.setCreatedBy(userEditDTO.getUserName());
                }
                userCheck.setUserName(userEditDTO.getUserName());

            }
            if (userEditDTO.getPhoneNumber() != null) {
                userCheck.setPhoneNumber(userEditDTO.getPhoneNumber());

            }
            userRepository.save(userCheck);
        }
        return "User Detail Modified Successfully";
    }

    @Override
    public SignInResponseDTO userLogin(UserLoginDTO userLoginDTO) {
        User user = userRepository.getByEmailWithAccess(userLoginDTO.getEmail());
        SignInResponseDTO signInResponseDTO = new SignInResponseDTO();
        if (user != null) {
//            if (user.isFirstTimeLogin()) {
//                if (user.getPassword().equals(userLoginDTO.getPassword())) {
//                    signInResponseDTO.setFirstTimeLogin(true);
//                    signInResponseDTO.setUserName(user.getUserName());
//                    return signInResponseDTO;
//                } else {
//                    throw new PasswordWrongException("Password is Wrong");
//                }
//            }

            if (user.getPassword().equals(userLoginDTO.getPassword())) {
                int userID = user.getUserId();
                signInResponseDTO.setSignInStatus("Sign-In Successful");
                String userName = user.getUserName();
                String role = String.valueOf(user.getRole());
                System.out.println("Creating token for user ID: " + userID + ", Role: " + role);
                signInResponseDTO.setUserId(userID);
                signInResponseDTO.setUserName(userName);
            } else {
                throw new PasswordWrongException("Password is Wrong");
            }
        } else {
            throw new UserNotFoundException("User Not Found, Check the Email");
        }
        return signInResponseDTO;
    }


    @Override
    public String deleteUser(int userId) {
        Optional<User> userCheck = userRepository.findById(userId);
        if (userCheck.isEmpty()) {
            throw new UserNotFoundException("User Not Found With the Email");
        } else {
            userRepository.deleteById(userCheck.get().getUserId());
        }
        return "User Removed Successfully";
    }

    @Override
    public List<UserDTO> getAllUser() {
        List<User> UserList = userRepository.findAllUser();
        List<UserDTO> ListOfUser = new ArrayList<>();
        for (User user : UserList) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserName(user.getUserName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setPassword(user.getPassword());
            ListOfUser.add(userDTO);
        }
        return ListOfUser;
    }
}
