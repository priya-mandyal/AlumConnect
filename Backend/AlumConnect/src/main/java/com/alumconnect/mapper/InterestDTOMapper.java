package com.alumconnect.mapper;

import com.alumconnect.dto.InterestDTO;
import com.alumconnect.entities.InterestEntity;
import com.alumconnect.mapper.interfaces.MapperObject;

public class InterestDTOMapper implements MapperObject<InterestEntity, InterestDTO> {
    @Override
    public InterestDTO map(InterestEntity interestEntity) {
        InterestDTO interestDTO = new InterestDTO();
        interestDTO.setLabel(interestEntity.getInterest());
        interestDTO.setValue(interestEntity.getInterest());
        return interestDTO;
    }
}
