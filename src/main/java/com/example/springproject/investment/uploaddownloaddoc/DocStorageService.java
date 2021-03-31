package com.example.springproject.investment.uploaddownloaddoc;

import com.itextpdf.text.Document;

import java.io.File;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.stream.StreamResult;

@Service
public class DocStorageService {
    @Autowired
    private DocRepository docRepository;

    public Doc saveFile(MultipartFile file) {
        String docname = file.getOriginalFilename();
        try
        {
            Doc doc = new Doc(docname,file.getContentType(),file.getBytes());
            doc.setAddDate(new Date());
            return docRepository.save(doc);
        }
        catch(Exception e)
        {   e.printStackTrace();  }
        return null;
    }

    public  Doc  getFile(Long fileId) {
        return docRepository.findById(fileId)   .stream()
                .findFirst()
                .get();
    }

    public List<Doc> getFiles(){
        return docRepository.findAll();
    }

    public void updateDoc(Long InvesmentId, Long DocId, Long userId)
    {
        docRepository.updateInvesmentId(InvesmentId, DocId,  userId);
    }

    public void deleteDoc(  Long docId)
    {
        List<Doc> docs=getFiles();
        Doc doc = docs
                .stream()
                .filter(item -> docId.equals(item.getId()))
                .findAny() .orElse(null);

        docRepository.delete(doc);
    }

    public void clearDoc()
    {
        List<Doc>docs= docRepository.findAll();
        Date date=new Date();
        int tempCurrentDate= (int) date.getDate();
        int tempDocDate=0;
        int tempDate=0;
        for(Doc doc :docs) {
            if(doc.getInvesmentId()==null && doc.getInvesmentId()==null) {
                tempDocDate=doc.getAddDate().getDate();
                tempDate =tempCurrentDate-tempDocDate;
                if((tempDate==2)||((tempDate==-28)))
                docRepository.delete(doc);
            }
        }
    }

    public  List<Doc>  listFindById(List<Long> fileId) {

        List<Doc> doc=new ArrayList<>();
        Iterator itr = fileId.iterator();
        while (itr.hasNext()) {
            Long temp = (Long)itr.next();
            doc.add( docRepository.findById(temp).stream()
                                                .findFirst()
                                                .get());
        }
        return doc;
    }

    public  List<Doc>  findDocByUserIdAndInvesment(Long userId,Long invesmentId) {
        return docRepository.findDocByUserIdAndInvesment(userId,invesmentId);
    }

    public  List<Doc>  findDocByInvesmentId(Long invesmentId) {
        return docRepository.findDocByInvesmentId(invesmentId);
    }

    public void deleteDocs(  List<Doc> doc)
    {
          doc.stream().forEach(docs->docRepository.delete(docs));
    }

    public Doc saveDocumentFile(MultipartFile file) {
        String docname = file.getOriginalFilename();
        try {
            Doc doc = new Doc(docname,file.getContentType(),file.getBytes());
            doc.setAddDate(new Date());
            return docRepository.save(doc);
        }
        catch(Exception e)
        { e.printStackTrace(); }

        return null;
    }

}