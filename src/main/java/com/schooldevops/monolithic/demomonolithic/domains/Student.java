package com.schooldevops.monolithic.demomonolithic.domains;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Student {

    private Long id;
    private String name;
    private Integer age;
    private String major;
    private LocalDateTime entranceAt;

}

