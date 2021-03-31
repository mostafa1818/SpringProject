package com.example.springproject.emailvalidation.registration;


import com.example.springproject.entity.AppUser;
import com.example.springproject.entity.UserRole;
import com.example.springproject.user.UserService;
import com.example.springproject.emailvalidation.token.ConfirmationToken;
import com.example.springproject.emailvalidation.token.ConfirmationTokenService;
import com.example.springproject.extra.TempClass;
import com.example.springproject.emailvalidation.token.Token;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;
import java.util.Optional;


@Controller
@RequestMapping(path = "api/v2/registration")
@AllArgsConstructor
public class ForgetController {

    private   TempClass tempClass;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

     private final RegisterationService registrationService;

    @Autowired
    private RegisterationService registerationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/forget")
    public String submitForm(@ModelAttribute("user") AppUser user)
    {
        if(user.getEmail()!=null) {
            AppUser tempUser = userService.loadUserByEmail(user.getEmail());
            RegistrationRequest registrationRequest = new RegistrationRequest(tempUser.getUsername(), tempUser.getEmail());
            String result = registerationService.register(registrationRequest, Token.FORGET);
             if(result.equals("error"))
            {
               return "redirect:/api/v2/registration/forget" ;
            }
        }return "/login";
    }

    @GetMapping("/forget")
    public String showForm(Model model, HttpServletResponse response)
    {
        Cookie cookie=new Cookie("error", tempClass.getErrorEmail());
        response.addCookie(cookie);
        AppUser user = new AppUser();
         model.addAttribute("user", user);
//        model.addAttribute("error", tempClass.getErrorEmail());
        tempClass.setErrorEmail("noterror");
//        model.addAttribute("listProfession", UserRole.ROLE_ADMIN);
        return "forget-password-login";
    }








    @GetMapping(path = "confirm")
    public ModelAndView confirm(@RequestParam("token") String token)
    {
        registrationService.confirmToken(token,false);
        Optional<ConfirmationToken> OptionalUser=confirmationTokenService.getToken(token);
        ConfirmationToken confirmationToken =OptionalUser.stream()
                .findFirst()
                .get();
        String user=confirmationToken.getUser().getUsername();
        tempClass.setUserName(user);
        ModelAndView modelAndView = new ModelAndView("forget-password");
        modelAndView.addObject("message", user);

        return modelAndView;
    }

    @PostMapping(path = "confirm")
    public String confirm2(@RequestParam(name="password1") String password1,@RequestParam(name="password2") String repeatPassword)
    {
         AppUser user=  userService.loadUserByUsername(tempClass.getUserName());
        if(password1.equals(repeatPassword)) {
            user.setPassword( passwordEncoder.encode(   password1));
            userService.editePassUser(user);
        }
          return "/login";
    }
}