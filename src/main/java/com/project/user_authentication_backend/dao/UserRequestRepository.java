package com.project.user_authentication_backend.dao;

import com.project.user_authentication_backend.entity.UserRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRequestRepository extends JpaRepository<UserRequest,Integer> {

    @Query(value = "SELECT * FROM user_request_table WHERE access_given=false AND allow_request=false",nativeQuery = true)
    List<UserRequest> findNotAccessUser();
    @Query(value = "SELECT * FROM user_request_table WHERE password_request=true AND allow_request=false",nativeQuery = true)
    List<UserRequest> findPasswordRequestUser();
    @Query(value = "SELECT * FROM user_request_table WHERE email_request=true AND allow_request=false",nativeQuery = true)
    List<UserRequest> findEmailRequestUser();
    @Query(value = "SELECT * FROM user_request_table WHERE email=?1",nativeQuery = true)
    UserRequest getByEmail(String email);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_request_table WHERE user_id = ?1", nativeQuery = true)
    void deleteByUserId(int userId);


//    @Query(value = "SELECT * FROM user_request_table WHERE isPhoneNumberRequest=true AND allowRequest=false",nativeQuery = true)
//    List<UserRequest> findPhoneNumberRequestUser();
}
