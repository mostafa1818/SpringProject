package com.example.springproject.emailvalidation.registration;

import com.example.springproject.user.UserService;
import com.example.springproject.emailvalidation.token.ConfirmationTokenService;
import com.example.springproject.emailvalidation.token.Token;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    private final RegisterationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request)
        {
            return registrationService.register(request, Token.FORGET);
        }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token)

        {
            return registrationService.confirmToken(token,true);
        }

}