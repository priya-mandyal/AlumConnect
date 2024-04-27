package com.alumconnect.mapper;

import com.alumconnect.dto.InterestDTO;
import com.alumconnect.entities.AlumniProfileEntity;
import com.alumconnect.entities.InterestEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class InterestEntityMapperTest {

    @Test
    public void testMapping() {
        // Create a sample InterestDTO object
        InterestDTO interestDTO = new InterestDTO();
        interestDTO.setLabel("Programming");

        // Mock the AlumniProfileEntity
        AlumniProfileEntity alumniProfileEntity = mock(AlumniProfileEntity.class);

        // Create an instance of InterestEntityMapper
        InterestEntityMapper mapper = new InterestEntityMapper(alumniProfileEntity);

        // Perform the mapping
        InterestEntity interestEntity = mapper.map(interestDTO);

        // Verify that the mapping is done correctly
        assertEquals(interestDTO.getLabel(), interestEntity.getInterest());
    }
}
