package com.alumconnect.repository;

import com.alumconnect.entities.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {


    @Query("SELECT cr FROM ChatRoom cr WHERE cr.studentID = ?1 AND cr.alumniID = ?2")
    Optional<ChatRoom> findChatRoomIdByStudentIdAndAlumniId(long studentId, long alumniId);
}

