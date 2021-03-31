package com.example.springproject.emailvalidation.registration;

import com.example.springproject.emailvalidation.email.EmailSender;
import com.example.springproject.emailvalidation.email.EmailValidator;
import com.example.springproject.entity.AppUser;

import com.example.springproject.entity.UserRole;
import com.example.springproject.extra.TempClass;
import com.example.springproject.user.UserService;
import com.example.springproject.emailvalidation.token.ConfirmationToken;
import com.example.springproject.emailvalidation.token.ConfirmationTokenService;
import com.example.springproject.emailvalidation.token.Token;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;



@Service
@AllArgsConstructor
public class RegisterationService {

//    private final EmailValidator emailValidator;

     private final EmailSender emailSender;
    @Autowired
    TempClass tempClass;

    private ConfirmationTokenService confirmationTokenService;

    private UserService UserService;

    public String register(RegistrationRequest registrationRequest, Token role)  {


        String link= sendToken( registrationRequest ,role);
        emailSender.send(registrationRequest.getEmail(),
                buildEmail(registrationRequest.getEmail(),link));
        System.out.println(tempClass.getErrorEmail()+"--------------------------ERROR");

        if(tempClass.getErrorEmail().equals("error"))
       {
           confirmationTokenService.deleteToken(tempClass.getConfirmationToken());

           return "error";
       }
       else
           {
               return "token";}



    }


    @Transactional
    public String confirmToken(String token,Boolean flage)
    {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

         boolean tagService = confirmationTokenService.getEnable(token);
            if (tagService==false)
                        { throw new IllegalStateException("token is disable"); }

            if (confirmationToken.getConfirmedAt() != null)
                        { throw new IllegalStateException("email already confirmed"); }

            LocalDateTime expiredAt = confirmationToken.getExpiresAt();

            if (expiredAt.isBefore(LocalDateTime.now()))
                        { throw new IllegalStateException("token expired"); }

            confirmationTokenService.setConfiremdAt(token);

            confirmationTokenService.setDisable(token);
      if (flage) {
          UserService.enableUser(
                  confirmationToken.getUser().getEmail());
      }
            return "confirmed";
    }
    /*********************************************/

    public String sendToken(RegistrationRequest registrationRequest,Token role)  {

        String token="null";
        String link=null;
        ConfirmationToken confirmationToken=new ConfirmationToken();
        if( role.equals(Token.REGISTER))
        {
            AppUser user2=    new AppUser(registrationRequest,UserRole.ROLE_USER);
            try {
                 confirmationToken = UserService.signUpUser(user2, role);
                token = confirmationToken.getToken();
            }catch (Exception e){
            }

            link = "http://localhost:8080/api/v1/registration/confirm?token=" +token ;
        }
        else if( role.equals(Token.FORGET))
        {
            AppUser user=UserService.loadUserByUsername(registrationRequest.getUserName());
            try{
                 confirmationToken =   UserService.signUpUser(user,role);
                token = confirmationToken.getToken();
            } catch (Exception e) {
            }
            link = "http://localhost:8080/api/v2/registration/confirm?token=" +token ;
        }
        else if( role.equals(Token.EDITEEmail))
        {
            AppUser user=UserService.loadUserByUsername(registrationRequest.getUserName());
            user.setEmail(registrationRequest.getEmail());
             confirmationToken = UserService.signUpUser(user,role);
            token = confirmationToken.getToken();
            link = "http://localhost:8080/api/v3/ChangeEmail/confirm?token=" +token ;
        }
          tempClass.setConfirmationToken(confirmationToken);
        return link;
    }
    /*********************************************/
    public String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";}
        /*********************************************/
}