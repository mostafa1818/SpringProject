package com.example.springproject.investment.uploaddownloaddoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface DocRepository  extends JpaRepository<Doc,Long>{

    @Transactional
    @Modifying
    @Query("UPDATE Doc c " +
            " SET c.invesmentId =?1 ," +
            "   c.userId =?3 " +
            "WHERE c.id=?2")
    int updateInvesmentId(Long InvesmentId, Long DocId, Long userId);

    @Query
            ("select doc from " +
                    "Doc doc " +
                    "where  doc.userId=?1 and doc.invesmentId=?2 "
            )
    List<Doc> findDocByUserIdAndInvesment(Long userId,Long invesmentId);
    @Query
            ("select doc from " +
                    "Doc doc " +
                    "where  doc.invesmentId=?1 "
            )
    List<Doc> findDocByInvesmentId(Long id);
}