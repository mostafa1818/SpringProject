package com.example.springproject.extra;

import com.example.springproject.investment.uploaddownloaddoc.DocStorageService;
import com.example.springproject.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.Date;

@Configuration
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    private DocStorageService docStorageService;
    @Autowired
    private UserService userService;

    @Scheduled(fixedRate = 600000, initialDelay = 900000)
    public void scheduleTaskWithInitialDelay() {

        userService.clearServer();
    }
    @Scheduled(fixedRate = 600000, initialDelay = 86400000)
    public void scheduleTaskWithInitialDelayDeleteFile() {

        docStorageService.clearDoc();
    }
    //////////////test/////////////////////////////
//    LocalDateTime localDateTime = LocalDateTime.now();;
//
//    @Scheduled(fixedRate = 5000000, initialDelay = 10000)
//    public void scheduleTask  () {
//        Date date=new Date();
//        System.out.println("1----1    "+date );
//         localDateTime = LocalDateTime.now();
//    }
//
//
//    @Scheduled(fixedRate = 30000, initialDelay = 20000)
//    public void scheduleTaskWithInitial () {
//        Date date=new Date();
//        System.out.println("2----2    "+date );
//        userService.fun(localDateTime);
//    }

//    @Scheduled(fixedRate = 60000, initialDelay = 1000)
//    public void scheduleTaskWithInitialDelay() {
//        System.out.println("clearServer------------------    ");
//        userService.clearServer();
//    }

}