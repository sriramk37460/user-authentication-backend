package com.project.user_authentication_backend.serviceInterface;

import com.project.user_authentication_backend.dto.*;
import jakarta.mail.MessagingException;

import java.util.List;

public interface UserService {

    String createUser(UserRegisterDTO userRegisterDTO);
    String editUser(UserEditDTO userEditDTO);
    String deleteUser(int userId);
    SignInResponseDTO userLogin(UserLoginDTO userLoginDTO);
    List<UserDTO> getAllUser();
    ProfileDTO getProfile();

    //need to implement
    List<UserRequestDTO>  getNotAccessUser();
    //    List<UserRequestDTO>  getPasswordRequestUser();
//    List<UserRequestDTO>  getPhoneNumberRequestUser();
//    List<UserRequestDTO>  getEmailRequestUser();
//    String editPassword(FirstTimeLoginDTO firstTimeLoginDTO);
    String resetPasswordRequest(PasswordRequestDTO passwordRequestDTO) throws MessagingException;
    String resetPassword(PasswordResetDTO passwordResetDTO);
    //    String editPhoneNumberRequest(PhoneNumberRequestDTO phoneNumberRequestDTO);
//    String editPhoneNumber(ChangePhoneNumberDTO changePhoneNumberDTO);
//    String editEmailRequest(EmailRequestDTO emailRequestDTO);
//    String editEmail(ChangeEmailDTO changeEmailDTO);
//    String resetPasswordPermission(List<UserEditDTO> userEditDTOs) throws MessagingException;
//    String editPhoneNumberPermission(UserEditDTO userEditDTO);
//    String editEmailPermission(List<UserEditDTO> userEditDTOs) throws MessagingException;
    String accessPermission(List<EmailDTO> emailDTOS) throws MessagingException;
    String removeAccessPermission(EmailDTO emailDTO) throws MessagingException;
}
