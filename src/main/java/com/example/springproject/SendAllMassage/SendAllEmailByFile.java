package com.example.springproject.SendAllMassage;

import com.example.springproject.entity.UserRole;
import com.example.springproject.extra.TempClass;
import com.example.springproject.investment.uploaddownloaddoc.Doc;
import com.example.springproject.user.UserService;
import lombok.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import org.docx4j.jaxb.XPathBinderAssociationIsPartialException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Controller
@RequestMapping(value ="/sendAllMasssage")
public class SendAllEmailByFile implements Runnable {


     @Autowired
    private TempClass tempClass ;
    @Autowired
    private UserService userService;


    public SendAllEmailByFile(TempClass tempClass) {
        this.tempClass=tempClass;
    }


    @GetMapping("/upload")
    public String sendEmailGet()
    {
        return "sendallemail/send_all_email";
    }

    @PostMapping("/upload")
    public String sendEmailPost(@RequestParam("files")  MultipartFile  files,
                                @RequestParam("attachfiles")  MultipartFile  attachFiles,
                                @RequestParam("textFile") String textFile,
                                @RequestParam("typeemail") String typeemail)
            throws IOException, JAXBException, Docx4JException {



       if (attachFiles.isEmpty())
       {
           tempClass.setFile(null);
       }
        if (files.isEmpty())
        {
            tempClass.setEmails(null);
        }

        if(typeemail.equals("users"))
        {
            tempClass.setEmails(userService.findUserTypeCustomer(UserRole.ROLE_USER ));
        }
        else if(typeemail.equals("responsibles"))
        {
            tempClass.setEmails(userService.findUserTypeCustomer(UserRole.ROLE_RESPONSIBLE ));
        }
        else {

            createWordFile(files);
        }

        if (!attachFiles.isEmpty())
        {
            convertFile(attachFiles);
        }

        tempClass.setMainText(textFile);

         Thread t= new Thread(new SendAllEmailByFile( tempClass));
        t.start();

        return "redirect:/userPanel";
    }


    @SneakyThrows
    @Override
    public void run() {
 
        tempClass.getEmails().stream().forEach(s-> System.out.println(s));

        boolean cheakFileIsEmpty=true;
        if(tempClass.getFile()!=null)
            {cheakFileIsEmpty=true;}
        else
            {cheakFileIsEmpty=false;}
         tempClass.getEmails().stream().forEach(s-> System.out.println(s));

        String subject = "Test Email";
        String message = "Test Email Message";

        final String username = "your email";
        final String password = "your password";


        Properties props =createPropertise();

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });


        try {
             Message message1 = new MimeMessage(session);
            message1.setFrom(new InternetAddress(username));
            message1.setSubject(subject);


            BodyPart messageBodyPart = new MimeBodyPart();
            MimeBodyPart attachmentPart = new MimeBodyPart();
            if(cheakFileIsEmpty)
            {
                attachmentPart.attachFile(tempClass.getFile());
            }

             messageBodyPart.setText(tempClass.getMainText());

             Multipart multipart = new MimeMultipart();
             multipart.addBodyPart(messageBodyPart);
             if(cheakFileIsEmpty) {
                 multipart.addBodyPart(attachmentPart);
            }
            message1.setContent(multipart);
             StringBuilder sb = new StringBuilder();
            int counter= 0;
            for (String email : tempClass.getEmails()) {
                sb.append(email);

                counter++;
                if (tempClass.getEmails().size() > counter) {
                    sb.append(", ");
                }
            }

            System.out.println(sb.toString()+"    sb");
             message1.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(sb.toString()));

            System.out.println("Sending Email to " + tempClass.getEmails() + " from " + username + " with Subject - " + subject);

            Transport.send(message1);
            if(tempClass.getFile()!=null){tempClass.getFile().delete();}
 
            tempClass.clearTempFiles();
            System.out.println("Email successfully sent to ------------------------------" + tempClass.getEmails());
        } catch ( Exception   e) {
            System.out.println("Email sending failed to ----------------------------------------" + tempClass.getEmails());
            System.out.println(e);
        }

    }

    private void createWordFile(MultipartFile  files) throws IOException, JAXBException, Docx4JException {

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +  //part before @
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        File file = new File("a.txt");
        Path filepath1 = Paths.get(String.valueOf(file));
        OutputStream os1 = Files.newOutputStream(filepath1);
        os1.write(files.getBytes());
        os1.close();

        File doc = new File("a.txt");
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
                .load(doc);
        MainDocumentPart mainDocumentPart = wordMLPackage
                .getMainDocumentPart();
        String textNodesXPath = "//w:t";
        List<Object> textNodes = mainDocumentPart
                .getJAXBNodesViaXPath(textNodesXPath, true);

        List<String> inputString = new ArrayList<>();
        for (Object obj : textNodes) {
            Text text = (Text) ((JAXBElement) obj).getValue();
            String textValue = text.getValue();
            String string = textValue;
            String[] parts = string.split(" ");
            for (String input : parts) {
                inputString.add(input);
            }
        }

        String temp = null;

        Iterator iterator = inputString.iterator();
        while (iterator.hasNext()) {
            temp = iterator.next().toString();
            Pattern pat = Pattern.compile(emailRegex);

            if (!pat.matcher(temp).matches()) {
                iterator.remove();
            }
        }



        List<String> emails = new ArrayList<>();
        for (String input : inputString) {
            emails.add(input);
        }
        tempClass.setEmails(emails);
        os1.close();
        file.delete();
    }

    private  void convertFile(MultipartFile attachFiles) throws IOException {
        File convFile = new File(attachFiles.getOriginalFilename());
        convFile.createNewFile();
        Path filepath2 = Paths.get(String.valueOf(convFile));
        OutputStream os2 = Files.newOutputStream(filepath2);
        os2.write(attachFiles.getBytes());
        os2.close();

        System.out.println("convFile======================" + convFile.length());
        tempClass.setFile(convFile);
        System.out.println("tempClass.getFile()======================" + tempClass.getFile().length());


    }
   private  Properties createPropertise()
   {
       final String smtpHost = "smtp.gmail.com";
       Properties props = new Properties();

       props.put("mail.smtp.user", "your email");
       props.put("mail.smtp.host", smtpHost);
       props.put("mail.smtp.port", 587);
       props.put("mail.transport.protocol", "smtp");
       props.put("mail.smtp.auth", "true");
       props.put("mail.smtp.starttls.enable", "true");
       props.put("mail.debug", "true");;
       props.put("mail.smtp.auth", "true");
       return  props;
   }

}


