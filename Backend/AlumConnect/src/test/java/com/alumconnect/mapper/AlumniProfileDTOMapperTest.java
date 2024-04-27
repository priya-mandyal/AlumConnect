package com.alumconnect.mapper;

import com.alumconnect.dto.AlumniProfile;
import com.alumconnect.entities.AlumniProfileEntity;
import com.alumconnect.entities.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AlumniProfileDTOMapperTest {

    @Test
    public void testMapping() {
        // Create a sample AlumniProfileEntity object
        AlumniProfileEntity alumniProfileEntity = new AlumniProfileEntity();
        alumniProfileEntity.setAvailability("Available");
        alumniProfileEntity.setExpertiseAndSkills("Java, Spring Boot");
        alumniProfileEntity.setProfessionalJourney("Software Engineer");

        // Mock the associated UserEntity
        User userEntity = mock(User.class);
        when(userEntity.getEmailId()).thenReturn("example@example.com");
        alumniProfileEntity.setUser(userEntity);

        // Create an instance of AlumniProfileDTOMapper
        AlumniProfileDTOMapper mapper = new AlumniProfileDTOMapper();

        // Perform the mapping
        AlumniProfile alumniProfile = mapper.map(alumniProfileEntity);

        // Verify that the mapping is done correctly
        assertEquals(alumniProfileEntity.getAvailability(), alumniProfile.getAvailability());
    }
}
