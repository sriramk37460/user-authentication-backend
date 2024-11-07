package com.project.user_authentication_backend.serviceInterface.serviceImplementation;

import com.project.user_authentication_backend.config.CUOConfig;
import com.project.user_authentication_backend.config.JwtTokenProvider;
import com.project.user_authentication_backend.controllerInterface.controller.StringGeneratorForPassword;
import com.project.user_authentication_backend.dao.UserRepository;
import com.project.user_authentication_backend.dao.UserRequestRepository;
import com.project.user_authentication_backend.dto.*;
import com.project.user_authentication_backend.entity.Enum.RoleEnum;
import com.project.user_authentication_backend.entity.User;
import com.project.user_authentication_backend.entity.UserRequest;
import com.project.user_authentication_backend.exception.customExceptions.*;
import com.project.user_authentication_backend.serviceInterface.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final UserRequestRepository userRequestRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CUOConfig cuoConfig;
    private final StringGeneratorForPassword stringGeneratorForPassword;
    private final EmailService emailService;

    @Override
    public String createUser(UserRegisterDTO userRegisterDTO) {

        User userCheck = userRepository.getByEmail(userRegisterDTO.getEmail());
        if (userCheck != null) {
            throw new UserAlreadyFoundException("Already Registered");
        } else {
            User phoneNumberCheck = userRepository.getByPhoneNumber(userRegisterDTO.getPhoneNumber());
            if (phoneNumberCheck != null) {
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
        User userCheck = userRepository.getByUserId(cuoConfig.getUserId());
        if (userCheck == null) {
            throw new UserNotFoundException("User Not Found With the Email");
        } else {
            UserRequest userRequest = userRequestRepository.getByUserId(cuoConfig.getUserId());
            if (userEditDTO.getUserName() != null) {
                if (userCheck.getCreatedBy().equals(userCheck.getUserName())) {
                    userCheck.setCreatedBy(userEditDTO.getUserName());
                }
                userRequest.setUserName(userEditDTO.getUserName());
                userCheck.setUserName(userEditDTO.getUserName());
            }
            if (userEditDTO.getPassword() != null) {
                userRequest.setPassword(userEditDTO.getPassword());
                userCheck.setPassword(userEditDTO.getPassword());
            }
            if (userEditDTO.getPhoneNumber() != null) {
                userRequest.setPhoneNumber(userEditDTO.getPhoneNumber());
                userCheck.setPhoneNumber(userEditDTO.getPhoneNumber());
            }
            if (userEditDTO.getEmail() != null) {
                userRequest.setEmail(userEditDTO.getEmail());
                userCheck.setEmail(userEditDTO.getEmail());
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
                String accessToken = jwtTokenProvider.createToken(userID, userName, role);
                System.out.println("Generated Access Token: " + accessToken);
                signInResponseDTO.setUserId(userID);
                signInResponseDTO.setUserName(userName);
                signInResponseDTO.setAccessToken(accessToken);
            } else {
                throw new PasswordWrongException("Password is Wrong");
            }
        } else {
            throw new UserNotFoundException("User Not Found, Check the Email");
        }
        return signInResponseDTO;
    }


    @Transactional
    public String deleteUser(int userId) {
        Optional<User> userCheck = userRepository.findById(userId);

        if (userCheck.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        // Proceed to delete the user and related requests
        userRequestRepository.deleteByUserId(userId); // Delete related requests first
        userRepository.deleteById(userId); // Then delete the user

        return ("User removed successfully");
    }

    @Override
    public List<UserDTO> getAllUser() {
        List<User> UserList = userRepository.findAllUser();
        List<UserDTO> ListOfUser = new ArrayList<>();
        for (User user : UserList) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
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

    //need to implement
    @Override
    public List<UserRequestDTO> getNotAccessUser() {
        List<UserRequest> UserList = userRequestRepository.findNotAccessUser();
        List<UserRequestDTO> ListOfUser = new ArrayList<>();
        for (UserRequest user : UserList) {
            UserRequestDTO userDTO = new UserRequestDTO();
            userDTO.setUserName(user.getUserName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setRole(user.getRole());
            userDTO.setPassword(user.getPassword());
            userDTO.setAccessGiven(user.isAccessGiven());
            userDTO.setEmailRequest(user.isEmailRequest());
//            userDTO.setPasswordRequest(user.isPasswordRequest());
            ListOfUser.add(userDTO);
        }
        return ListOfUser;
    }
//
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
////            userDTO.setPasswordRequest(user.isPasswordRequest());
//            ListOfUser.add(userDTO);
//        }
//        return ListOfUser;
//    }

    //    @Override
//    public List<UserRequestDTO>  getPhoneNumberRequestUser() {
//        List<UserRequest> UserList = userRequestRepository.findPhoneNumberRequestUser();
//        List<UserRequestDTO> ListOfUser = new ArrayList<>();
//        for(UserRequest user: UserList){
//            UserRequestDTO userDTO=new UserRequestDTO();
//            userDTO.setUserName(user.getUserName());
//            userDTO.setEmail(user.getEmail());
//            userDTO.setPhoneNumber(user.getPhoneNumber());
//            userDTO.setPassword(user.getPassword());
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
////            userDTO.setPasswordRequest(user.isPasswordRequest());
//            ListOfUser.add(userDTO);
//        }
//        return ListOfUser;
//    }


//    @Override
//    public String editPassword(FirstTimeLoginDTO firstTimeLoginDTO) {
//        User userCheck = userRepository.getByEmail(firstTimeLoginDTO.getEmail());
//        if(userCheck==null){
//            throw new UserNotFoundException("User Not Found With the Email");
//        }
//        else{
//            if(userCheck.isFirstTimeLogin()) {
//                if (userCheck.getPassword().equals(firstTimeLoginDTO.getOldPassword())) {
//                    userCheck.setFirstTimeLogin(false);
//                    userCheck.setPassword(firstTimeLoginDTO.getNewPassword());
//                    userRepository.save(userCheck);
//                    return "Password Update Successfully";
//                } else {
//                    throw new PasswordWrongException("Password is Wrong");
//                }
//            }
//            else{
//                throw new UserPermissionNotFoundException("Permission is Not Given");
//            }
//        }
//    }

//    @Override
//    public String resetPasswordRequest(PasswordRequestDTO passwordRequestDTO) {
//        User userCheck = userRepository.getByEmail(passwordRequestDTO.getEmail());
//        if (userCheck == null) {
//            throw new UserNotFoundException("User Not Found With the Email");
//        } else {
//
//            if (userCheck.isAccessGiven()) {
//                UserRequest userRequest = userRequestRepository.getByEmail(passwordRequestDTO.getEmail());
//                userRequest.setAllowRequest(false);
//                userRequest.setPasswordRequest(true);
//                userRequestRepository.save(userRequest);
//            }
//        }
//        return "Request Send Successfully";
//    }
//
//    @Override
//    public String resetPassword(PasswordResetDTO passwordResetDTO) {
//        User userCheck = userRepository.getByEmailWithUserId(passwordResetDTO.getEmail(), cuoConfig.getUserId());
//        if (userCheck == null) {
//            throw new UserNotFoundException("User Not Found With the Email");
//        } else {
//            if (userCheck.isAccessGiven() && userCheck.isPasswordRequest()) {
//                UserRequest userRequest = userRequestRepository.getByEmail(passwordResetDTO.getEmail());
//                if (userCheck.getPassword().equals(passwordResetDTO.getOldPassword())) {
//                    userCheck.setPassword(passwordResetDTO.getNewPassword());
//                    userCheck.setPasswordRequest(false);
//                    userRequest.setPassword(passwordResetDTO.getNewPassword());
//                    userRequest.setPasswordRequest(false);
//                    userRepository.save(userCheck);
//                    userRequestRepository.save(userRequest);
//                    return "Password Update Successfully";
//                } else {
//                    throw new PasswordWrongException("Password is Wrong");
//                }
//            } else {
//                throw new UserPermissionNotFoundException("Reset Password Permission is Not Given");
//            }
//        }
//    }

    @Transactional
    public String resetPasswordRequest(PasswordRequestDTO passwordRequestDTO) throws MessagingException {
        User userCheck = userRepository.getByEmail(passwordRequestDTO.getEmail());
        if (userCheck == null) {
            throw new UserNotFoundException("User Not Found With the Email");
        }

        if (userCheck.isAccessGiven()) {

            // Generate a token
            String token = UUID.randomUUID().toString();
            userCheck.setResetToken(token);
            userCheck.setResetTokenExpiration(LocalDateTime.now().plusMinutes(30)); // Token valid for 1 hour
            userCheck.setTokenGeneratedAt(LocalDateTime.now()); // Store when the token was generated
            userRepository.save(userCheck);

            // Send email
            String subject = "Password Reset Request";
            String body = "Hello " + userCheck.getUserName() + ",<br><br>" +
                    "Click the link below to reset your password:<br>" +
                    "<a href='http://127.0.0.1:5500/password_reset.html?token=" + token + "'>Reset Password</a><br><br>" +
                    "If you did not request a password reset, please ignore this email.";


            emailService.sendEmail(passwordRequestDTO.getEmail(), subject, body);
        }

        return "Request Sent Successfully";
    }

    @Transactional
    public String resetPassword(PasswordResetDTO passwordResetDTO) {
        User user = userRepository.findByResetToken(passwordResetDTO.getToken());
        if (user == null || user.getResetTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new TokenInvalidException("Invalid or Expired Token");
        }

        user.setPassword(passwordResetDTO.getNewPassword()); // You should encode the password before saving
        user.setResetToken(null); // Clear the token after successful reset
        user.setResetTokenExpiration(null); // Clear the expiration
        user.setPasswordRequest(false); // Reset password request status
        userRepository.save(user);

        return "Password Reset Successfully";
    }

//    @Override
//    public String editPhoneNumberRequest(PhoneNumberRequestDTO phoneNumberRequestDTO) {
//        return "";
//    }
//
//    @Override
//    public String editPhoneNumber(ChangePhoneNumberDTO changePhoneNumberDTO) {
//        return "";
//    }

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
//
//    @Override
//    public String editEmail(ChangeEmailDTO changeEmailDTO) {
//        User userCheck = userRepository.getByEmailWithUserId(changeEmailDTO.getEmail(), cuoConfig.getUserId());
//        if (userCheck == null) {
//            throw new UserNotFoundException("User Not Found With the Email");
//        } else {
//            if (userCheck.isAccessGiven() && userCheck.isEmailRequest() && userCheck.getEmail().equals(changeEmailDTO.getEmail())) {
//                    UserRequest userRequest = userRequestRepository.getByEmail(changeEmailDTO.getEmail());
//                    userCheck.setEmail(changeEmailDTO.getNewEmail());
//                    userCheck.setEmailRequest(false);
//                    userRequest.setEmail(changeEmailDTO.getNewEmail());
//                    userRequest.setEmailRequest(false);
//                    userRepository.save(userCheck);
//                    userRequestRepository.save(userRequest);
//                return "Email Update Successfully";
//
//            } else {
//                throw new UserPermissionNotFoundException("Email Change Permission is Not Given");
//            }
//        }
//    }

//    @Override
//    public String resetPasswordPermission(List<UserEditDTO> userEditDTOs) throws MessagingException {
//        for (UserEditDTO user : userEditDTOs) {
//            User userCheck = userRepository.getByEmail(user.getEmail());
//            if (userCheck != null) {
//
//                userCheck.setPasswordRequest(true);
//                String str = stringGeneratorForPassword.generateRandomString(8);
//                String defaultPassword = userCheck.getPhoneNumber() + str;
//                userCheck.setPassword(defaultPassword);
//                UserRequest userRequest = userRequestRepository.getByEmail(userCheck.getEmail());
//                userRequest.setAllowRequest(true);
//                userRequest.setPassword(defaultPassword);
//                userRequestRepository.save(userRequest);
//                userRepository.save(userCheck);
//                String subject = "Password Reset Notification";
//                String body = "Hello " + userCheck.getUserName() + ",<br><br>" +
//                        "We have reset your password. Your new password is: <strong>" + defaultPassword + "</strong>.<br>" +
//                        "This is your old password now. Copy this password and apply it in the reset password link.<br><br>" +
//                        "Please use the following link to set a new password:<br>" +
//                        "<a href='http://127.0.0.1:5500/password_reset.html'>Click here to reset your password</a><br><br>" +
//                        "Once you've set a new password, you can use it to log in.";
//
//                emailService.sendEmail(userCheck.getEmail(), subject, body);
//
//            }
//        }
//        return "Reset Password Permission Set Successfully";
//    }

    //    @Override
//    public String editPhoneNumberPermission(UserEditDTO userEditDTO) {
//        return "";
//    }
//    @Override
//    public String editEmailPermission(List<UserEditDTO> userEditDTOs) throws MessagingException {
//        for (UserEditDTO user : userEditDTOs) {
//            User userCheck = userRepository.getByEmail(user.getEmail());
//            if (userCheck != null) {
//
//                userCheck.setEmailRequest(true);
//                UserRequest userRequest = userRequestRepository.getByEmail(userCheck.getEmail());
//                userRequest.setAllowRequest(true);
//                userRequestRepository.save(userRequest);
//                userRepository.save(userCheck);
//                String subject = "Email Change Notification";
//                String body = "Hello " + userCheck.getUserName() + ",<br><br>" +
//                        "We accept your email change request. Click the link below to set your new email:<br>" +
//                        "<a href='http://127.0.0.1:5500/change_email.html'>Click here to change your email</a><br><br>" +
//                        "Once you've set a new email, you can use it to log in.<br><br>" +
//                        "If you have any questions or need further assistance, feel free to contact us.<br><br>" +
//                        "Best regards,<br>" +
//                        "Your Support Team";
//
//                // Send email as HTML
//                emailService.sendEmail(userCheck.getEmail(), subject, body);
//            }
//        }
//        return "Change Email Permission Set Successfully";
//    }

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
        User userCheck = userRepository.getByEmail(emailDTO.getEmail());
        if (userCheck == null) {
            throw new UserNotFoundException("User Not Found With the Email");
        } else {
            String supportEmail = "ramsri37460@gmail.com";
            userCheck.setAccessGiven(false);
            UserRequest userRequest = userRequestRepository.getByEmail(userCheck.getEmail());
            userRequest.setAccessGiven(false);
            userRequest.setAllowRequest(false);
            userRequestRepository.save(userRequest);
            userRepository.save(userCheck);
            String subject = "Decline Access Notification";
            String body = "Hello " + userCheck.getUserName() + ",<br><br>" +
                    "We regret to inform you that your access has been declined. If you have any questions or need further assistance, please email our support team at <a href='mailto:" + supportEmail + "'>" + supportEmail + "</a>.<br><br>" +
                    "Thank you for your understanding.<br><br>" +
                    "Best regards,<br>" +
                    "Your Support Team";
            emailService.sendEmail(userCheck.getEmail(), subject, body);

        }
        return "Remove Access Permission Successfully";
    }

    public static void UserToUserRequestDetails(User user, UserRequest userRequest) {
        userRequest.setUserId(user.getUserId());
        userRequest.setUserName(user.getUserName());
        userRequest.setEmail(user.getEmail());
        userRequest.setPhoneNumber(user.getPhoneNumber());
        userRequest.setPassword(user.getPassword());
        userRequest.setRole(user.getRole());
//        userRequest.setPasswordRequest(user.isPasswordRequest());
        userRequest.setEmailRequest(user.isEmailRequest());
        userRequest.setAccessGiven(user.isAccessGiven());
        userRequest.setAllowRequest(false);
    }
}