package com.example.springproject.extra;

import com.example.springproject.emailvalidation.token.ConfirmationToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TempClass {
    String errorEmail="noterror";
    ConfirmationToken confirmationToken;
    String fileName;
    String userName;
    String email;
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

}