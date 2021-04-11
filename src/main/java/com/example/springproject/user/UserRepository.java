package com.example.springproject.user;


import com.example.springproject.entity.AppUser;
import com.example.springproject.entity.UserRole;
import com.example.springproject.investment.uploaddownloaddoc.Doc;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository

public interface UserRepository extends JpaRepository<AppUser,Long> {

     Optional<AppUser> findByUsername(String username) ;


     List<AppUser> findAllBy();
     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.enabled = true" +

             " WHERE user.email = ?1")
     int enableUser(String email );

     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.password = ?2" +

             " WHERE user.username = ?1")
     int updatePasswordUser(String userName,String password  );

     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.email = ?2" +

             " WHERE user.username = ?1")
     int updateEmailUser(String userName,String email  );

     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.firstName = ?2 " +


             " WHERE user.username = ?1")
     int editeUserPart1(
             String userName  ,String firstName );

     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.lastName = ?2," +


             "  user.email = ?3" +

             " WHERE user.username = ?1")
     int editeUserPart2(
             String userName   ,String lastName ,String email );

     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.phoneNumber = ?2," +

             "  user.nationalCode = ?3" +
             " WHERE user.username = ?1")
     int editeUserPart3(String userName  ,  String phoneNumber, String nationalcode);

     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.roles = ?2" +


             " WHERE user.username = ?1")
     int editeUserPart4(String userName  ,  UserRole role );

     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.numberProgressInvesment = ?2 " +


             " WHERE user.username = ?1")
     int editeNumberInvesment(String userName  ,Integer number);

     @Transactional
     @Modifying
     @Query("select user.id from " +
                     "AppUser user " +
                     "where  user.username=?1 "
             )
     List<Long> findUserIdByUserName(String userName);


     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.enabled = false" +

             " WHERE user.username = ?1")
     int disableUserByUserName(String userName );

     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.enabled = true" +

             " WHERE user.username = ?1")
     int enableUserByUserName(String userName );

     @Transactional
     @Modifying
     @Query("select user from " +
             " AppUser user  " +

             " WHERE user.locked = false")
     List<AppUser>  findAllUnDeleteUser();

     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.locked = true" +

             " WHERE user.username = ?1")
     int deleteUserByUserName(String userName );

     @Transactional
     @Modifying
     @Query("select user  from " +
             "AppUser user " +
             "where  user.username=?1 "
     )
     List<AppUser> findUserByUserName(String userName);


     @Query("select user.email  from " +
             "AppUser user " +
             "where  user.roles=?1 "
     )
     List<String> findUserTypeCustomer(UserRole role );



}
