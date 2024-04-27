package com.alumconnect.repository;

import com.alumconnect.entities.UserPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomepagePostRepository extends JpaRepository<UserPostEntity, Long> {

    @Query("""
            SELECT p
            FROM UserPostEntity p
            WHERE EXISTS (
                SELECT 1
                FROM User u
                LEFT JOIN Student s ON u.id = s.user.id
                LEFT JOIN AlumniProfileEntity ap ON u.id = ap.user.id
                LEFT JOIN InterestEntity i ON i.student.id = s.id OR i.alumniProfile.id = ap.id
                WHERE u.id = 1
                AND LOWER(p.text) LIKE CONCAT('%', LOWER(i.interest),'%')
            )
            """)
    List<UserPostEntity> findByInterestsOrAllPosts(@Param("keyword") String keyword);

}