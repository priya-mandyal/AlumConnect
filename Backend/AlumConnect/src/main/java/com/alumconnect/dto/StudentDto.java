package com.alumconnect.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentDto {
    private long id;
    private String studentSummary;
    private List<AcademicDetailDTO> academicDetails;
    private List<InterestDTO> interests;
    private String email;
    private String hobbyForm;
    private String imageUrl;
}