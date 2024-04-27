package com.alumconnect.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Table(name = "alumni_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlumniProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alumniProfile")
    private List<AcademicDetailEntity> academicDetails;

    @Column(name = "availability")
    private String availability;

    @Column(name = "expertise_and_skills")
    private String expertiseAndSkills;

    @OneToMany(mappedBy = "alumniProfile", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<InterestEntity> interests;

    @Column(name = "professional_journey")
    private String professionalJourney;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private String profilePictureUrl;
}
