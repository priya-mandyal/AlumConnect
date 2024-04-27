package com.alumconnect.repository;

import com.alumconnect.entities.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for managing ResetToken entities in the AlumConnect system.
 */
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

    /**
     * Retrieves an optional ResetToken entity by the specified token.
     *
     * @param token The token associated with the ResetToken to retrieve.
     * @return An Optional containing the ResetToken entity if found, otherwise empty.
     */
    Optional<ResetToken> findByResetToken(String token);
}

