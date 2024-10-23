package com.project.user_authentication_backend.dao;


import com.project.user_authentication_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Integer> {


    @Query(value = "SELECT * FROM user_table WHERE email=?1",nativeQuery = true)
    User getByEmail(String email);
    @Query(value = "SELECT * FROM user_table WHERE email=?1 AND access_given=true",nativeQuery = true)
    User getByEmailWithAccess(String email);

    @Query(value = "SELECT * FROM user_table WHERE user_name = :username",nativeQuery = true)
    User findByUserName(@Param("username")String userName);

    @Query(value = "SELECT * FROM user_table WHERE access_given=true ",nativeQuery = true)
    List<User> findAllUser();
    @Query(value = "SELECT * FROM user_table WHERE user_id=?1 ",nativeQuery = true)
    Optional<User> findById(int userId);
}

