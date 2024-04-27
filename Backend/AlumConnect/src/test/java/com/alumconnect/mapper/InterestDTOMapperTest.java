package com.alumconnect.mapper;

import com.alumconnect.dto.InterestDTO;
import com.alumconnect.entities.InterestEntity;
import com.alumconnect.mapper.InterestDTOMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterestDTOMapperTest {

    @Test
    public void testMapping() {
        // Create a sample InterestEntity object
        InterestEntity interestEntity = new InterestEntity();
        interestEntity.setInterest("Programming");

        // Create an instance of InterestDTOMapper
        InterestDTOMapper mapper = new InterestDTOMapper();

        // Perform the mapping
        InterestDTO interestDTO = mapper.map(interestEntity);

        // Verify that the mapping is done correctly
        assertEquals(interestEntity.getInterest(), interestDTO.getLabel());
    }
}
