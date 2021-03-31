package com.example.springproject.emailvalidation.token;

import com.example.springproject.entity.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
             confirmationTokenRepository.save(confirmationToken);
        }

    public Optional<ConfirmationToken> getToken(String token)
        {
            return confirmationTokenRepository.findByToken(token);
        }

    public boolean getEnable(String token) {
            Optional<ConfirmationToken> enable=  confirmationTokenRepository.findByToken(token);
            return   enable.get().getEnable();
        }

    public int setConfiremdAt(String token) {
            return confirmationTokenRepository.updateConfimedAt(token, LocalDateTime.now());
        }

    public int setDisable(String token) {
            return confirmationTokenRepository.updateEnable(token, false);
        }

    public List<ConfirmationToken> ListExpireToken( ) {
        return confirmationTokenRepository.ListExpireToken( );
    }
    public void deleteToken(ConfirmationToken token ) {
       confirmationTokenRepository.delete(token);
    }

}