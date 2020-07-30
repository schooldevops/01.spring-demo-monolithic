package com.schooldevops.monolithic.demomonolithic.services;

import com.schooldevops.monolithic.demomonolithic.domains.Professor;
import com.schooldevops.monolithic.demomonolithic.repositories.ProfessorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public List<Professor> findAll() {
        return professorRepository.findAll();
    }

    public Professor findById(Long id) {
        return professorRepository.findById(id);
    }

    public List<Professor> findBySubjectName(String subjectName) {
        log.info(String.format("Service Call by Subject %s", subjectName));
        return professorRepository.findBySubjectName(subjectName);
    }

    public Professor join(Professor professor) {
        if (professor != null && professor.getId() != null) {
            throw new IllegalArgumentException("Professor id must be null, when you join in.");
        }
        professor.setJoinedAt(LocalDateTime.now());
        return professorRepository.save(professor);
    }

    public Professor modify(Long professorId, Professor professor) {
        if (professorId == null || professor == null) {
            throw new IllegalArgumentException("Professor id can not be null, when you update it");
        }

        Professor existsProfessor = setProfessorUpdateInfo(professorId, professor);

        return professorRepository.save(existsProfessor);
    }

    private Professor setProfessorUpdateInfo(Long professorId, Professor professor) {
        Professor existsProfessor = professorRepository.findById(professorId);
        if (professor.getName() != null) {
            existsProfessor.setName(professor.getName());
        }

        if (professor.getMajor() != null) {
            existsProfessor.setMajor(professor.getMajor());
        }
        return existsProfessor;
    }

    public void delete(Long id) {
        professorRepository.delete(id);
    }
}
