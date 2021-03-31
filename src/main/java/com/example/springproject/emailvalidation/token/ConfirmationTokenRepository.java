package com.example.springproject.emailvalidation.token;

import com.example.springproject.entity.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long>
{
    Optional<ConfirmationToken>findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            " SET c.confirmedAt =?2 " +
            "WHERE c.token=?1")
    int updateConfimedAt( String token,LocalDateTime confirmAt);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            " SET c.enable =?2 " +
            "WHERE c.token=?1")
    int updateEnable( String token,Boolean tag);

    @Query
            ("select token from " +
                    "ConfirmationToken token "

            )
    List<ConfirmationToken> ListExpireToken( );
    @Query
            ("select token from " +
                    "ConfirmationToken token " +
                    "where ( token.user=?1  )"
            )
    List<ConfirmationToken> ListExpireTokenByUser(AppUser user );
}