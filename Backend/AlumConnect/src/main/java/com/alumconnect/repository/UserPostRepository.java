package com.alumconnect.repository;

import com.alumconnect.entities.UserPostEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPostRepository extends JpaRepository<UserPostEntity, Long> {
    @Query("SELECT up FROM UserPostEntity up WHERE up.user.id = ?1")
    List<UserPostEntity> findByUserId(long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserPostEntity up WHERE up.id = ?1")
    void deleteByPostId(long postId);
}
