package com.example.springproject.Conteroller;


import com.example.springproject.entity.AppUser;

import com.example.springproject.entity.UserRole;

import com.example.springproject.emailvalidation.registration.RegisterationService;
import com.example.springproject.emailvalidation.registration.RegistrationRequest;
import com.example.springproject.extra.FindCurrentUser;
import com.example.springproject.extra.TempClass;
import com.example.springproject.investment.RequestStatus;
import com.example.springproject.investment.applyingforinvestment.ApplyingForInvesment;
import com.example.springproject.investment.applyingforinvestment.ApplyingForInvesmentService;

import com.example.springproject.investment.isuue.IssueService;
import com.example.springproject.investment.isuue.MainIssue;
import com.example.springproject.investment.uploaddownloaddoc.Doc;
import com.example.springproject.investment.uploaddownloaddoc.DocStorageService;
import com.example.springproject.user.UserRepository;
import com.example.springproject.user.UserService;
import com.example.springproject.emailvalidation.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
//@RequestMapping(value = "user")
public class UserController {


    private boolean valid=false;

    private AppUser userValid;

    @Autowired
    private TempClass tempClass;

    List<Long> tempFile=new ArrayList<>();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DocStorageService docStorageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @Autowired
    private RegisterationService registerationService;

    @Autowired
    private ApplyingForInvesmentService applyingforinvestmentService;

    @PostMapping("/register")
    public String submitForm(@ModelAttribute("user") AppUser user,Model model)
        {
            boolean  userNameValidFlage = findDuplicate(user.getUsername(),1);
            boolean  emailValidFlage = findDuplicate(user.getEmail(),2);
            if(userNameValidFlage==true || emailValidFlage==true ) {

                if(userNameValidFlage==true)
                {     user.setUsername("duplicate"); }
                if(emailValidFlage==true)
                {   user.setEmail("duplicate");      }

                userValid=new AppUser(user);
                valid=true;
                HttpServletRequest request = null;
                model.addAttribute("user", user);
                model.addAttribute("listProfession", UserRole.ROLE_USER);
                return "registerform";
            }else
            {
                RegistrationRequest registrationRequest = new RegistrationRequest(user);
                System.out.println(user.getUsername());

                System.out.println(registrationRequest.getUserName());
                System.out.println(registrationRequest.getFirstName());

                registrationRequest.setPassword(passwordEncoder.encode(  user.getPassword()));
                String result=  registerationService.register(  registrationRequest, Token.REGISTER);

                if(result.equals("error"))
                {
                    return "redirect:/register" ;
                }

                return "redirect:/login";
            }
        }

    @GetMapping("/register")
    public String showForm( HttpServletRequest request,  Model model, HttpServletResponse response) {
        Cookie cookie=new Cookie("error", tempClass.getErrorEmail());
        response.addCookie(cookie);

        AppUser user = new AppUser();
        model.addAttribute("user", user);
        model.addAttribute("listProfession", UserRole.ROLE_USER);
        tempClass.setErrorEmail("noterror");
         return "registerform";
    }

    @PostMapping("/login")
    public String loginForm( @ModelAttribute("user") AppUser user)
    {
             return "index";
    }

    @GetMapping("/login")
    public String loginToForm( Model model) {

            AppUser user = new AppUser();
            model.addAttribute("user", user);
            return "login";
        }

    @PostMapping("/edite")
    public String editeForm(@ModelAttribute("user") AppUser user) {

        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();
        /////////////////duplicate////////////////////
            user.setUsername(userName);
            user.setPassword(passwordEncoder.encode(   user.getPassword()));
            userService.editeUser(user);
            return "redirect:/userPanel";
        }

    @GetMapping("/edite")
    public String editeshowForm(Model model ) {

        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();
        /////////////////duplicate////////////////////
            AppUser user= userService.loadUserByUsername(userName);
            model.addAttribute("user", user);
            model.addAttribute("listProfession", UserRole.ROLE_ADMIN);
            return "edite-user";
        }

    @PostMapping("/editePassword")
    public String editePasswordForm(@ModelAttribute("user") AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.editePassUser(user);
        return "redirect:/userPanel";
    }


    @GetMapping("/editePassword")
    public String editePasswordget(Model model  ) {

        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();

        AppUser user= userService.loadUserByUsername(userName);
        model.addAttribute("user", user);
        return "uservalidation/edite-password";
    }

    @GetMapping("/editePassword/{userName}")
    public String editePasswordAdmin(@ModelAttribute("userName") String userName ,Model model ) {

        AppUser user= userService.loadUserByUsername(userName);
        model.addAttribute("user", user);
        return "uservalidation/edite-password";
    }


///////////////////////////////////////////

    public boolean findDuplicate(String input , int type){

        boolean validFlage=false;
        List<AppUser> user1=userService.findAllUser();

        Iterator<AppUser> iterator = user1.iterator();
        String temp=null;
        while (iterator.hasNext())
        {
            if (type==1)
                {
                temp=iterator.next().getUsername();
                }
            else if(type==2)
                {
                temp=iterator.next().getEmail();
                }

            if(( temp!=null) &&(input!=null)) {

                if(temp.equals(input)) {

                     validFlage=true;}
            }
        }
        return validFlage;
    }

    ///////////////ADMIN-DASHBOARD///////////////////////

    @GetMapping("/adminDashbord")
    public String dashboard(Model model)
    {
        List <AppUser> user=userService.findAllUnDeleteUser();
        model.addAttribute("user",user);
        return   "dashboard/admin-dashboard";
    }


    @GetMapping("/adminDashbord/disabled/{userName}")
    public String dashboardDisable(@PathVariable String userName, Model model){

        AppUser user1=userService.loadUserByUsername(userName);
        if(user1.getEnable()==true)
        {        userService.disableUserByUserName(user1.getUsername());    }
        else if( user1.getEnable()==false)
        {        userService.enableUserByUserName(user1.getUsername());     }
        return  "redirect:/adminDashbord";
    }


    @GetMapping("/adminDashbord/edite/{userName}")
    public String dashboardEdite(@PathVariable String userName, Model model){

        AppUser user=userService.loadUserByUsername(userName);
        model.addAttribute("user",user);
        return editeUserGet(model);
    }

    @GetMapping("/dashboardEditeuser")
    public String editeUserGet( Model model)
    {
        return "dashboard/edite-user";
    }

    @PostMapping("/dashboardEditeUser")
    public String editeUserPost( @ModelAttribute("appUser") AppUser appUser)
    {
        System.out.println("======= "+appUser.getFirstName());
        System.out.println("======= "+appUser.getRoles());
        System.out.println("======= "+appUser.getPassword());

        userService.editeUser(appUser );

        System.out.println("=======================bigproblem==================");
        return "redirect:/adminDashbord";
    }

    @GetMapping("/adminDashbord/delete/{userName}")
    public String dashboardDelete(@PathVariable String userName){
        AppUser user=userService.loadUserByUsername(userName);
         userService.deleteUser(user);
        return "redirect:/adminDashbord";
    }

    ///////////////////////////////////SHOW AND EDITE invesment///////////////////////////////////////////
    Long investId= Long.valueOf(0);
    Long totalSize=Long.valueOf(0);
    Long tempTotalSize=Long.valueOf(0);
    @GetMapping("/showAndEditeInvesment")
    public String showAndEditeInvesment(Model model)
    {

        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();
        /////////////////duplicate////////////////////
        List <ApplyingForInvesment> investment=
                applyingforinvestmentService.findByUserName(userName, RequestStatus.IN_PROGRESS );
        List <ApplyingForInvesment>  reversInvestment=
                applyingforinvestmentService.findByUserNameReverse(userName,RequestStatus.IN_PROGRESS );

        model.addAttribute("reversInvestment",reversInvestment);
        model.addAttribute("investment",investment);
        return   "investment/show-invesment-for-user";
    }


    @GetMapping("/adminInvesment/viewInvesmentForUser/{id}")
    public String viewInvesmentForUser (@PathVariable Long id, Model model)
    {
        List<Doc> docs = docStorageService.findDocByInvesmentId(id);
        model.addAttribute("docs", docs);
        ApplyingForInvesment investment=
                applyingforinvestmentService.getInvestmentById(id);
        model.addAttribute("investment",investment);
        return   "investment/show-detail-invesment-for-user";
    }

    @GetMapping("/adminInvesment/editeInvesmentForUser/{id}")
    public String editeInvesmentForUser (@PathVariable Long id, Model model )
    {
        TempClass tempClass=new TempClass();

        List<Doc> docs = docStorageService.findDocByInvesmentId(id);
        List<Doc> unSaveDocs = docStorageService.listFindById(tempFile);

        Iterator<Doc> itr = unSaveDocs.iterator();
        while (itr.hasNext())
        {
            docs.add(itr.next());
        }

        for(Doc d:docs){
            totalSize=totalSize+d.getData().length;
        }

        ApplyingForInvesment investment=
                applyingforinvestmentService.getInvestmentById(id);

        ApplyingForInvesment  applyingforinvestment=new ApplyingForInvesment();

        List<MainIssue> mainIssues= issueService.findAllIssue();
        String temp1=null;
        String temp2=null;

        Map <String,String> map=new HashMap<>();
        for (MainIssue mainIssue:mainIssues)
        {
            temp1= mainIssue.getSubjectIssue();
            temp2= mainIssue.getDetailsIssue();
            if(( temp1!=null)  ) {
                map.put(temp1,temp2);
            }
        }

        tempClass.setTemp(totalSize);
        model.addAttribute("tempClass", tempClass);
        model.addAttribute("docs", docs);
        model.addAttribute( "map",map);
        model.addAttribute("investment",investment);
        investId=investment.getId();
        return   "investment/edite-invesment-for-user";
    }

    @PostMapping("/adminInvesment/editeInvesmentForUser")
    public String editeInvesmentForUserPost( @ModelAttribute(value = "investment")
                                                         ApplyingForInvesment applyingforinvestment)
    {
        ////////////////edite////////////////////
        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();
        /////////////////duplicate////////////////////
        AppUser user= userService.loadUserByUsername(userName);

        /*********************/
        /////////////////dirty code////////////////////
        applyingforinvestmentService.updateInvesmentAfterEdite
                (applyingforinvestment.getId(), applyingforinvestment.getIssue(),applyingforinvestment.getDescription());
        for(Long docId:tempFile)
        {
            docStorageService.updateDoc(investId, docId,  user.getId());
        }
        tempFile.clear();
        return "happy";
    }
    /////////////////USER-PANEL/////////////////////////////

    @GetMapping("/userPanel")
    public String loginForm(Model model  ) {

        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();
        AppUser user= userService.loadUserByUsername(userName);
        model.addAttribute("user",user);
        return "main-user-panel";

    }
    @GetMapping("/logOut")
    public String logOutForm(Model model  )
    {
        return "log-out";
    }
}
