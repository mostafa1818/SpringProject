package com.example.springproject.investment.applyingforinvestment;

import com.example.springproject.entity.AppUser;
import com.example.springproject.extra.FindCurrentUser;
import com.example.springproject.investment.RequestStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ApplyingForInvesmentService {

    @Autowired
    private ApplyingForInvesmentRepository investmentRepository;

    public Long save(ApplyingForInvesment investment)
    {
        ApplyingForInvesment  investment1=  investmentRepository.save(investment);
        return investment1.getId();
    }

    public ApplyingForInvesment loadApply(Long id)
    {
        return investmentRepository.findById(id)
                                    .stream()
                                    .findFirst()
                                    .get();
    }

    public List<ApplyingForInvesment> getInvestment(String username , RequestStatus status)
    {
        return investmentRepository.findByUserName(username,status);
    }

    public List<ApplyingForInvesment> findByUserName(String username , RequestStatus status)
    {
        return investmentRepository.findByUserName(username,status);
    }

    public List<ApplyingForInvesment> findByUserNameReverse(String username , RequestStatus status)
    {
        return investmentRepository.findByUserNameReverse(username,status);
    }

    public  ApplyingForInvesment getInvestmentById(Long id)
    {
        ApplyingForInvesment invesment= investmentRepository.ListfindByIdFor(id).stream()
                                                                                .findFirst()
                                                                                .get();
        return invesment;
    }

    public List<ApplyingForInvesment> findByUserNameAlone(String username  )
    {
        return investmentRepository.findByUserNameAlone(username );
    }

    public  void updateInvesmentStatuse(ApplyingForInvesment invesment, AppUser user)
    {
            investmentRepository.
                    updateInvesmentStatusePart1(invesment.getId(), invesment.getAnswer() ,invesment.getRequestStatus() );
            investmentRepository.
                    updateInvesmentStatusePart2(invesment.getId(), user);
            LocalDate localDate = LocalDate.now();
            investmentRepository.
                    updateInvesmentResponseDate(invesment.getId(), localDate );
    }

    public  void updateInvesmentAfterEdite(Long InvesmentId, String issue,String description )
    {
        investmentRepository.
                updateInvesmentAfterEdite(InvesmentId, issue, description);
    }
    public  void deleteInvesment (Long invesmentId  )
    {
        investmentRepository.
                delete( getInvestmentById(invesmentId));
    }
    public  String findDocByUserName (Long id )
    {
      return   investmentRepository.
                findDocByUserName( id).stream()
                                        .findFirst()
                                        .get();
    }
   public List<ApplyingForInvesment> findInvesmentByDateForUser (String userName,LocalDate  startLocalDate ,LocalDate  endLocalDate  )
   {
       return   investmentRepository.
               findInvesmentByDateForUse(  userName,startLocalDate ,endLocalDate );
   }
    public List<ApplyingForInvesment> findInvesmentByDateForResponsible (String userName,LocalDate  startLocalDate ,LocalDate  endLocalDate  )
    {
        return   investmentRepository.
                findInvesmentByDateForResponsible(  userName,startLocalDate ,endLocalDate );
    }
    public List<String> findAllUserNameOfInvesment()
    {
        return   investmentRepository. findAll().stream().map(e -> e.getUserName()).collect(Collectors.toList());
    }
    public List<String> findAllUserNameOfResponsibleInvesment()
    {

            List<ApplyingForInvesment> outPut = investmentRepository.findAll();


        Iterator<ApplyingForInvesment> iterator=outPut.iterator();
        while(iterator.hasNext())
        {
            String answer=iterator.next().getAnswer();
            if(answer==null){iterator.remove();}
        }
          List<String> outPut2=outPut.stream().map(e->e.getRsponsibleUser().getUsername()).collect(Collectors.toList());


        return outPut2;
    }


}
