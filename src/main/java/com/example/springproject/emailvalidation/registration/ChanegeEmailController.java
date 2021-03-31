package com.example.springproject.emailvalidation.registration;

import com.example.springproject.entity.AppUser;
import com.example.springproject.entity.UserRole;
import com.example.springproject.extra.FindCurrentUser;
import com.example.springproject.extra.TempClass;
import com.example.springproject.user.UserService;
import com.example.springproject.emailvalidation.token.ConfirmationToken;
import com.example.springproject.emailvalidation.token.ConfirmationTokenService;
import com.example.springproject.emailvalidation.token.Token;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@RequestMapping(path = "api/v3/changeEmail")
@AllArgsConstructor
public class ChanegeEmailController {
    private UserService userService;

    private   TempClass tempClass;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    private final RegisterationService registrationService;

    @Autowired
    private RegisterationService registerationService;

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token)
    {
        registrationService.confirmToken(token,false);
        Optional<ConfirmationToken> OptionalUser=confirmationTokenService.getToken(token);
        ConfirmationToken confirmationToken =OptionalUser.stream()
                .findFirst()
                .get();

        String userName=confirmationToken.getUser().getUsername();
        String email=confirmationToken.getUser().getEmail();
        AppUser user= userService.loadUserByUsername(userName);
        user.setEmail(email);
        userService.editeEmailUser(user);

        return "happy";
    }

    @PostMapping("/editeEmailLogin")
    public String submitForm(@ModelAttribute("user") AppUser user)
    {

        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();

        AppUser tempUser= userService.loadUserByUsername(userName);
        RegistrationRequest registrationRequest = new RegistrationRequest(tempUser);
        registrationRequest.setEmail(user.getEmail());
        String result=  registerationService.register(  registrationRequest, Token.EDITEEmail);
        if(result.equals("error"))
        {
            return "redirect:/api/v3/changeEmail/editeEmailLogin" ;
        }


        return "redirect/userPanel";
    }

    @GetMapping("/editeEmailLogin")
    public String showForm(Model model, HttpServletResponse response)
    {


        Cookie cookie=new Cookie("error", tempClass.getErrorEmail());
        response.addCookie(cookie);

        ///////////////DUPLICATE////////////
        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();

        AppUser user=userService.loadUserByUsername(userName);

        model.addAttribute("user", user);
        tempClass.setErrorEmail("noterror");

        return "uservalidation/edite-email-login";
    }
}
