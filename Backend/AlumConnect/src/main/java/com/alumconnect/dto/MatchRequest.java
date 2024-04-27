package com.alumconnect.dto;

import lombok.Data;

import java.util.List;

@Data
public class MatchRequest {
        private String email;
        private String role;
        private List<String> interests;
}


