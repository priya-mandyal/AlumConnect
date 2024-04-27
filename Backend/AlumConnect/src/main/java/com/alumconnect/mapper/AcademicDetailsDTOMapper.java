package com.alumconnect.mapper;

import com.alumconnect.dto.AcademicDetailDTO;
import com.alumconnect.entities.AcademicDetailEntity;
import com.alumconnect.mapper.interfaces.MapperObject;

public class AcademicDetailsDTOMapper implements MapperObject<AcademicDetailEntity, AcademicDetailDTO> {
    @Override
    public AcademicDetailDTO map(AcademicDetailEntity academicDetailEntity) {
        AcademicDetailDTO academicDetailDTO = new AcademicDetailDTO();
        academicDetailDTO.setDegree(academicDetailEntity.getDegree());
        academicDetailDTO.setUniversity(academicDetailEntity.getUniversity());
        return academicDetailDTO;
    }
}
