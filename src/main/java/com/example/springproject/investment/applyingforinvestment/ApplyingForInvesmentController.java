package com.example.springproject.investment.applyingforinvestment;

import com.example.springproject.entity.AppUser;
import com.example.springproject.extra.ExtraClass;
import com.example.springproject.extra.FindCurrentUser;
import com.example.springproject.extra.TempClass;

import com.example.springproject.investment.RequestStatus;
import com.example.springproject.investment.isuue.MainIssue;
import com.example.springproject.investment.uploaddownloaddoc.Doc;
import com.example.springproject.investment.uploaddownloaddoc.DocStorageService;
 import com.example.springproject.investment.isuue.IssueService;
import com.example.springproject.user.UserService;
import com.example.springproject.voiceRecord.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "api/investmentController")
public class ApplyingForInvesmentController {
    @Autowired
    private ApplyingForInvesmentService applyingforinvestmentService;
    @Autowired
    private IssueService issueService;
    @Autowired
    private UserService userService;
    @Autowired
    private TempClass tempClass ;
    private ExtraClass extraClass=new ExtraClass();

    private   List<Long> tempFile=new ArrayList<>();
    @Autowired
    private DocStorageService docStorageService;

    Long totalSize=Long.valueOf(0);
    Long tempTotalSize=Long.valueOf(0);


    ////////////////////////add//////////////////////////////

    @GetMapping(value = "/applyInvesment")
    public String addInvestment(Model model)
    {
        TempClass tempClass=new TempClass();

        ApplyingForInvesment  applyingForInvestment=new ApplyingForInvesment();

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

        List<Doc> docs = docStorageService.listFindById(tempFile);
        for(Doc d:docs){
            totalSize=totalSize+d.getData().length;
        }

        tempClass.setTemp(totalSize);
        model.addAttribute("tempClass", tempClass);
        model.addAttribute("docs", docs);
        model.addAttribute( "map",map);
        model.addAttribute("applyingForInvestment", applyingForInvestment);
        return "investment/add-investment";
    }

    @PostMapping (value = "/applyInvesment" )
    public String getInvestment( @ModelAttribute(value = "investment")
                                 ApplyingForInvesment applyingforinvestment,
                                 @ModelAttribute UploadedFile uploadedFile)
    {


        System.out.println("===================yes");
        MultipartFile multipartFile = uploadedFile.getMultipartFile();

        String fileName = "m1.mp3";
        File file = new File(  fileName);
        try {
            multipartFile.transferTo(file);
         } catch (NullPointerException | IOException e) {
         }


         
        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();
        /////////////////////duplicate//////////////
        AppUser user= userService.loadUserByUsername(userName);

        Integer temp=user.getNumberProgressInvesment()+1;
        user.setNumberProgressInvesment(temp);
        userService.editeNumberInvesment(user);
        applyingforinvestment.setIssue(applyingforinvestment.getIssue());

        applyingforinvestment.setUserName(userName);
        applyingforinvestment.setRequestStatus(RequestStatus.IN_PROGRESS);
        applyingforinvestment.setUserFriendly((long) 5);
        applyingforinvestment.setAddDate(  LocalDate.now());

       Long invesmentId= applyingforinvestmentService.save(applyingforinvestment);

       for(Long docId:tempFile)
       {
           docStorageService.updateDoc(invesmentId, docId,  user.getId());
       }
        tempFile.clear();
        tempClass.clearTempFiles();
        return "redirect:/userPanel";
    }

    @GetMapping(value = "/" )
    public String get(Model model)

    {
        replaceTempFile();
        for (Long s: tempClass.getTempFile()){
            System.out.println(s);
        }

        TempClass tempClass=new TempClass();
        ApplyingForInvesment  applyingForInvestment=new ApplyingForInvesment();

        List<MainIssue> mainIssues = issueService.findAllIssue();
        String temp1=null;
        String temp2=null;
        Map <String,String> map=new HashMap<>();
        for (MainIssue mainIssue:mainIssues)
        {
            temp1= mainIssue.getSubjectIssue();
            temp2= mainIssue.getDetailsIssue();

               if(( temp1!=null)  ) { map.put(temp1,temp2); }
        }
        List<Doc> docs = docStorageService.listFindById(tempFile);
        for(Doc d:docs)
               { totalSize=totalSize+d.getData().length; }
        tempClass.setTemp(totalSize);

        model.addAttribute( "map",map);
         model.addAttribute("applyingForInvestment", applyingForInvestment);
        model.addAttribute("tempClass", tempClass);
        model.addAttribute("docs", docs);

        System.out.println(")))))))))))))))))))))))1");

        return "investment/add-investment";
    }
////////////////////////edite//////////////////////////////

    Long investId= Long.valueOf(0);

    @GetMapping("/showAndEditeInvesment")
    public String showandediteInvesment(Model model)
    {

        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();
        /////////////////duplicate////////////////////
        List <ApplyingForInvesment> investment=
                applyingforinvestmentService.findByUserName(userName,RequestStatus.IN_PROGRESS );
        List <ApplyingForInvesment>  reversInvestment=
                applyingforinvestmentService.findByUserNameReverse(userName,RequestStatus.IN_PROGRESS );

        model.addAttribute("reversInvestment",reversInvestment);
        model.addAttribute("investment",investment);
        return   "investment/show-invesment-for-user";
    }


    @GetMapping("/adminInvesment/viewInvesmentForUser/{id}")
    public String viewInvesmentForUser (@PathVariable Long id, Model model)
    {
        System.out.println("problem1=====================");
        List<Doc> docs = docStorageService.findDocByInvesmentId(id);
        model.addAttribute("docs", docs);
        ApplyingForInvesment investment=
                applyingforinvestmentService.getInvestmentById(id);

        List<MainIssue> mainIssues = issueService.findAllIssue();
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
        model.addAttribute( "map",map);
        model.addAttribute("investment",investment);

        System.out.println("problem2===================="+investment.getRequestStatus());

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

        List<MainIssue> mainIssues = issueService.findAllIssue();
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
        System.out.println("1---edite----------------------------issue  "+applyingforinvestment.getIssue());

        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();
        /////////////////duplicate////////////////////
        AppUser user= userService.loadUserByUsername(userName);

        /*********************/
        /////////////////dirty code////////////////////
        applyingforinvestmentService.updateInvesmentAfterEdite
                (applyingforinvestment.getId(), applyingforinvestment.getIssue(),applyingforinvestment.getDescription());
        System.out.println("---------------------------------updateInvesmentAfterEdite   "+applyingforinvestment.getDescription());

        for(Long docId:tempFile)
        {
            docStorageService.updateDoc(investId, docId,  user.getId());
        }
        tempFile.clear();
        tempClass.clearTempFiles();
        System.out.println("---------------------------------updateInvesmentAfterEdite");
        return "redirect:/userPanel";
    }

    @GetMapping("/editeInvesmentLoadAgain")
    public String getedite(Model model) {

        replaceTempFile();
        TempClass tempClass=new TempClass();
        ApplyingForInvesment investment=
                applyingforinvestmentService.getInvestmentById(investId);

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

        model.addAttribute( "map",map);
        model.addAttribute("investment", investment);

        List<Doc> docs = docStorageService.findDocByInvesmentId(investId);
        List<Doc> unSaveDocs = docStorageService.listFindById(tempFile);

        Iterator<Doc> itr = unSaveDocs.iterator();
        while (itr.hasNext())
        {
            docs.add(itr.next());
        }
        totalSize= Long.valueOf(0);
        for(Doc d:docs){
            totalSize=totalSize+d.getData().length;
        }

        model.addAttribute("docs", docs);
        tempClass.setTemp(totalSize);
        model.addAttribute("totalsize", totalSize);
        model.addAttribute("tempClass", tempClass);
        return "investment/edite-invesment-for-user";
    }

    public void replaceTempFile(  )
    {
     //   tempClass.getTempFile().stream().forEach(s-> System.out.println(s));

        tempFile.clear();

        Iterator itr = tempClass.getTempFile().iterator();
        while (itr.hasNext())
        {
            tempFile.add((Long) itr.next());
        }
    }


    ////////////////////////////////admininvesment///////////////////////////////////////////
    @GetMapping("/adminInvesment")

    public String adminInvesmentDashboard(Model model)
    {

        Date date=new Date();
        Long tempCurrentDate=date.getTime();

        //////////////CLEANCODE ///////////////////////////
        List <AppUser> userHaveInvesment=userService.findAllUser();
        Integer number=null;
        List <AppUser> userWithOutInvesment=new ArrayList<>();
        AppUser temp=null;

        Iterator<AppUser> itr = userHaveInvesment.iterator();
        while (itr.hasNext())
        {
            temp= itr.next();
            number=temp.getNumberProgressInvesment();
            if (number==0)
            {
                userWithOutInvesment.add(temp);
                itr.remove();}
        }
        //////////////CLEANCODE ///////////////////////////
        model.addAttribute("userHaveInvesment",userHaveInvesment);
        model.addAttribute("userWithOutInvesment",userWithOutInvesment);


        System.out.println(" 1++++++++++++++++++++++++++++");

        return   "investment/show-users-have-invesment";
    }

    ////////////////////////////////list-invesment-for-answer///////////////////////////////////////////

    @GetMapping("/adminInvesment/listInvesment/{userName}")
    public String listInvesment (@PathVariable String userName, Model model)
    {

        List <ApplyingForInvesment> investment=
                applyingforinvestmentService.getInvestment(userName,  RequestStatus.IN_PROGRESS);
                ////we cant edite/////
        List <ApplyingForInvesment> closedInvestment=
                applyingforinvestmentService.getInvestment(userName,  RequestStatus.CLOSED);


        /////////////////////////dont remember////////////////////////////
//            List<List<Applyingforinvestment>> closedInvestmentByResponsible = divideList(closedInvestment);
//
//
//            Iterator<List<Applyingforinvestment>> itr = closedInvestmentByResponsible.iterator();
//            List<Applyingforinvestment> a = null;
//
//            while (itr.hasNext()) {
//                System.out.println("                un               ");
//                a = itr.next();
//                a.stream().forEach(s -> System.out.println(s.getDescription()));
//
//            }

//        List<List<Applyingforinvestment>>  answeredInvestmentByResponsible=  divideList( investment );
//
//        Iterator<List<Applyingforinvestment>> itr1 = answeredInvestmentByResponsible.iterator();
//        List<Applyingforinvestment> a1 = null;
//
//        while (itr1.hasNext()) {
//            System.out.println("                un               ");
//            a1= itr1.next();
//            a1.stream().forEach(s -> System.out.println(s.getDescription()));
//
//        }

        List <ApplyingForInvesment> answeredInvestment=
                applyingforinvestmentService.getInvestment(userName,  RequestStatus.HAS_BEEN_ANSWERED);

//        model.addAttribute("userName",userName);
        model.addAttribute("closedInvestment",closedInvestment);
        model.addAttribute("answeredInvestment",answeredInvestment);
        model.addAttribute("investment",investment);
        System.out.println(" 2++++++++++++++++++++++++++++");
        return   "investment/list-invesment-for-answer";
    }

    @GetMapping("/adminInvesment/viewDetailsInvesment/{id}")
    public String viewDetailsInvesment (@PathVariable Long id, Model model)
    {
        String userName =applyingforinvestmentService.findDocByUserName(id);
        Long userId=userService.findUserIdByUserName(userName);

        List<Doc> docs = docStorageService.findDocByUserIdAndInvesment(userId,id);
        docs.stream().forEach(s-> System.out.println(s+"------------------s"));

        List<Doc> docs2=docs.stream().filter(doc -> !doc.getDocType().equals("mp3")).collect(Collectors.toList());
        model.addAttribute("docs", docs2 );
        ApplyingForInvesment investment=
                applyingforinvestmentService.getInvestmentById(id);
        model.addAttribute("userName",userName);
        model.addAttribute("investment",investment);
        System.out.println(" 3++++++++++++++++++++++++++++");
        return   "investment/show-invesment-for-Responsible";
    }

    @GetMapping("/adminInvesment/viewDetailsInvesmenForAdmin/{id}")
    public String viewDetailsInvesmentForAdmin (@PathVariable Long id, Model model)
    {

        String userName =applyingforinvestmentService.findDocByUserName(id);
        Long userId=userService.findUserIdByUserName(userName);

        List<Doc> docs = docStorageService. findDocByUserIdAndInvesment(userId,id);
        model.addAttribute("docs", docs);
        ApplyingForInvesment investment=
                applyingforinvestmentService.getInvestmentById(id);

        model.addAttribute("userName",userName);
        model.addAttribute("investment",investment);
        System.out.println(" 4++++++++++++++++++++++++++++");
        return   "investment/show-invesment-for-admin";
    }

    @PostMapping("/adminInvesment/changeStatusInvesment")
    public String changestatusInvesment (@ModelAttribute("invesment") ApplyingForInvesment invesment)
    {
        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();
        AppUser user=userService.findUserByUserName(userName);

        ApplyingForInvesment tempInvesment  =  applyingforinvestmentService.getInvestmentById(invesment.getId());
        tempInvesment.setAnswer(invesment.getAnswer());
        tempInvesment.setRequestStatus(invesment.getRequestStatus());
        System.out.println(" 5++++++++++++++++++++++++++++");
                applyingforinvestmentService.
                updateInvesmentStatuse(tempInvesment,user);
        return   "redirect:/api/investmentController/adminInvesment";
    }

       //////////////////////////////delete-invesment///////////////////////////
       @GetMapping("/adminInvesment/deleteInvesment/{id}")
       public String deleteInvesment (@PathVariable Long id, Model model)
       {
           FindCurrentUser findCurrentUser=new FindCurrentUser();
           String userName= findCurrentUser.getSendUserName();

           applyingforinvestmentService.deleteInvesment(id);
           docStorageService.deleteDocs(docStorageService.findDocByInvesmentId(id));

           return   "redirect:/api/investmentController/adminInvesment";
       }



       public  <T extends ApplyingForInvesment>  List<List<T>>   divideList  (List<T> input)
    {
        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();
        /////////////////duplicate////////////////////
        AppUser currentUser= userService.loadUserByUsername(userName);

        AppUser tempUser=null;
        List <T> userWithOutInvesment=new ArrayList<>();
        T temp=null;

        Iterator<T> itr =  input.iterator();
        while (itr.hasNext())
        {
            temp= itr.next();
            tempUser=temp.getRsponsibleUser();
            if(tempUser!=null){
            if (tempUser.getUsername().equals(currentUser.getUsername()))
            {
                userWithOutInvesment.add(temp);
                itr.remove();}}
        }

        List<List<T>> outPut=new ArrayList<>();
        outPut.add(input);
        outPut.add(userWithOutInvesment);
        return outPut;
    }
}