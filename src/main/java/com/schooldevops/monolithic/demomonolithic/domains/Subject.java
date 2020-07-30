package com.schooldevops.monolithic.demomonolithic.domains;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Subject {

    private Long id;
    private String name;
    private Long professorId;
    private Professor professor;
    private Integer credit;

}
