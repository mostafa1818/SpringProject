package com.example.springproject.thereport;
import java.nio.file.Files;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import com.example.springproject.investment.uploaddownloaddoc.DocRepository;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.text.*;
import org.apache.commons.io.IOUtils;
import com.example.springproject.investment.uploaddownloaddoc.Doc;
import com.example.springproject.investment.uploaddownloaddoc.DocStorageService;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import nonapi.io.github.classgraph.utils.FileUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Node;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

@Controller
public class ReportController {
    @Autowired
    private DocStorageService docStorageService;
    @Autowired
    private DocRepository docRepository;
    @GetMapping("/reportpdf1")
    private String callPdf1() throws IOException, DocumentException {


      /////////////////////////////////////////DocumentException

        OutputStream outputStream =
                new FileOutputStream(new File("T4.pdf"));

        Document document = new Document();

        PdfWriter.getInstance(document, outputStream);

        document.open();

        document.add(new Paragraph("Hello World, Creating PDF document in Java is easy"));
        document.add(new Paragraph("You are customer # 2345433"));
        document.add(new Paragraph(new Date(new java.util.Date().getTime()).toString()));

        document.addCreationDate();
        document.addAuthor("Javarevisited");
        document.addTitle("How to create PDF docukkment in Java");
        document.addCreator("Thanks to iText, writing into PDF is easy");

        document.close();
        /////////////////////////////////////////DocumentException
        System.out.println("-----------------------------"+outputStream);



        String filePath = "T4.pdf";

        // file to byte[], Path
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));

        // file to byte[], File -> Path
        File file = new File(filePath);
        byte[] byts = Files.readAllBytes(file.toPath());

//        File file = new File( "D:\\TestFile.pdf" );




//        FileInputStream fis = new FileInputStream(file);
//
//
//        byte [] data = new byte[(int)file.length()];
//        fis.read(data);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        data = bos.toByteArray();


        Doc doc = new Doc();

        doc.setAddDate(new Date());
        doc.setData(byts);
        docRepository.save(doc);
        System.out.println(byts);



        return "redirect:/happy";
    }

    @GetMapping("/reportpdf2")
    private String callPdf2() throws IOException, DocumentException {



        /////////////////////////////////////////DocumentException

        OutputStream outputStream =
                new FileOutputStream(new File("T4.pdf"));

        Document document = new Document();

        PdfWriter.getInstance(document, outputStream);

        document.open();

        document.add(new Paragraph("Hello mostafa, Creating PDF document in Java is easy"));
        document.add(new Paragraph("You are customer # 2345433"));
        document.add(new Paragraph(new Date(new java.util.Date().getTime()).toString()));

        document.addCreationDate();
        document.addAuthor("Javarevisited");
        document.addTitle("How to create PDF docukkment in Java");
        document.addCreator("Thanks to iText, writing into PDF is easy");

        document.close();
        /////////////////////////////////////////DocumentException
        System.out.println("-----------------------------"+outputStream);



        String filePath = "T4.pdf";

        // file to byte[], Path
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));

        // file to byte[], File -> Path
        File file = new File(filePath);
        byte[] byts = Files.readAllBytes(file.toPath());




        Doc doc = new Doc();

        doc.setAddDate(new Date());
        doc.setData(byts);
        docRepository.save(doc);
        System.out.println(byts);
        File file2=new File("T4.pdf");

//        if(file2.delete())
//        { System.out.println("File deleted successfully"); }
//        else
//        { System.out.println("Failed to delete the file"); }


        return "redirect:/happy";
    }

    @GetMapping("/reportpdf3")
    private String callPdf3() throws IOException, DocumentException {



        /////////////////////////////////////////DocumentException

        OutputStream outputStream =
                new FileOutputStream(new File("D:\\T5.pdf"));

        Document document = new Document();

        PdfWriter.getInstance(document, outputStream);

        document.open();

        document.add(new Paragraph("Hello mostafa, Creating PDF document in Java is easy"));
        document.add(new Paragraph("You are customer # 2345433"));
        document.add(new Paragraph(new Date(new java.util.Date().getTime()).toString()));

        document.addCreationDate();
        document.addAuthor("Javarevisited");
        document.addTitle("How to create PDF docukkment in Java");
        document.addCreator("Thanks to iText, writing into PDF is easy");


        document.close();
        /////////////////////////////////////////DocumentException
        System.out.println("-----------------------------"+outputStream);



        String filePath = "D:\\T5.pdf";

        // file to byte[], Path
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));

        // file to byte[], File -> Path
        File file = new File(filePath);
        byte[] byts = Files.readAllBytes(file.toPath());




        Doc doc = new Doc();

        doc.setAddDate(new Date());
        doc.setData(byts);
        docRepository.save(doc);
        System.out.println(byts);
//        File file2=new File("T4.pdf");




//        if(file2.delete())
//        { System.out.println("File deleted successfully"); }
//        else
//        { System.out.println("Failed to delete the file"); }




        return "redirect:/happy";
    }

    void copy(InputStream source, OutputStream target) throws IOException {
        byte[] buf = new byte[8192];
        int length;
        while ((length = source.read(buf)) > 0) {
            target.write(buf, 0, length);
        }
    }
}
