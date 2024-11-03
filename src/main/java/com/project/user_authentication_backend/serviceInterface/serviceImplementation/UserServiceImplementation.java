package com.project.user_authentication_backend.serviceInterface.serviceImplementation;

import com.project.user_authentication_backend.config.CUOConfig;
import com.project.user_authentication_backend.dao.UserRepository;
import com.project.user_authentication_backend.dao.UserRequestRepository;
import com.project.user_authentication_backend.dto.*;
import com.project.user_authentication_backend.entity.Enum.RoleEnum;
import com.project.user_authentication_backend.entity.User;
import com.project.user_authentication_backend.entity.UserRequest;
import com.project.user_authentication_backend.exception.customExceptions.PasswordWrongException;
import com.project.user_authentication_backend.exception.customExceptions.UserAlreadyFoundException;
import com.project.user_authentication_backend.exception.customExceptions.UserNotFoundException;
import com.project.user_authentication_backend.exception.customExceptions.UserPermissionNotFoundException;
import com.project.user_authentication_backend.serviceInterface.UserService;
import jakarta.mail.MessagingException;
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
    private final UserRequestRepository userRequestRepository;
    private final CUOConfig cuoConfig;
    private final EmailService emailService;

    @Override
    public String createUser(UserRegisterDTO userRegisterDTO) {

        User userCheck = userRepository.getByEmail(userRegisterDTO.getEmail());
        if (userCheck != null) {
            throw new UserAlreadyFoundException("Already Registered");
        } else {
            User phoneNumberCheck=userRepository.getByPhoneNumber(userRegisterDTO.getPhoneNumber());
            if(phoneNumberCheck!=null){
                throw new UserAlreadyFoundException("Already Registered");
            }
            User user = new User();
            user.setUserName(userRegisterDTO.getUserName());
            user.setEmail(userRegisterDTO.getEmail());
            user.setPhoneNumber(userRegisterDTO.getPhoneNumber());
            user.setPassword(userRegisterDTO.getPassword());
            user.setRole(RoleEnum.ROLE_EMPLOYEE);
            user.setAdmin(false);
            user.setAccessGiven(false);
            userRepository.save(user);

            UserRequest userRequest = new UserRequest();
            UserToUserRequestDetails(user, userRequest);
            userRequestRepository.save(userRequest);

        }
        return "Register Successfully";
    }

    public static String randomUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @Override
    public String editUser(UserEditDTO userEditDTO) {
        User userCheck = userRepository.getByEmailWithUserId(userEditDTO.getEmail(), cuoConfig.getUserId());
        if (userCheck == null) {
            throw new UserNotFoundException("User Not Found With the Email");
        } else {
            UserRequest userRequest = userRequestRepository.getByEmail(userEditDTO.getEmail());
            if (userEditDTO.getUserName() != null) {
                if (userCheck.getCreatedBy().equals(userCheck.getUserName())) {
                    userCheck.setCreatedBy(userEditDTO.getUserName());
                }
                userRequest.setUserName(userEditDTO.getUserName());
                userCheck.setUserName(userEditDTO.getUserName());
            }
            if(userEditDTO.getPassword()!=null){
                userRequest.setPassword(userEditDTO.getPassword());
                userCheck.setPassword(userEditDTO.getPassword());
            }
            if (userEditDTO.getPhoneNumber() != null) {
                userRequest.setPhoneNumber(userEditDTO.getPhoneNumber());
                userCheck.setPhoneNumber(userEditDTO.getPhoneNumber());
            }
            userRequestRepository.save(userRequest);
            userRepository.save(userCheck);
        }
        return "User Detail Modified Successfully";
    }

    @Override
    public SignInResponseDTO userLogin(UserLoginDTO userLoginDTO) {
        User user = userRepository.getByEmailWithAccess(userLoginDTO.getEmail());
        SignInResponseDTO signInResponseDTO = new SignInResponseDTO();
        if (user != null) {

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
            userRequestRepository.deleteByUserId(userCheck.get().getUserId());
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
            userDTO.setRole(user.getRole());
            ListOfUser.add(userDTO);
        }
        return ListOfUser;
    }

    @Override
    public ProfileDTO getProfile() {
        Optional<User> user = userRepository.findById(cuoConfig.getUserId());
        if (user.isEmpty()) {
            throw new UserNotFoundException("User Not Found With the User Id");
        }
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUserName(user.get().getUserName());
        profileDTO.setRole(user.get().getRole());
        profileDTO.setEmail(user.get().getEmail());
        profileDTO.setPhoneNumber(user.get().getPhoneNumber());
        profileDTO.setPassword(user.get().getPassword());
        return profileDTO;
    }

    @Override
    public List<UserRequestDTO> getNotAccessUser() {
        List<UserRequest> UserList = userRequestRepository.findNotAccessUser();
        List<UserRequestDTO> ListOfUser = new ArrayList<>();
        for (UserRequest user : UserList) {
            UserRequestDTO userDTO = new UserRequestDTO();
            userDTO.setUserName(user.getUserName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setPassword(user.getPassword());
            userDTO.setAccessGiven(user.isAccessGiven());
            userDTO.setEmailRequest(user.isEmailRequest());
            userDTO.setPasswordRequest(user.isPasswordRequest());
            ListOfUser.add(userDTO);
        }
        return ListOfUser;
    }

//    @Override
//    public List<UserRequestDTO> getPasswordRequestUser() {
//        List<UserRequest> UserList = userRequestRepository.findPasswordRequestUser();
//        List<UserRequestDTO> ListOfUser = new ArrayList<>();
//        for (UserRequest user : UserList) {
//            UserRequestDTO userDTO = new UserRequestDTO();
//            userDTO.setUserName(user.getUserName());
//            userDTO.setEmail(user.getEmail());
//            userDTO.setPhoneNumber(user.getPhoneNumber());
//            userDTO.setPassword(user.getPassword());
//            userDTO.setAccessGiven(user.isAccessGiven());
//            userDTO.setEmailRequest(user.isEmailRequest());
//            userDTO.setPasswordRequest(user.isPasswordRequest());
//            ListOfUser.add(userDTO);
//        }
//        return ListOfUser;
//    }

//    @Override
//    public List<UserRequestDTO> getEmailRequestUser() {
//        List<UserRequest> UserList = userRequestRepository.findEmailRequestUser();
//        List<UserRequestDTO> ListOfUser = new ArrayList<>();
//        for (UserRequest user : UserList) {
//            UserRequestDTO userDTO = new UserRequestDTO();
//            userDTO.setUserName(user.getUserName());
//            userDTO.setEmail(user.getEmail());
//            userDTO.setPhoneNumber(user.getPhoneNumber());
//            userDTO.setPassword(user.getPassword());
//            userDTO.setAccessGiven(user.isAccessGiven());
//            userDTO.setEmailRequest(user.isEmailRequest());
//            userDTO.setPasswordRequest(user.isPasswordRequest());
//            ListOfUser.add(userDTO);
//        }
//        return ListOfUser;
//    }
    @Override
    public String resetPasswordRequest(PasswordRequestDTO passwordRequestDTO) {
        User userCheck = userRepository.getByEmail(passwordRequestDTO.getEmail());
        if (userCheck == null) {
            throw new UserNotFoundException("User Not Found With the Email");
        } else {

            if (userCheck.isAccessGiven()) {
                UserRequest userRequest = userRequestRepository.getByEmail(passwordRequestDTO.getEmail());
                userRequest.setAllowRequest(false);
                userRequest.setPasswordRequest(true);
                userRequestRepository.save(userRequest);
            }
        }
        return "Request Send Successfully";
    }

    @Override
    public String resetPassword(PasswordResetDTO passwordResetDTO) {
        User userCheck = userRepository.getByEmailWithUserId(passwordResetDTO.getEmail(), cuoConfig.getUserId());
        if (userCheck == null) {
            throw new UserNotFoundException("User Not Found With the Email");
        } else {
            if (userCheck.isAccessGiven() && userCheck.isPasswordRequest()) {
                UserRequest userRequest = userRequestRepository.getByEmail(passwordResetDTO.getEmail());
                if (userCheck.getPassword().equals(passwordResetDTO.getOldPassword())) {
                    userCheck.setPassword(passwordResetDTO.getNewPassword());
                    userCheck.setPasswordRequest(false);
                    userRequest.setPassword(passwordResetDTO.getNewPassword());
                    userRequest.setPasswordRequest(false);
                    userRepository.save(userCheck);
                    userRequestRepository.save(userRequest);
                    return "Password Update Successfully";
                } else {
                    throw new PasswordWrongException("Password is Wrong");
                }
            } else {
                throw new UserPermissionNotFoundException("Reset Password Permission is Not Given");
            }
        }
    }

    @Override
    public String accessPermission(List<EmailDTO> emailDTOS) throws MessagingException {
        for (EmailDTO user : emailDTOS) {
            User userCheck = userRepository.getByEmail(user.getEmail());
            if (userCheck != null) {

                userCheck.setAccessGiven(true);
                UserRequest userRequest = userRequestRepository.getByEmail(userCheck.getEmail());
                userRequest.setAccessGiven(true);
                userRequest.setAllowRequest(true);
                userRequestRepository.save(userRequest);
                userRepository.save(userCheck);
                String subject = "Access Given Notification";
                String body = "Hello " + userCheck.getUserName() + ",<br><br>" +
                        "Your request has been allowed. Please log in first to use the application.<br><br>" +
                        "If you have any questions or need further assistance, feel free to reach out.<br><br>" +
                        "Best regards,<br>" +
                        "Your Support Team";
                // Send email as HTML
                emailService.sendEmail(userCheck.getEmail(), subject, body);
            }
        }
        return "Access Permission Set Successfully";
    }

    @Override
    public String removeAccessPermission(EmailDTO emailDTO) throws MessagingException {
        return "";
    }
//    @Override
//    public String editEmailRequest(EmailRequestDTO emailRequestDTO) {
//        User userCheck = userRepository.getByEmailWithUserId(emailRequestDTO.getEmail(), cuoConfig.getUserId());
//        if (userCheck == null) {
//            throw new UserNotFoundException("User Not Found With the Email");
//        } else {
//            if (userCheck.isAccessGiven()) {
//                UserRequest userRequest = userRequestRepository.getByEmail(emailRequestDTO.getEmail());
//                userRequest.setAllowRequest(false);
//                userRequest.setEmailRequest(true);
//                userRequestRepository.save(userRequest);
//            }
//        }
//        return "Request Send Successfully";
//    }

//    @Override
//    public String editEmail(ChangeEmailDTO changeEmailDTO) {
//        User userCheck = userRepository.getByEmailWithUserId(changeEmailDTO.getEmail(), cuoConfig.getUserId());
//        if (userCheck == null) {
//            throw new UserNotFoundException("User Not Found With the Email");
//        } else {
//            if (userCheck.isAccessGiven() && userCheck.isEmailRequest() && userCheck.getEmail().equals(changeEmailDTO.getEmail())) {
//                UserRequest userRequest = userRequestRepository.getByEmail(changeEmailDTO.getEmail());
//                userCheck.setEmail(changeEmailDTO.getNewEmail());
//                userCheck.setEmailRequest(false);
//                userRequest.setEmail(changeEmailDTO.getNewEmail());
//                userRequest.setEmailRequest(false);
//                userRepository.save(userCheck);
//                userRequestRepository.save(userRequest);
//                return "Email Update Successfully";
//
//            } else {
//                throw new UserPermissionNotFoundException("Email Change Permission is Not Given");
//            }
//        }
//    }

    public static void UserToUserRequestDetails(User user, UserRequest userRequest) {
        userRequest.setUserId(user.getUserId());
        userRequest.setUserName(user.getUserName());
        userRequest.setEmail(user.getEmail());
        userRequest.setPhoneNumber(user.getPhoneNumber());
        userRequest.setPassword(user.getPassword());
        userRequest.setRole(user.getRole());
        userRequest.setPasswordRequest(user.isPasswordRequest());
        userRequest.setEmailRequest(user.isEmailRequest());
        userRequest.setAccessGiven(user.isAccessGiven());
        userRequest.setAllowRequest(false);
    }
}
