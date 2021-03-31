package com.example.springproject.investment.isuue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class MapIssue {

    private String issue;

    //@Size( max = 4000)
    //@Column
    private String detailsIssue;

    Map<String,String> issueMap= new HashMap<>();

    public void setIssueMap(String temp1, String temp2) {
        issueMap.put(temp1,temp2);
    }
}
