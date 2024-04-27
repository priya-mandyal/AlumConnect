package com.alumconnect.mapper;

import com.alumconnect.dto.AlumniProfile;
import com.alumconnect.entities.AlumniProfileEntity;
import com.alumconnect.mapper.interfaces.MapperObject;
import org.jetbrains.annotations.NotNull;

public class AlumniProfileEntityMapper implements MapperObject<AlumniProfile, AlumniProfileEntity> {
    private final AlumniProfileEntity alumniProfileEntity;
    public AlumniProfileEntityMapper(AlumniProfileEntity alumniProfileEntity) {
        this.alumniProfileEntity = alumniProfileEntity;
    }

    @NotNull
    @Override
    public AlumniProfileEntity map(AlumniProfile alumniProfileDTO) {
        alumniProfileEntity.setAvailability(alumniProfileDTO.getAvailability());
        alumniProfileEntity.setExpertiseAndSkills(alumniProfileDTO.getExpertiseAndSkills());
        alumniProfileEntity.setProfessionalJourney(alumniProfileDTO.getProfessionalJourney());
        return alumniProfileEntity;

    }
}
