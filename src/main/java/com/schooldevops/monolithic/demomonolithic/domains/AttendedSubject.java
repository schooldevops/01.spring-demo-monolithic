package com.schooldevops.monolithic.demomonolithic.domains;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AttendedSubject {

    private Long id;
    private Long subjectId;
    private Student student;
    private String grade;
    private String state;
}
