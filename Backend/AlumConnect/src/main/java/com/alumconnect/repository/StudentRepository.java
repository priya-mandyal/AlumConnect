package com.alumconnect.repository;

import com.alumconnect.entities.AlumniProfileEntity;
import com.alumconnect.entities.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserId(long userId);
    @Transactional
    @Modifying
    @Query("UPDATE Student SET hobbyForm = :hobbyForm, studentSummary = :summary WHERE id = :id")
    void updateProfileDetailsById(@Param("id") Long id, @Param("hobbyForm") String hobby, @Param("summary") String summary);

}
