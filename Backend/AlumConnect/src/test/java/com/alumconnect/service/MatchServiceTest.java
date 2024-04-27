 package com.alumconnect.service;

 import com.alumconnect.dto.MatchRequest;
 import com.alumconnect.dto.MatchResponse;
 import com.alumconnect.entities.AlumniProfileEntity;
 import com.alumconnect.entities.InterestEntity;
 import com.alumconnect.entities.Student;
 import com.alumconnect.entities.User;
 import com.alumconnect.enums.Role;
 import com.alumconnect.repository.AlumniProfileRepository;
 import com.alumconnect.repository.InterestRepository;
 import com.alumconnect.repository.StudentRepository;
 import com.alumconnect.repository.UserRepository;
 import org.junit.jupiter.api.Test;
 import org.mockito.InjectMocks;
 import org.mockito.Mock;
 import org.springframework.boot.test.context.SpringBootTest;

 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.List;
 import java.util.Optional;

 import static org.junit.jupiter.api.Assertions.*;
 import static org.mockito.Mockito.mock;
 import static org.mockito.Mockito.when;

 @SpringBootTest
 class MatchServiceTest {

     @Mock

     private UserRepository userRepository;

     @Mock
     private InterestRepository interestRepository;

     @Mock
     private AlumniProfileRepository alumniProfileRepository;

     @Mock
     private StudentRepository studentRepository;

     @InjectMocks
     private MatchService matchService;

     @Test
     void testInitiateMatch_StudentRole() {

         User studentUser = new User();
         studentUser.setId(1L);
         studentUser.setRole(Role.STUDENT);
         when(userRepository.findById(1L)).thenReturn(Optional.of(studentUser));


         List<InterestEntity> interests = createInterestEntities();
         when(interestRepository.findByAlumniProfileIsNotNullAndInterest("Java")).thenReturn(interests);


         MatchRequest request = new MatchRequest();
         request.setRole("STUDENT");
         request.setInterests(Collections.singletonList("Java"));


         MatchResponse response = matchService.initiateMatch(1L, request);
         assertNotNull(response.getMatchedProfiles());
     }

     @Test
     void testInitiateMatch_AlumniRole() {

         User alumniUser = new User();
         alumniUser.setId(1L);
         alumniUser.setRole(Role.ALUMNI);
         when(userRepository.findById(1L)).thenReturn(Optional.of(alumniUser));

         List<InterestEntity> interestEntities = createInterestEntities();

         when(interestRepository.findByStudentIsNotNullAndInterest("Java")).thenReturn(interestEntities);


         MatchRequest request = new MatchRequest();
         request.setRole("ALUMNI");
         request.setInterests(Collections.singletonList("Java"));


         MatchResponse response = matchService.initiateMatch(1L, request);
         assertNotNull(response.getMatchedProfiles());
     }

     private List<InterestEntity> createInterestEntities() {

         List<InterestEntity> interestEntities = new ArrayList<>();
         InterestEntity interestEntity1 = new InterestEntity();
         interestEntity1.setId(1L);
         interestEntity1.setInterest("Java");
         AlumniProfileEntity alumniProfileEntity = new AlumniProfileEntity();
         User mockUser = mock(User.class);
         when(mockUser.getId()).thenReturn(1L);
         alumniProfileEntity.setUser(mockUser);
         alumniProfileEntity.setInterests(Collections.emptyList());
         interestEntity1.setAlumniProfile(alumniProfileEntity);
         Student student = new Student();
         student.setUser(mockUser);
         student.setInterests(Collections.emptyList());
         interestEntity1.setStudent(student);

         interestEntities.add(interestEntity1);


         return interestEntities;
     }
 }
