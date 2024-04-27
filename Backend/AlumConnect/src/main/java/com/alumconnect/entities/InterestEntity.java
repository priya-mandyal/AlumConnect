package com.alumconnect.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing the interests of an alumni.
 * <p>
 * An {@code Alumni} can have multiple interests, each linked to a specific {@link SkillSet}.
 * </p>
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "alumni_profile_id")
    private AlumniProfileEntity alumniProfile;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "interest")
    private String interest;
}

