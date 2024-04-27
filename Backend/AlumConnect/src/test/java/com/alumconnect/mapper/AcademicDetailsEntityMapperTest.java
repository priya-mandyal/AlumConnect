package com.alumconnect.mapper;

import com.alumconnect.dto.AcademicDetailDTO;
import com.alumconnect.entities.AcademicDetailEntity;
import com.alumconnect.entities.AlumniProfileEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class AcademicDetailsEntityMapperTest {

    @Test
    public void testMapping() {
        // Create a sample AcademicDetailDTO object
        AcademicDetailDTO academicDetailDTO = new AcademicDetailDTO();
        academicDetailDTO.setDegree("Bachelor of Science");
        academicDetailDTO.setUniversity("Example University");

        // Mock the AlumniProfileEntity
        AlumniProfileEntity alumniProfileEntity = mock(AlumniProfileEntity.class);

        // Create an instance of AcademicDetailsEntityMapper
        AcademicDetailsEntityMapper mapper = new AcademicDetailsEntityMapper(alumniProfileEntity);

        // Perform the mapping
        AcademicDetailEntity academicDetailEntity = mapper.map(academicDetailDTO);

        // Verify that the mapping is done correctly
        assertEquals(academicDetailDTO.getDegree(), academicDetailEntity.getDegree());
    }
}
