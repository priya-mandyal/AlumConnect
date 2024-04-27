package com.alumconnect.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class UserPost {
    private long id;
    private String title;
    private String text;
    private String imageUrl;
    private long userId;
    private String firstName;
    private String lastName;
}
