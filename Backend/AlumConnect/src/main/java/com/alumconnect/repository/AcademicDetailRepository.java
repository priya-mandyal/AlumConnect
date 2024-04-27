package com.alumconnect.repository;

import com.alumconnect.entities.AcademicDetailEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicDetailRepository extends JpaRepository<AcademicDetailEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM AcademicDetailEntity ad WHERE ad.alumniProfile.id = ?1 AND ad.student IS NULL")
    void deleteByAlumniProfileId(Long alumniProfileId);

    @Modifying
    @Transactional
    @Query("DELETE FROM AcademicDetailEntity ad WHERE ad.student.id = ?1 AND ad.alumniProfile IS NULL")
    void deleteByStudentId(Long studentId);
}
