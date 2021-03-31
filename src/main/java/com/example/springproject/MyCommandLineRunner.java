package com.example.springproject;


import com.example.springproject.entity.AppUser;
import com.example.springproject.entity.UserRole;
import com.example.springproject.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class MyCommandLineRunner implements CommandLineRunner {




    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {

//        AppUser user1 = new AppUser();
//        user1.setPassword(passwordEncoder.encode("1"));
//        user1.setEnabled(true);
//        user1.setUserName("a1");
//        user1.setEmail("mostafa18051b@yahoo.com");
//        user1.setFirstName("114441");
//        user1.setLastName("11144");
//        user1.setNationalCode("11144");
//        user1.grantAuthority(UserRole.ROLE_RESPONSIBLE);
//        userRepository.save(user1);
//                AppUser user2 = new AppUser();
//        user2.setPassword(passwordEncoder.encode("1"));
//        user2.setEnabled(true);
//        user2.setUserName("b1");
//        user2.setEmail("mostafa18051b@yahoo.com");
//        user2.setFirstName("114441");
//        user2.setLastName("11144");
//        user2.setNationalCode("11144");
//        user2.grantAuthority(UserRole.ROLE_USER);
//        userRepository.save(user2);
//        AppUser user3 = new AppUser();
//        user3.setPassword(passwordEncoder.encode("1"));
//        user3.setEnabled(true);
//        user3.setUserName("c1");
//        user3.setEmail("mostafa18051b@yahoo.com");
//        user3.setFirstName("114441");
//        user3.setLastName("11144");
//        user3.setNationalCode("11144");
//        user3.grantAuthority(UserRole.ROLE_ADMIN);
//        userRepository.save(user3);



    }
}
