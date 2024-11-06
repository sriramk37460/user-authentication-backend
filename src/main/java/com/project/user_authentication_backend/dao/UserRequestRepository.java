package com.project.user_authentication_backend.dao;

import com.project.user_authentication_backend.entity.UserRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest,Integer> {

    @Query(value = "SELECT * FROM user_request_table WHERE access_given=false AND allow_request=false",nativeQuery = true)
    List<UserRequest> findNotAccessUser();
    @Query(value = "SELECT * FROM user_request_table WHERE email=?1",nativeQuery = true)
    UserRequest getByEmail(String email);
    @Query(value = "SELECT * FROM user_request_table WHERE user_id=?1",nativeQuery = true)
    UserRequest getByUserId(int userId);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_request_table WHERE user_id = ?1", nativeQuery = true)
    void deleteByUserId(int userId);

}