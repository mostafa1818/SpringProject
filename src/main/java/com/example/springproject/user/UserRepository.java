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
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<AppUser,Long> {

     Optional<AppUser> findByUserName(String userName) ;

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

             " WHERE user.userName = ?1")
     int updatePasswordUser(String userName,String password  );

     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.email = ?2" +

             " WHERE user.userName = ?1")
     int updateEmailUser(String userName,String email  );

     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.password = ?2," +
             " user.firstName = ?3" +

             " WHERE user.userName = ?1")
     int editeUserPart1(
             String userName  , String password ,String firstName );

     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.lastName = ?2," +


             "  user.email = ?3" +

             " WHERE user.userName = ?1")
     int editeUserPart2(
             String userName   ,String lastName ,String email );

     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.phoneNumber = ?2," +

             "  user.nationalCode = ?3" +
             " WHERE user.userName = ?1")
     int editeUserPart3(String userName  ,  String phoneNumber, String nationalcode);

     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.roles = ?2" +


             " WHERE user.userName = ?1")
     int editeUserPart4(String userName  ,  UserRole role );

     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.numberProgressInvesment = ?2 " +


             " WHERE user.userName = ?1")
     int editeNumberInvesment(String userName  ,Integer number);

     @Transactional
     @Modifying
     @Query("select user.id from " +
                     "AppUser user " +
                     "where  user.userName=?1 "
             )
     List<Long> findUserIdByUserName(String userName);


     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.enabled = false" +

             " WHERE user.userName = ?1")
     int disableUserByUserName(String userName );

     @Transactional
     @Modifying
     @Query("UPDATE AppUser user " +
             "SET user.enabled = true" +

             " WHERE user.userName = ?1")
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

             " WHERE user.userName = ?1")
     int deleteUserByUserName(String userName );

     @Transactional
     @Modifying
     @Query("select user  from " +
             "AppUser user " +
             "where  user.userName=?1 "
     )
     List<AppUser> findUserByUserName(String userName);

}
