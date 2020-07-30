package com.schooldevops.monolithic.demomonolithic.domains;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
public class Professor {

    private Long id;
    private String name;
    private String major;
    private LocalDateTime joinedAt;

}