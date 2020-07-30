package com.schooldevops.monolithic.demomonolithic.resources;

import com.schooldevops.monolithic.demomonolithic.domains.Professor;
import com.schooldevops.monolithic.demomonolithic.services.ProfessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/professors")
public class ProfessorController {

    private final ProfessorService professorService;

    @Autowired
    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping
    public List<Professor> findAll() {
        return professorService.findAll();
    }

    @GetMapping("/{id}")
    public Professor findById(@PathVariable("id") Long id) {
        return professorService.findById(id);
    }

    @GetMapping("/subjects/{subjectName}")
    public List<Professor> findBySubjectName(@PathVariable("subjectName") String subjectName) {
        log.info(String.format("Resource Call by Subject %s", subjectName));
        return professorService.findBySubjectName(subjectName);
    }

    @PostMapping
    public Professor join(@RequestBody Professor professor) {
        return professorService.join(professor);
    }

    @PutMapping("/{id}")
    public Professor modify(@PathVariable("id") Long id, @RequestBody Professor professor) {
        return professorService.modify(id, professor);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        professorService.delete(id);
    }
}
