package com.alumconnect.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MatchResponse {
        private List<ProfileDTO> matchedProfiles;
}

