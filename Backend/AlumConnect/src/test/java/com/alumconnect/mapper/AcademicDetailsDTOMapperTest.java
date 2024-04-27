package com.alumconnect.mapper;

import com.alumconnect.dto.AcademicDetailDTO;
import com.alumconnect.entities.AcademicDetailEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcademicDetailsDTOMapperTest {

    @Test
    public void testMapping() {
        // Create a sample AcademicDetailEntity object
        AcademicDetailEntity academicDetailEntity = new AcademicDetailEntity();
        academicDetailEntity.setDegree("Bachelor of Science");
        academicDetailEntity.setUniversity("Example University");

        // Create an instance of AcademicDetailsDTOMapper
        AcademicDetailsDTOMapper mapper = new AcademicDetailsDTOMapper();

        // Perform the mapping
        AcademicDetailDTO academicDetailDTO = mapper.map(academicDetailEntity);

        // Verify that the mapping is done correctly
        assertEquals(academicDetailEntity.getDegree(), academicDetailDTO.getDegree());
    }
}
