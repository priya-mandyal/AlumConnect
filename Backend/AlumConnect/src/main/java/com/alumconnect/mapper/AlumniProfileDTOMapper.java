package com.alumconnect.mapper;

import com.alumconnect.dto.AlumniProfile;
import com.alumconnect.entities.AlumniProfileEntity;
import com.alumconnect.mapper.interfaces.MapperObject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlumniProfileDTOMapper implements MapperObject<AlumniProfileEntity, AlumniProfile> {
    @Override
    public AlumniProfile map(AlumniProfileEntity alumniProfileEntity) {
        AlumniProfile alumniProfile = new AlumniProfile();
        alumniProfile.setAvailability(alumniProfileEntity.getAvailability());
        alumniProfile.setEmail(alumniProfileEntity.getUser().getEmailId());
        alumniProfile.setExpertiseAndSkills(alumniProfileEntity.getExpertiseAndSkills());
        alumniProfile.setProfessionalJourney(alumniProfileEntity.getProfessionalJourney());
        return alumniProfile;
    }
}
