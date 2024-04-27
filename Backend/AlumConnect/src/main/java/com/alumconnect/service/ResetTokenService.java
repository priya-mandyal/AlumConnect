package com.alumconnect.service;

import com.alumconnect.entities.ResetToken;
import com.alumconnect.entities.User;
import com.alumconnect.repository.ResetTokenRepository;
import com.alumconnect.repository.UserRepository;
import com.alumconnect.service.interfaces.IResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ResetTokenService implements IResetTokenService {
    @Autowired
    private ResetTokenRepository resetTokenRepository;

    public ResetToken createResetToken(User user) {
        ResetToken token = new ResetToken(user);
        return resetTokenRepository.save(token);
    }

    public Optional<ResetToken> findByToken(String token) {
        return resetTokenRepository.findByResetToken(token);
    }

    public void deleteToken(ResetToken token) {
        resetTokenRepository.delete(token);
    }
}