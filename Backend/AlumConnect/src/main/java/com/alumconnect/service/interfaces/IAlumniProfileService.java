package com.alumconnect.service.interfaces;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.AlumniProfile;

public interface IAlumniProfileService {
    ACResponse<Object> createAlumniProfile(AlumniProfile alumniProfileDTO);
}