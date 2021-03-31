package com.example.springproject.investment.isuue;

import com.example.springproject.entity.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class IssueService {

    public final IssueRepository issueRepository;

    public void saveIsuue(MainIssue mainIssue)
    {
        issueRepository.save(mainIssue);
    }

    public MainIssue loadIssue(long id)
    {
       return issueRepository.findById(id)
                                .stream()
                                .findFirst()
                                .get();
    }

    public void deleteIsuue(MainIssue mainIssue)
    {
         issueRepository.delete(mainIssue);
    }

    public void editeIsuue(MainIssue mainIssue)
    {
        issueRepository.updateIssueUser(mainIssue.getId(),mainIssue.getSubjectIssue(),
                                                          mainIssue.getDetailsIssue());
    }

    public List<MainIssue> findAllIssue( )
    {
       List<MainIssue> issueNews= issueRepository.findAll();
       return issueNews;
    }
    public MainIssue findIssue( String subject)
    {
        List<MainIssue> issueNews=   issueRepository.findAll( );
        MainIssue  filteredList = issueNews
                                  .stream()
                                  .filter(item -> subject.equals(item.getSubjectIssue()))
                                  .findAny() .orElse(null);;
        return filteredList ;
    }
}