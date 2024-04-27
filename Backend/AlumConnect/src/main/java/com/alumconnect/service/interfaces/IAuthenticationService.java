package com.alumconnect.service.interfaces;

import com.alumconnect.dto.ACResponse;
import com.alumconnect.dto.RegisterRequest;

public interface IAuthenticationService {
    ACResponse<Object> register(RegisterRequest request);
}
