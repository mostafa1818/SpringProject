package com.example.springproject.usernamevalidation;

import com.example.springproject.entity.AppUser;
import com.example.springproject.entity.UserRole;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("valid")
public class usernamecontrolller {

    @GetMapping("/registernew")
    public String showForm(HttpServletRequest request, Model model) {

        AppUser user=new AppUser();
        user.setFirstName("a");
        user.setLastName("b");

    ;
        model.addAttribute("user", user);

        return "uservalidation/validpage";

    }

}
