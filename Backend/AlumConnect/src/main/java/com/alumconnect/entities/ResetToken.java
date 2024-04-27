package com.alumconnect.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This class is used to generate and manage reset tokens for user password reset requests.
 */
@Data
@Entity
@Table(name = "reset_token")
public class ResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String resetToken;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    private User user;

    private LocalDateTime expiryDate;

    public ResetToken() {
        this.resetToken = UUID.randomUUID().toString();
        this.expiryDate = calculateExpiryDate();
    }

    public ResetToken(User user) {
        this();
        this.user = user;
    }

    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusHours(1);
    }
}
