package com.alumconnect.mapper;

import com.alumconnect.dto.AlumniProfile;
import com.alumconnect.entities.AlumniProfileEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlumniProfileEntityMapperTest {

    @Test
    public void testMapping() {
        // Create a sample AlumniProfileDTO object
        AlumniProfile alumniProfileDTO = new AlumniProfile();
        alumniProfileDTO.setAvailability("Available");

        // Create an instance of AlumniProfileEntity
        AlumniProfileEntity alumniProfileEntity = new AlumniProfileEntity();

        // Create an instance of AlumniProfileEntityMapper
        AlumniProfileEntityMapper mapper = new AlumniProfileEntityMapper(alumniProfileEntity);

        // Perform the mapping
        AlumniProfileEntity mappedEntity = mapper.map(alumniProfileDTO);

        // Verify that the mapping is done correctly
        assertEquals(alumniProfileDTO.getAvailability(), mappedEntity.getAvailability());
    }
}
