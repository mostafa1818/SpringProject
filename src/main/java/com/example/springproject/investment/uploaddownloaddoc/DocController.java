package com.example.springproject.investment.uploaddownloaddoc;

import java.util.*;

import com.example.springproject.extra.ExtraClass;
import com.example.springproject.extra.TempClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("upload")
public class DocController {

    @Autowired
    private DocStorageService docStorageService;
    @Autowired
    private TempClass tempClass3 ;

    ArrayList<Long> tempFile=new ArrayList<>();
    Long totalSize=Long.valueOf(0);
    Long tempTotalSize=Long.valueOf(0);

    @GetMapping("/")
    public String get(Model model) {
        List<Doc> docs = docStorageService.getFiles();
        model.addAttribute("docs", docs);
        return "investment/add-investment";
    }

    @PostMapping("/uploadFiles")
    public String uploadMultipleFiles(@RequestParam("files") MultipartFile[] files
              , @RequestParam("typeOfPage")  String typeOfPage
                                       )
    {

          Doc doc=new Doc();
        for (MultipartFile file: files)
        {
            System.out.println("fieltype-------------------==="+file.getContentType());

            doc=docStorageService.saveFile(file);
            System.out.println("doctype-------------------==="+doc.getDocType());
            tempFile.add(doc.getId());

        }
        tempClass3.setTempFiles(tempFile);
        return linkLibrrary(typeOfPage);

    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long fileId){
        System.out.println("doc-----------------------------------3 "+fileId);
        Doc doc = docStorageService.getFile(fileId) ;
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getDocType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+doc.getDocName()+"\"")
                .body(new ByteArrayResource(doc.getData()));
    }

    @GetMapping("/deleteFile/{fileId}/{typeOfPage}")
    public String deleteMultipleFiles(@PathVariable Long fileId,@PathVariable String typeOfPage)
    {
        Iterator itr = tempFile.iterator();
        while (itr.hasNext())
        {
            Long temp = (Long)itr.next();
            if (temp==fileId)
            {
                itr.remove();}
        }

        docStorageService.deleteDoc(fileId);
        tempFile.stream().forEach(s-> System.out.println(s));
        tempClass3.setTempFiles( tempFile);
        return linkLibrrary(typeOfPage );
    }

    public String linkLibrrary (String operationtype  )
    {
        String outPut=null;
        switch (operationtype)
        {

            case "deleteAdd":
                outPut="redirect:/api/investmentController/";
                break;
            case "uploadAdd":
                outPut="redirect:/api/investmentController/" ;
                break;
            case "deleteEdite":
                outPut="redirect:/api/investmentController/editeInvesmentLoadAgain";
                break;
            case "uploadEdite":
                outPut="redirect:/api/investmentController/editeInvesmentLoadAgain";
                break;
//            case "":
//                outPut="";
//                break;
//            case "":
//                outPut="";
//                break;
        }
                return outPut;
    }
}