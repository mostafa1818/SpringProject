package com.example.springproject.investment.isuue;

import com.example.springproject.entity.AppUser;
import com.example.springproject.entity.UserRole;
import com.example.springproject.extra.FindCurrentUser;
import com.example.springproject.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping(value = "/issueController")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @GetMapping("/addIssue")
    public String addIssue(Model model)
    {
        MainIssue mainIssue=new MainIssue();
        model.addAttribute("issue",mainIssue);
        return "issues/add-issue";
    }

    @PostMapping("/addIssue")
    public String saveIssue(  @ModelAttribute(value = "issue")MainIssue mainIssue ,Model model)
    {
        boolean  SubjectIssueFlage = findDuplicate(mainIssue );
        if(SubjectIssueFlage==true )
        {
            mainIssue.setSubjectIssue("duplicate");
            model.addAttribute("issue", mainIssue);
            return "issues/add-issue";
        }else
        {
            FindCurrentUser findCurrentUser = new FindCurrentUser();
            String userName = findCurrentUser.getSendUserName();
            /////////////////duplicate////////////////////
            AppUser user = userService.loadUserByUsername(userName);
            mainIssue.setAppUser(user);
            issueService.saveIsuue(mainIssue);
            return "redirect:/userPanel";
        }
    }

    @GetMapping("/deleteIssue/{subjectIssue}")
    public String deleteIssue(@PathVariable String subjectIssue)
    {
        MainIssue mainIssue= issueService.findIssue(subjectIssue);
        issueService.deleteIsuue(mainIssue);
        return "redirect:/userPanel";
    }
    String temp=null;

    @GetMapping("/editeIssue/{subjectIssue}")
    public String editeIssue(@PathVariable   String subjectIssue, Model model )
    {
        temp=subjectIssue;
        MainIssue mainIssue= issueService.findIssue(temp);
        model.addAttribute("mainIssue",mainIssue);
        return editeIssueGet(model);
    }

    @GetMapping("/editeIssues")
    public String editeIssueGet( Model model)
    {
        return "issues/edited-issue";
    }

    @PostMapping("/editeIssues")
    public String editeIssuePost( @ModelAttribute("mainIssue") MainIssue mainIssue)
    {
        System.out.println("--------------------"+mainIssue.getSubjectIssue());
        System.out.println("--------------------"+mainIssue.getDetailsIssue());

        issueService.editeIsuue(mainIssue );
        return "redirect:/userPanel";
    }

    @GetMapping("/dashboardIssue")
    public String listIssue(Model model)
    {
        List<MainIssue> mainIssue=issueService.findAllIssue();
        model.addAttribute("issue",mainIssue);
        return   "issues/show-list-issue";
    }

    public boolean findDuplicate(MainIssue input  ){

        boolean validFlage=false;
        List<MainIssue> issueNews=issueService.findAllIssue();
        Iterator<MainIssue> iterator = issueNews.iterator();
        String temp=null;
        while (iterator.hasNext())
        {
                temp=iterator.next().getSubjectIssue();

            if(( temp!=null)  )
            {
                if(temp.equals(input.getSubjectIssue()))
                {
                    validFlage=true;
                }
            }
        }
        return validFlage;
    }
}