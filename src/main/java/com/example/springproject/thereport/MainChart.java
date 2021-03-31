package com.example.springproject.thereport;

import com.example.springproject.emailvalidation.email.EmailSender;
import com.example.springproject.investment.uploaddownloaddoc.Doc;
import com.example.springproject.investment.uploaddownloaddoc.DocRepository;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

@Controller
public class MainChart {

    @Autowired
    private EmailSender emailSender;
    @Autowired
    private DocRepository docRepository;

    @GetMapping("/chart1")
    public  String chart1() throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
//invoking creatSheet() method and passing the name of the sheet to be created
        HSSFSheet sheet = workbook.createSheet("January");
//creating the 0th row using the createRow() method
        HSSFRow rowhead = sheet.createRow((short)0);
//creating cell by using the createCell() method and setting the values to the cell by using the setCellValue() method
        rowhead.createCell(0).setCellValue("S.No.");
        rowhead.createCell(1).setCellValue("Customer Name");
        rowhead.createCell(2).setCellValue("Account Number");
        rowhead.createCell(3).setCellValue("e-mail");
        rowhead.createCell(4).setCellValue("Balance");
//creating the 1st row
        HSSFRow row = sheet.createRow((short)1);
//inserting data in the first row
        row.createCell(0).setCellValue("1");
        row.createCell(1).setCellValue("John William");
        row.createCell(2).setCellValue("9999999");
        row.createCell(3).setCellValue("william.john@gmail.com");
        row.createCell(4).setCellValue("700000.00");
//creating the 2nd row
        HSSFRow row1 = sheet.createRow((short)2);
//inserting data in the second row
        row1.createCell(0).setCellValue("2");
        row1.createCell(1).setCellValue("Mathew Parker");
        row1.createCell(2).setCellValue("22222222");
        row1.createCell(3).setCellValue("parker.mathew@gmail.com");
        row1.createCell(4).setCellValue("200000.00");
        Workbook wb = new HSSFWorkbook();
//creates an excel file at the specified location
        OutputStream fileOut = new FileOutputStream("demo2.xls");
        System.out.println("Excel File has been created successfully.");
        workbook.write(fileOut);


        String filePath = "demo2.xls";

        // file to byte[], Path
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));

        // file to byte[], File -> Path
        File file1 = new File(filePath);
        byte[] byts = Files.readAllBytes(file1.toPath());
 
        Doc doc1 = new Doc();

        doc1.setAddDate(new Date());
        doc1.setData(byts);
        docRepository.save(doc1);
        System.out.println(byts);


        emailSender.send("mostafa18051b@yahoo.com","excel");

        return "redirect:/happy";
    }



}
