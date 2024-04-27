package com.alumconnect.mapper;

import com.alumconnect.dto.AcademicDetailDTO;
import com.alumconnect.entities.AcademicDetailEntity;
import com.alumconnect.entities.AlumniProfileEntity;
import com.alumconnect.mapper.interfaces.MapperObject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AcademicDetailsEntityMapper implements MapperObject<AcademicDetailDTO, AcademicDetailEntity> {
    private final AlumniProfileEntity alumniProfileEntity;
    @Override
    public AcademicDetailEntity map(AcademicDetailDTO academicDetailDTO) {
        AcademicDetailEntity academicDetailEntity = new AcademicDetailEntity();
        academicDetailEntity.setUniversity(academicDetailDTO.getUniversity());
        academicDetailEntity.setDegree(academicDetailDTO.getDegree());
        academicDetailEntity.setAlumniProfile(alumniProfileEntity);
        return academicDetailEntity;
    }
}
