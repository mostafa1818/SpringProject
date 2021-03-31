package com.example.springproject.emailvalidation.email;


import com.example.springproject.extra.TempClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;

@Component
public class EmailService implements EmailSender
{
    private final static Logger LOGGER= LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private final JavaMailSender mailSender;
    @Autowired
    private TempClass tempClass;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(String to, String email)
    {


         try
            {
                 if(email.equals("pdf")||email.equals("xls")){
                     MimeMessage mimeMessage=mailSender.createMimeMessage();
                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setText("Mail Body");
                    MimeBodyPart attachmentPart = new MimeBodyPart();

                    if(email.equals("pdf"))
                    {   attachmentPart.attachFile(new File(tempClass.getFileName()));     }
                    else
                    {   attachmentPart.attachFile(new File(tempClass.getFileName()));     }

                    Multipart multipart = new MimeMultipart();
                    multipart.addBodyPart(messageBodyPart);
                    multipart.addBodyPart(attachmentPart);
                    mimeMessage.setContent(multipart);
                    mimeMessage.setFrom(new InternetAddress("mstfjafarzadeh@gmail.com"));
                    mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                     try{
                         mailSender.send(mimeMessage);}catch (MailSendException e)
                     {
                         tempClass.setErrorEmail("error");
                     }
                     System.out.println("send");


                }
                else
                    {
                        MimeMessage mimeMessage=mailSender.createMimeMessage();
                        MimeMessageHelper messageHelper=new MimeMessageHelper(mimeMessage,"utf-8");
                        messageHelper.setText(email,true);
                        messageHelper.setTo(to);
                        System.out.println("---------------------------to"+to);
                        messageHelper.setSubject("confirm your email");
                        messageHelper.setFrom("mstfjafarzadeh@gmail.com");

                      try{
                        mailSender.send(mimeMessage);}catch (MailSendException e)
                      {
                          tempClass.setErrorEmail("error");
                      }
                        System.out.println("send");
                    }

            }
        catch ( Exception e)
            {

             }
    }
}