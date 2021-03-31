package com.example.springproject.thereport;

import com.example.springproject.emailvalidation.email.EmailSender;

import com.example.springproject.entity.AppUser;
import com.example.springproject.entity.UserRole;
import com.example.springproject.extra.FindCurrentUser;
import com.example.springproject.extra.TempClass;
import com.example.springproject.investment.applyingforinvestment.ApplyingForInvesment;
import com.example.springproject.investment.applyingforinvestment.ApplyingForInvesmentService;
import com.example.springproject.investment.uploaddownloaddoc.Doc;
import com.example.springproject.investment.uploaddownloaddoc.DocRepository;
import com.example.springproject.user.UserService;
import com.itextpdf.text.DocumentException;
import com.twilio.rest.api.v2010.account.ApplicationUpdater;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class ReportController {
    @Autowired
    private UserService userService;
    @Autowired
    private TempClass tempClass;
    @Autowired
    private ApplyingForInvesmentService invesmentService;
    @Autowired
    private   EmailSender emailSender;
    @Autowired
    private DocRepository docRepository;

    @PostMapping("/userReport")
    private String userReportPost( HttpServletRequest request ,Model model) throws IOException {
        System.out.println( "       userReport2-----------------------------------------" );

        ////////////////ADMIN////////////////////
        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();
        String mainUserName=null;
        if(tempClass.getUserName()==null)
        {
            mainUserName=userName;

        }else
        {
            mainUserName=tempClass.getUserName();
        }
        ////////////////ADMIN////////////////////

        List<LocalDate>input= giveDate(request);

        LocalDate localDateStart=input.get(0);
        LocalDate localDateEnd=input.get(1);

        List<ApplyingForInvesment> invesmentList= invesmentService.findInvesmentByDateForUser(mainUserName,localDateStart,localDateEnd);

        Doc doc= createPdf(invesmentList,userName, "usual");


        if (tempClass.getErrorEmail().equals("error"))
        {
            return "redirect:/userReport";
        }


        model.addAttribute("docs",doc);
        invesmentList.stream().forEach(s-> System.out.println(s.getDescription()));


        return "report/main-report";

    }
    @GetMapping("/userReport")
    public String userReportGet(  HttpServletResponse response ) {

        Cookie cookie=new Cookie("error", tempClass.getErrorEmail());
        response.addCookie(cookie);

        System.out.println("USER       ------------------------");
        tempClass.setErrorEmail("noterror");
        return "report/main-report";
    }


    @PostMapping("/responsibleReport")
    private String responsibleReportPost( HttpServletRequest request ,Model model) throws IOException {

        ////////////////ADMIN////////////////////
        System.out.println( "       responsibleReport2-----------------------------------------" );

        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String userName= findCurrentUser.getSendUserName();
        String mainUserName=null;
        if(tempClass.getUserName()==null)
        {
            System.out.println( "       responsibleReportegula-----------------------------------------" );

            mainUserName=userName;

        }else
        {
            System.out.println( "       responsibleReportnotegual-----------------------------------------" );

            mainUserName=tempClass.getUserName();
        }
        ////////////////ADMIN////////////////////

        List<LocalDate>input= giveDate(request);

        LocalDate localDateStart=input.get(0);
        LocalDate localDateEnd=input.get(1);

        List<ApplyingForInvesment> invesmentList= invesmentService.findInvesmentByDateForResponsible(mainUserName,localDateStart,localDateEnd);
        Doc doc= createPdf(invesmentList,userName,"usual" );
        if (tempClass.getErrorEmail().equals("error"))
        {
            return "redirect:/responsibleReport";
        }
        model.addAttribute("docs",doc);
        invesmentList.stream().forEach(s-> System.out.println(s.getDescription()));


        return "report/main-report";

    }

    @GetMapping("/responsibleReport")
    public String responsibleReportGet( HttpServletResponse response  ) {

        Cookie cookie=new Cookie("error", tempClass.getErrorEmail());
        response.addCookie(cookie);
        tempClass.setErrorEmail("noterror");
        return "report/main-report";
    }

    @PostMapping("/adminReport")
    private String adminReportPost( HttpServletRequest request,Model model  ) throws IOException {

        return "report/main-report";

    }

    @GetMapping("/adminReport/{userName}")
    public String adminReportGet( @PathVariable String userName,Model model ) {

         GiveDate giveDate=new GiveDate();

        tempClass.setUserName(userName);
        System.out.println("userName-----------"+userName);
        model.addAttribute("giveDate",giveDate);


        AppUser user=userService.findUserByUserName(userName);
        System.out.println("user.getRoles()-----------"+user.getRoles());
        if(user.getRoles().equals(UserRole.ROLE_RESPONSIBLE))
        {
            return "redirect:/responsibleReport";
        }
        else
        {
            return "redirect:/userReport";
        }

    }
 
    ///////////////////////EXCEL ADMIN REPORT/////////////////////////
    @PostMapping("/adminExcelReport")
    private String adminExcelReportPost( HttpServletRequest request,Model model  ) throws IOException {


        List<LocalDate>input= giveDate(request);
        LocalDate localDateStart=input.get(0);
        LocalDate localDateEnd=input.get(1);

        AppUser user=userService.findUserByUserName(tempClass.getUserName());
        List<ApplyingForInvesment> invesmentList=new ArrayList<>();
        if(user.getRoles().equals("USER"))
        {
            invesmentList= invesmentService.findInvesmentByDateForUser(tempClass.getUserName(),localDateStart,localDateEnd);
        }
        else
        {
            invesmentList= invesmentService.findInvesmentByDateForResponsible(tempClass.getUserName(),localDateStart,localDateEnd);
        }

        Doc doc= createPdf(invesmentList,tempClass.getUserName() ,"admin" );

        model.addAttribute("docs",doc);
        invesmentList.stream().forEach(s-> System.out.println(s.getDescription()));

        return "report/main-report";

    }

    @GetMapping("/adminExcelReport")
    public String adminExcelReportGet(  Model model ) throws IOException, InvalidFormatException {
        GiveDate giveDate=new GiveDate();

         ApplyingForInvesment  invesment=new ApplyingForInvesment();


        List<String> invesments2=invesmentService.findAllUserNameOfResponsibleInvesment();

         Set<String> distinct2 = new HashSet<>(invesments2);
        Map<String,Integer> outPut2=new HashMap<>();
        for (String s: distinct2) {
            outPut2.put(s,Collections.frequency(invesments2, s));
         }

                for (String name: outPut2.keySet()) {
            String key = name.toString();
            String value = outPut2.get(name).toString();
            System.out.println(key + " " + value);
        }
        Doc doc = createExel(outPut2);

        model.addAttribute("docs",doc);

        return "report/main-chart";

    }

  //////////////////////////CREATE CHART IN EXCEL///////////////////////

    public Doc createExel(Map<String,Integer> input) throws IOException, InvalidFormatException {
        LocalDateTime localDateTime=  LocalDateTime.now();
        String fileName= "u"+localDateTime.getDayOfYear()+""+
                         localDateTime.getMinute()+".xls";

            // creating workbook
            XSSFWorkbook workbook = new XSSFWorkbook();
            // creating sheet with name "Report" in workbook
            XSSFSheet sheet = workbook.createSheet("Report");

            XSSFDrawing drawing = sheet.createDrawingPatriarch();

            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 10, 20, 30, 40);

            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("عملکرد کلی");
            chart.setTitleOverlay(false);

            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP_RIGHT);


            int counter=0;

        String[] legendData = new String[ 1000] ;
        Integer[] numericData =new Integer[ 1000] ;

        Iterator<Map.Entry<String, Integer>> itr = input.entrySet().iterator();

        while(itr.hasNext())
        {
            Map.Entry<String, Integer> entry = itr.next();

            legendData[counter]=entry.getKey();
            numericData[counter]= entry.getValue();
            counter++;
        }


            XDDFDataSource<String> testOutcomes = XDDFDataSourcesFactory.fromArray(legendData);

            XDDFNumericalDataSource<Integer> values = XDDFDataSourcesFactory.fromArray(numericData);

            XDDFChartData data = chart.createData(ChartTypes.PIE3D, null, null);// for simple pie chart you can use
            // ChartTypes.PIE
            chart.displayBlanksAs(null);
            data.setVaryColors(true);
            data.addSeries(testOutcomes, values);

            chart.plot(data);

            try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
                workbook.write(outputStream);
            } finally {
                 workbook.close();
            }

        String filePath = fileName;


        byte[] bytes = Files.readAllBytes(Paths.get(filePath));


        File file1 = new File(filePath);
        byte[] byts = Files.readAllBytes(file1.toPath());

        Doc doc1 = new Doc();



        doc1.setAddDate(new Date());
        doc1.setDocType("application/vnd.ms-excel");
        doc1.setDocName(fileName);
        doc1.setData(byts);
        Doc tempDoc= docRepository.save(doc1);
        System.out.println(byts);
        tempClass.setFileName(fileName);

        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String tempUserName= findCurrentUser.getSendUserName();
        AppUser user= userService.findUserByUserName(tempUserName);

        emailSender.send(user.getEmail(),"xls");


        File file2=new File(fileName);

        if(file2.delete())
        { System.out.println("File deleted successfully"); }
        else
        { System.out.println("Failed to delete the file"); }
             return tempDoc;

    }

/////////////////CREATE PDF//////////////////////
    public Doc createPdf(List<ApplyingForInvesment> invesmentList,String userName,String type ) throws IOException {

        LocalDateTime localDateTime=  LocalDateTime.now();
        String fileName=userName+""+localDateTime.getDayOfYear()+""+localDateTime.getMinute();
         String file=null;
        if(type.equals("usual" ))
        {
           file = fileName+".pdf";
        }
        else{
            file = fileName+"1.pdf";
        }

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(file));
        Document doc = new Document(pdfDoc);
        int cellNumber=4;

        Table table = new Table(cellNumber);

        table.addCell(new Cell().add("id"));
        table.addCell(new Cell().add("Description"));
        table.addCell(new Cell().add("Issue"));
        table.addCell(new Cell().add("Answer"));

        for(ApplyingForInvesment invesment:invesmentList)
        {
            if(invesment.getId()!=null)
            { table.addCell(new Cell().add(invesment.getId().toString()));}
            else
            { table.addCell(new Cell().add("empty"));}

            if(invesment.getDescription()!=null)
            { table.addCell(new Cell().add(invesment.getDescription().toString())); }
            else
            { table.addCell(new Cell().add("empty"));}

            if(invesment.getIssue()!=null)
            { table.addCell(new Cell().add(invesment.getIssue().toString()));}
            else
            { table.addCell(new Cell().add("empty"));}

            if(invesment.getAnswer()!=null)
            { table.addCell(new Cell().add(invesment.getAnswer().toString()));}
            else
            { table.addCell(new Cell().add("empty"));}

        }

        doc.add(table);

        doc.close();

        String filePath = file;

        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        File file1 = new File(filePath);
        byte[] byts = Files.readAllBytes(file1.toPath());
        Doc doc1 = new Doc();

        doc1.setAddDate(new Date());
        doc1.setDocType("application/pdf");
        doc1.setDocName(fileName);
        doc1.setData(byts);
        Doc tempDoc= docRepository.save(doc1);
        System.out.println(byts);
        tempClass.setFileName(file);

        FindCurrentUser findCurrentUser=new FindCurrentUser();
        String tempUserName= findCurrentUser.getSendUserName();
        AppUser user= userService.findUserByUserName(tempUserName);

        emailSender.send(user.getEmail(),"pdf");


       File file2=new File(file);

      if(file2.delete())
      { System.out.println("File deleted successfully"); }
      else
      { System.out.println("Failed to delete the file"); }

        return tempDoc;
    }
/////////////////////////DATE//////////////////////////////
    public List<LocalDate> giveDate(HttpServletRequest request)
    {
        Cookie[]   cookeis1=request.getCookies();


        String inputDate=cookeis1[3].getName();



        Integer startday   =   Integer.valueOf   (inputDate.substring(0 , 2));
        Integer startmonth =   Integer.valueOf   (inputDate.substring(2 , 4));
        Integer startyear  =   Integer.valueOf   (inputDate.substring(4 , 8));
        Integer endday     =   Integer.valueOf   (inputDate.substring(8 ,10));
        Integer endmonth   =   Integer.valueOf   (inputDate.substring(10,12));
        Integer endyear    =   Integer.valueOf   (inputDate.substring(12,16));

        int[]  gregorianStartDate=jalaliToGregorian(startyear,startmonth,startday);
        int[]  gregorianEndDate=jalaliToGregorian(endyear,endmonth,endday);

        LocalDate localDateStart =   LocalDate.of(gregorianStartDate[0],gregorianStartDate[1],gregorianStartDate[2]);
        LocalDate localDateEnd =   LocalDate.of(gregorianEndDate[0],gregorianEndDate[1],gregorianEndDate[2]);

        List<LocalDate> outPut=new ArrayList<>();
        outPut.add(localDateStart);
        outPut.add(localDateEnd);
        return outPut;
    }

        public static int[] gregorianToJalali(int gregorianYear, int gregorianMonth, int gregorianDay) {

            int[] g_d_m = { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 };
            int gregorianYear2 = (gregorianMonth > 2) ? (gregorianYear + 1) : gregorianYear;
            int days = 355666 + (365 * gregorianYear) + ((int) ((gregorianYear2 + 3) / 4)) - ((int) ((gregorianYear2 + 99) / 100)) + ((int) ((gregorianYear2 + 399) / 400)) + gregorianDay + g_d_m[gregorianMonth - 1];
            int jalaliYear = -1595 + (33 * ((int) (days / 12053)));
            days %= 12053;
            jalaliYear += 4 * ((int) (days / 1461));
            days %= 1461;
            if (days > 365) {
                jalaliYear += (int) ((days - 1) / 365);
                days = (days - 1) % 365;
            }
            int jalaliMonth = (days < 186) ? 1 + (int) (days / 31) : 7 + (int) ((days - 186) / 30);
            int jalaliDay = 1 + ((days < 186) ? (days % 31) : ((days - 186) % 30));

            int[] jalali = { jalaliYear, jalaliMonth, jalaliDay };
            return jalali;
        }

    public static int[] jalaliToGregorian(int jalaliYear, int jalaliMonth, int jalaliDay) {

            jalaliYear += 1595;
            int days = -355668 + (365 * jalaliYear) + (((int) (jalaliYear / 33)) * 8) + ((int) (((jalaliYear % 33) + 3) / 4)) + jalaliDay + ((jalaliMonth < 7) ? (jalaliMonth - 1) * 31 : ((jalaliMonth - 7) * 30) + 186);
            int gregorianYear = 400 * ((int) (days / 146097));
            days %= 146097;
            if (days > 36524) {
                gregorianYear += 100 * ((int) (--days / 36524));
                days %= 36524;
                if (days >= 365)
                    days++;
            }
            gregorianYear += 4 * ((int) (days / 1461));
            days %= 1461;
            if (days > 365) {
                gregorianYear += (int) ((days - 1) / 365);
                days = (days - 1) % 365;
            }
            int gregorianDay = days + 1;
            int[] sal_a = { 0, 31, ((gregorianYear % 4 == 0 && gregorianYear % 100 != 0) || (gregorianYear % 400 == 0)) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
            int gregorianMonth;
            for (gregorianMonth = 0; gregorianMonth < 13 && gregorianDay > sal_a[gregorianMonth]; gregorianMonth++) gregorianDay -= sal_a[gregorianMonth];
            int[] gregorian = { gregorianYear, gregorianMonth, gregorianDay };
            return gregorian;
    }
}