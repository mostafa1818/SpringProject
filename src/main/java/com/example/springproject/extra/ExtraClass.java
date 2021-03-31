package com.example.springproject.extra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExtraClass {
    ArrayList<Long> tempFile;

    public void setTempFiles(List<Long> tempFile)
    {
        this.tempFile.addAll(tempFile);
    }
}
