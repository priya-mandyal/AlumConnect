package com.alumconnect.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "academic_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "university")
    private String university;

    @Column(name = "degree")
    private String degree;

    @ManyToOne
    @JoinColumn(name = "alumni_profile_id")
    private AlumniProfileEntity alumniProfile;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
