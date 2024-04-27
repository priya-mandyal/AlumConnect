package com.alumconnect.repository;

import com.alumconnect.entities.User;
import com.alumconnect.enums.Role;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves an optional User entity by the specified email ID.
     *
     * @param email The email ID of the user to retrieve.
     * @return An Optional containing the User entity if found, otherwise empty.
     */
    Optional<User> findByEmailId(@NotNull String email);

    List<User> findByRole(Role role);
}
