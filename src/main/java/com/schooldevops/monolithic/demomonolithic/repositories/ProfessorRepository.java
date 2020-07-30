package com.schooldevops.monolithic.demomonolithic.repositories;

import com.schooldevops.monolithic.demomonolithic.domains.Professor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ProfessorRepository {

    private AtomicLong professorId = new AtomicLong(3);

    private Map<Long, Professor> professorsDB = new HashMap<>();

    public ProfessorRepository() {
        Map<Long, Professor> professors = Map.of(
                1L, new Professor(1L, "Prof-KIDO", "Computer Science", LocalDateTime.now()),
                2L, new Professor(2L, "Madona", "Music", LocalDateTime.now()),
                3L, new Professor(3L, "Alibaba", "Math", LocalDateTime.now())
        );

        professorsDB.putAll(professors);
    }

    public List<Professor> findAll() {
        return professorsDB.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByKey())).map(item -> item.getValue()).collect(Collectors.toList());
    }

    public Professor findById(Long id) {
        return professorsDB.get(id);
    }

    public List<Professor> findBySubjectName(String subjectName) {
        log.info(String.format("Repository Call by Subject %s", subjectName));
        return professorsDB.entrySet().stream().filter(item -> item.getValue().getMajor().equals(subjectName)).map(item -> item.getValue()).collect(Collectors.toList());
    }

    public Professor save(Professor professor) {
        if (professor != null && professor.getId() != null && professor.getId() != 0) {
            professorsDB.put(professor.getId(), professor);
        }
        else {
            long id = professorId.addAndGet(1);
            professor.setId(id);
            professorsDB.put(id, professor);
        }

        return professor;
    }

    public void delete(Long id) {
        professorsDB.remove(id);
    }
}

