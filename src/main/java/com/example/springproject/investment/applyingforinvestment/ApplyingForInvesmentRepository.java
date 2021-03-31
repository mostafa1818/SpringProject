package com.example.springproject.investment.applyingforinvestment;

import com.example.springproject.entity.AppUser;
import com.example.springproject.investment.RequestStatus;

import com.example.springproject.investment.uploaddownloaddoc.Doc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApplyingForInvesmentRepository extends
                                JpaRepository<ApplyingForInvesment,Long> {
    @Query
    ("select invesment from " +
                    "ApplyingForInvesment invesment " +
                    "where ( invesment.userName=?1 " +
                    "and invesment.requestStatus=?2 )"
    ) List<ApplyingForInvesment> findByUserName(String username, RequestStatus status);

    @Transactional
    @Modifying
    @Query
    ("select invesment from " +
                    "ApplyingForInvesment invesment  " +
                    "where ( invesment.userName=?1 " +
                    "and invesment.requestStatus != ?2 )"
    ) List<ApplyingForInvesment> findByUserNameReverse(String username, RequestStatus status);

    @Transactional
    @Modifying
    @Query("UPDATE ApplyingForInvesment invesment" +
            " SET invesment.answer =?2 ," +
            "   invesment.requestStatus =?3 " +
            "WHERE invesment.id=?1")
    int updateInvesmentStatusePart1(Long invesmentId, String answer, RequestStatus status);
    @Transactional
    @Modifying
    @Query("UPDATE ApplyingForInvesment invesment" +
            " SET invesment.rsponsibleUser =?2 " +

            "WHERE invesment.id=?1")
    int updateInvesmentStatusePart2(Long invesmentId, AppUser user);

    @Query
            ("select invesment from " +
                    "ApplyingForInvesment invesment " +
                    "where ( invesment.userName=?1   )"
            ) List<ApplyingForInvesment> findByUserNameAlone(String username );

    @Query
            ("select invesment from " +
                    "ApplyingForInvesment invesment " +
                    "where ( invesment.id=?1  )"
            ) List<ApplyingForInvesment> ListfindByIdFor(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE ApplyingForInvesment invesment" +
            " SET invesment.issue =?2 ," +
            "   invesment.description =?3 " +
            "WHERE invesment.id=?1")
    int updateInvesmentAfterEdite(Long InvesmentId, String issue,String description  );

    @Query
            ("select invesment.userName from " +
                    "ApplyingForInvesment invesment " +
                    "where  invesment.id=?1 "
            )
    List<String> findDocByUserName(Long id);

    @Query
            ("select invesment  from " +
                    "ApplyingForInvesment invesment " +
                    "where  invesment.addDate >= ?2 and  invesment.addDate <=?3 and invesment.userName=?1 "
            )
    List<ApplyingForInvesment> findInvesmentByDateForUse(String userName,LocalDate  startLocalDate ,LocalDate  endLocalDate );


    @Query
            ("select invesment  from " +
                    "ApplyingForInvesment invesment " +
                    "where  invesment.addDate >= ?2 and  invesment.addDate <=?3 and invesment.rsponsibleUser.userName=?1 "
            )
    List<ApplyingForInvesment> findInvesmentByDateForResponsible(String userName,LocalDate  startLocalDate ,LocalDate  endLocalDate );




    @Transactional
    @Modifying
    @Query("UPDATE ApplyingForInvesment invesment" +
            " SET invesment.responseDate =?2  " +

            "WHERE invesment.id=?1")
    int updateInvesmentResponseDate(Long InvesmentId, LocalDate localDate   );


}