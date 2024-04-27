package com.alumconnect.repository;

import com.alumconnect.entities.InterestEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link InterestEntity} entities.
 */
@Repository
public interface InterestRepository extends JpaRepository<InterestEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM InterestEntity ad WHERE ad.alumniProfile.id = ?1 AND ad.student IS NULL")
    void deleteByAlumniProfileId(Long alumniProfileId);

    @Modifying
    @Transactional
    @Query("DELETE FROM InterestEntity ad WHERE ad.student.id = ?1 AND ad.alumniProfile IS NULL")
    void deleteByStudentId(Long studentId);
    @Query("SELECT ie FROM InterestEntity ie WHERE ie.student IS NOT NULL AND ie.interest = ?1")
    List<InterestEntity> findByStudentIsNotNullAndInterest(String interest);

    @Query("SELECT ie FROM InterestEntity ie WHERE ie.alumniProfile IS NOT NULL AND ie.interest = ?1")
    List<InterestEntity> findByAlumniProfileIsNotNullAndInterest(String interest);

}