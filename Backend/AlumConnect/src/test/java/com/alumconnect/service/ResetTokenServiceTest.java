package com.alumconnect.service;

import com.alumconnect.entities.ResetToken;
import com.alumconnect.entities.User;
import com.alumconnect.repository.ResetTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ResetTokenServiceTest {

    @Mock
    private ResetTokenRepository resetTokenRepository;

    @InjectMocks
    private ResetTokenService resetTokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createResetToken_ValidUser_Success() {
        when(resetTokenRepository.save(any(ResetToken.class))).thenReturn(new ResetToken(new User()));
        User user = new User();
        ResetToken createdToken = resetTokenService.createResetToken(user);
        verify(resetTokenRepository, times(1)).save(any(ResetToken.class));
        assertEquals(user, createdToken.getUser());
    }

    @Test
    void findByToken_ValidToken_TokenFound() {
        when(resetTokenRepository.findByResetToken(any(String.class))).thenReturn(Optional.of(new ResetToken()));
        Optional<ResetToken> foundToken = resetTokenService.findByToken("mockToken");
        verify(resetTokenRepository, times(1)).findByResetToken(any(String.class));
        assertEquals(true, foundToken.isPresent());
    }

    @Test
    void findByToken_InvalidToken_TokenNotFound() {
        when(resetTokenRepository.findByResetToken(any(String.class))).thenReturn(Optional.empty());
        Optional<ResetToken> foundToken = resetTokenService.findByToken("invalidToken");
        verify(resetTokenRepository, times(1)).findByResetToken(any(String.class));
        assertEquals(false, foundToken.isPresent());
    }

    @Test
    void deleteToken_ValidToken_DeletedSuccessfully() {
        ResetToken token = new ResetToken();
        resetTokenService.deleteToken(token);
        verify(resetTokenRepository, times(1)).delete(token);
    }
}