package com.alumconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlumniProfile {
    private long alumniProfileId;
    private String email;
    private List<AcademicDetailDTO> academicDetails;
    private String availability;
    private String expertiseAndSkills;
    private List<InterestDTO> interests;
    private String professionalJourney;
    private String imageUrl;
}
