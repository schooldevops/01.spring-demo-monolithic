package com.schooldevops.monolithic.demomonolithic.domains;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
public class Lecture {

    private Long id;
    private Professor professor;
    private Subject subject;
    private List<AttendedSubject> attendedSubjects;
    private Integer limitStudents;
    private String state;

    public void addAttendedSubject(AttendedSubject attendedSubject) {
        if (attendedSubjects == null) {
            attendedSubjects = new ArrayList<>();
        }

        attendedSubjects.add(attendedSubject);
    }
}
