package com.alumconnect.repository;

import com.alumconnect.dto.AlumniProfile;
import com.alumconnect.entities.AlumniProfileEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AlumniProfileRepository extends JpaRepository<AlumniProfileEntity, Long> {
    Optional<AlumniProfileEntity> findByUserId(long userId);
    @Transactional
    @Modifying
    @Query("UPDATE AlumniProfileEntity SET availability = :availability, expertiseAndSkills = :expertiseAndSkills, professionalJourney = :professionalJourney WHERE id = :id")
    void updateProfileDetailsById(Long id, String availability, String expertiseAndSkills, String professionalJourney);
}
