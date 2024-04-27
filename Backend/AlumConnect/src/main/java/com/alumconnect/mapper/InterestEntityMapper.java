package com.alumconnect.mapper;

import com.alumconnect.dto.InterestDTO;
import com.alumconnect.entities.AlumniProfileEntity;
import com.alumconnect.entities.InterestEntity;
import com.alumconnect.mapper.interfaces.MapperObject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InterestEntityMapper implements MapperObject<InterestDTO, InterestEntity> {
    private final AlumniProfileEntity alumniProfileEntity;

    @Override
    public InterestEntity map(InterestDTO interestDTO) {
        InterestEntity interestEntity = new InterestEntity();
        interestEntity.setInterest(interestDTO.getLabel());
        interestEntity.setAlumniProfile(alumniProfileEntity);
        return interestEntity;
    }
}
