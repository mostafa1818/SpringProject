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
//        user1.setPassword(passwordEncoder.encode("12345aA@"));
//        user1.setEnabled(true);
//        user1.setUsername("responsible11");
//        user1.setEmail("mostafa18051b@yahoo.com");
//        user1.setFirstName("soheil");
//        user1.setLastName("masoomi");
//        user1.setNationalCode("3434433434");
//        user1.grantAuthority(UserRole.ROLE_RESPONSIBLE);
//        userRepository.save(user1);
//                AppUser user2 = new AppUser();
//        user2.setPassword(passwordEncoder.encode("12345aA@"));
//        user2.setEnabled(true);
//        user2.setUsername("user11");
//        user2.setEmail("java_nkh@yahoo.com");
//        user2.setFirstName("javad");
//        user2.setLastName("rahimi");
//        user2.setNationalCode("2323232323");
//        user2.grantAuthority(UserRole.ROLE_USER);
//        userRepository.save(user2);
//        AppUser user3 = new AppUser();
//        user3.setPassword(passwordEncoder.encode("12345aA@"));
//        user3.setEnabled(true);
//        user3.setUsername("admin11");
//        user3.setEmail("m.b_jafarzadeh.com");
//        user3.setFirstName("ali");
//        user3.setLastName("ahmadi");
//        user3.setNationalCode("1212121212");
//        user3.grantAuthority(UserRole.ROLE_ADMIN);
//        userRepository.save(user3);



    }
}
