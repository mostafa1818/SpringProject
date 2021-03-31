package com.example.springproject.investment.isuue;

import com.example.springproject.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<MainIssue,Long> {

  @Transactional
  @Modifying
  @Query("UPDATE MainIssue issue " +
         "SET issue.detailsIssue = ?3 ," +
          "issue.detailsIssue = ?2"+
         " WHERE issue.id= ?1")
  int updateIssueUser(Long id,String subjectIssue,String detailsIssue  );
}