package com.example.springproject.user;


import com.example.springproject.entity.AppUser;

import com.example.springproject.emailvalidation.token.ConfirmationToken;
import com.example.springproject.emailvalidation.token.ConfirmationTokenService;
import com.example.springproject.emailvalidation.token.Token;
import com.example.springproject.investment.uploaddownloaddoc.Doc;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";;
    private  final BCryptPasswordEncoder bCryptPasswordEncoder;
    private   ConfirmationTokenService confirmationTokenService;
    private final  UserRepository userRepository;

    public  AppUser loadUserByEmail(String email) throws UsernameNotFoundException
        {
            List<AppUser> user=   userRepository.findAllBy( );
            AppUser  filteredList = user
                    .stream()
                    .filter(item -> email.equals(item.getEmail()))
                    .findAny() .orElse(null);;

            return filteredList ;

        }
    public void saveUser(AppUser user)
        {
            AppUser user1=new AppUser();
            user1.setUserName(user.getUsername());
            user1.setPassword(user.getPassword());
            user1.setEmail(user.getEmail());
            user1.setFirstName(user.getFirstName());
            user1.setLastName(user.getLastName());
            user1.setNationalCode(user.getNationalCode());
            user1.setPhoneNumber(user.getPhoneNumber());
            userRepository.save(user1);
        }

    @Override
    public AppUser loadUserByUsername(String userName) throws UsernameNotFoundException
        {
            AppUser user= userRepository.findByUserName(userName).stream()
                .findFirst()
                .get();
            return user;
        }
    @Transactional
    public ConfirmationToken signUpUser(AppUser user, Token role)
    {


         boolean userExists = userRepository
                .findByUserName(user.getEmail())
                .isPresent();
        ////MOHEM
        userExists=false;
        if (userExists)
            {
            }
         if(role.equals(Token.REGISTER))
            {
                String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                userRepository.save(user);
            }
         String token = UUID.randomUUID().toString();
         ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
                ,role
                );
         confirmationTokenService.saveConfirmationToken(confirmationToken);
         return confirmationToken;
    }

    public int enableUser(String email)
    {
        return userRepository.enableUser(email);
    }

    public void editePassUser(AppUser user)
        {
            userRepository.updatePasswordUser(user.getUsername(),user.getPassword());
        }
    @Transactional
    public void deleteUser(AppUser user)
    {
        userRepository.deleteUserByUserName(user.getUsername());
    }

    public void editeEmailUser(AppUser user)
    {
        userRepository.updateEmailUser(user.getUsername(),user.getEmail());
    }
    @Transactional
    public void clearServer()
        {
            LocalDateTime localDateTime = LocalDateTime.now();
            Long currentHour= Long.valueOf(localDateTime.getHour());
            Long currentMinute= Long.valueOf(localDateTime.getMinute());
            Long tempHour=null;
            Long tempMinute=null;

            List<ConfirmationToken> confirmationToken=confirmationTokenService.ListExpireToken();
             Iterator<ConfirmationToken> iterator=confirmationToken.iterator();
           ConfirmationToken tempToken=null;
            while(iterator.hasNext())
                {
                    tempToken=iterator.next();
                    if(tempToken.getConfirmedAt()==null)
                    {
                        Long hour= Long.valueOf(tempToken.getCreateAt().getHour());
                        Long minute= Long.valueOf(tempToken.getCreateAt().getMinute());
                        tempHour=currentHour-hour;
                        tempMinute=currentMinute-minute;
                        if((tempHour>-23)
                                    ||((tempHour== 0) &&(tempMinute>=15))
                                    ||((tempHour<0) &&(tempMinute>=(-45))))
                         {
                             AppUser user=tempToken.getUser();
                             confirmationTokenService.deleteToken(tempToken);
                             userRepository.delete(user);
                         }
                    }
                }
        }


    public void editeUser(AppUser user)
        {
            userRepository.editeUserPart1(user.getUsername(),
                                          user.getPassword(),
                                          user.getFirstName());
            userRepository.editeUserPart2(user.getUsername(),
                                          user.getLastName(),
                                          user.getEmail());
            userRepository.editeUserPart3(user.getUsername(),
                                          user.getPhoneNumber(),
                                          user.getNationalCode());
            userRepository.editeUserPart4(user.getUsername(),
                                          user.getRoles());

        }


    public List<AppUser> findAllUser( )
    {
        return   userRepository.findAll();
    }

    public List<AppUser> findAllUnDeleteUser( )
    {
        return   userRepository.findAllUnDeleteUser();
    }

    public  void  editeNumberInvesment(AppUser user )
    {
            userRepository.editeNumberInvesment(user.getUsername(),
                                                user.getNumberProgressInvesment());
    }

    public AppUser findUserByUserName(String userName )
    {
        return   userRepository.findUserByUserName(userName)
                .stream()
                .findFirst()
                .get();
    }

    public Long findUserIdByUserName(String userName )
    {
        return   userRepository.findUserIdByUserName(userName)
                .stream()
                .findFirst()
                .get();
    }

    public int disableUserByUserName(String userName  )
    {
       return userRepository.disableUserByUserName(userName);
    }
    public int enableUserByUserName(String userName  )
    {
        return userRepository.enableUserByUserName(userName);
    }
}