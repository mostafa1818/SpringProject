package com.example.springproject.extra;

import com.example.springproject.emailvalidation.token.ConfirmationToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TempClass {

     List<String> emails  ;
    File file=null ;
    String errorEmail="noterror";
    ConfirmationToken confirmationToken;
    String fileName;
    String userName;
    String email;
    String mainText;

    Long temp;
    List<Long> tempFile=new ArrayList<>();

    public void setTempFiles(List<Long> tempFile)
    {
        this.tempFile.clear();
        this.tempFile.addAll(tempFile);
    }
    public void clearTempFiles( )
    {

        this.tempFile.clear();
    }
    public void clearTempFiles2( )
    {
        this.tempFile=null ;
    }
}


