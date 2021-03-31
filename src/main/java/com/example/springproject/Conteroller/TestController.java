package com.example.springproject.Conteroller;


import com.example.springproject.entity.AppUser;
import com.example.springproject.extra.FindCurrentUser;
import com.example.springproject.extra.TempClass;
import com.example.springproject.investment.applyingforinvestment.ApplyingForInvesmentService;
import com.example.springproject.investment.uploaddownloaddoc.DocStorageService;
import com.example.springproject.investment.isuue.IssueService;
import com.example.springproject.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.*;

@Controller
 //@RequestMapping(path = "/a")
public class TestController {

    @Autowired
    private TempClass tempClass;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;
    @Autowired
    private ApplyingForInvesmentService applyingforinvestmentService;

    @Autowired
    private DocStorageService docStorageService;

    @Autowired
    private IssueService issueService;

    @RequestMapping("/login-error")
    public String login2(){return "login-error.html";}

    @RequestMapping("/profile")
    public String login4(){return "index.html";}

    @GetMapping("/logouttime")
    public String loginToForm( )
        {
            return "p.json";
        }


    List<Long> tempFile=new ArrayList<>();


     //////////////////////////
    @GetMapping("/user2")
    public String main(Model model)
    {

                Map<String, String> map = new HashMap<>();
                map.put("spring1", "mvc");
                map.put("spring", "mvc");
                model.addAttribute("message", "Baeldung");
                model.addAttribute("map", map);
  ;
        return   "upload-file/testhtml/d";
    }


    @RequestMapping("/u")
    public ModelAndView thymeleafView(Map<String, Object> model) {
        model.put("number", 1234);
        model.put("message", "Hello from Spring MVC");
        return new ModelAndView("upload-file/testhtml/d");
    }

    @Transactional
    @PostMapping("/admindashbord/disabled")
    public String dashboardDisablePost(@ModelAttribute("user") AppUser user ){

        AppUser user1=userService.loadUserByUsername(user.getUsername());
        if(user1.getEnable()==true) {
            userService.disableUserByUserName(user1.getUsername());
            }
        else if( user1.getEnable()==false) {
            userService.enableUserByUserName(user1.getUsername());
            }
        System.out.println("/admindashbord/disabled---PostMapping-------------------------------");
        return "happy";
    }



    @GetMapping("/maindashbord")
    public String mainDashboard(Model model)
    {
        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();
        /////////////////duplicate////////////////////
        AppUser user= userService.loadUserByUsername(userName);
        model.addAttribute("user",user);
        return   "dashboard/main-dashboard";
    }


    @GetMapping("/happy")
    public String happy(Model model)
    {
        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();
        /////////////////duplicate////////////////////
        AppUser user= userService.loadUserByUsername(userName);
        model.addAttribute("user",user);
        return   "test2/main";
    }


}

