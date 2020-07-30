package com.schooldevops.monolithic.demomonolithic.services;

import com.schooldevops.monolithic.demomonolithic.domains.Professor;
import com.schooldevops.monolithic.demomonolithic.domains.Subject;
import com.schooldevops.monolithic.demomonolithic.repositories.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Slf4j 는 Lombok 과 연동된 로거이다. Lombok 을 활용하면 편리하게 로깅을 남길 수 있다.
 * @Service 는 Spring Framework 의 Stereotype 으로 서비스 레이어에 대한 Component 라고 알려준다.
 *          이렇게 되면 Spring Framework 가 관리하는 Bean 으로 등록된다.
 */
@Slf4j
@Service
public class SubjectService {

    /**
     * SubjectRepository 를 생성자를 통한 의존성 주입을 수행하고 있다.
     */
    private final SubjectRepository subjectRepository;
    private final ProfessorService professorService;

    public SubjectService(SubjectRepository subjectRepository, ProfessorService professorService) {
        this.subjectRepository = subjectRepository;
        this.professorService = professorService;
    }


    /**
     * 과목 아이디로 학생 정보 조회
     * @param subjectId 학생 아이디
     * @return 조회된 학생 정보
     */
    public Subject findById(Long subjectId) {
        Subject existsSubject = subjectRepository.findById(subjectId);
        if (existsSubject != null) {
            addProfessorInfoToSubject(existsSubject);
        }

        return existsSubject;
    }

    /**
     * 교수 정보를 조회하여 subject 에 추가한다.
     * @param subject
     */
    private void addProfessorInfoToSubject(Subject subject) {
        Professor professor = professorService.findById(subject.getProfessorId());
        subject.setProfessor(professor);
    }

    /**
     * 전체 과목 목록을 반환한다.
     * @return 전체 과목 목록
     */
    public List<Subject> findAll() {
        List<Subject> subjects = subjectRepository.findAll();
        if (subjects != null && subjects.size() > 0) {
            subjects.stream().forEach(item -> {
                addProfessorInfoToSubject(item);
            });
        }
        return subjects;
    }

    /**
     * 과목 등록을 수행하는 서비스이다.
     * 서비스에서는 이렇게 비즈니스 로직을 수행할 수 있다. 정보의 존재여부, 아이디 존재여부 등 검사.
     * @param subject 등록할 학생 정보
     * @return 등록된 학생 정보, 등록이 되면 아이디와 등록일이 함께 저장된다.
     */
    public Subject applySubject(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject can not be null, when you save it.");
        }
        if (subject.getId() != null) {
            throw new IllegalArgumentException("Subject id must be null, when you save it.");
        }

        Subject savedSubject = subjectRepository.save(subject);
        addProfessorInfoToSubject(savedSubject);
        return savedSubject;
    }

    /**
     * 과목 정보를 수정한다.
     * @param subjectId 수정할 과목 아이디
     * @param subject 수정할 데이터
     * @return 수정된 과목 정보
     */
    public Subject modifySubject(Long subjectId, Subject subject) {
        if (subjectId == null || subjectId == 0L) {
            throw new IllegalArgumentException("Subject id can not be null, when you modify subject info.");
        }

        Subject targetSubject = setModifyInfo(subjectId, subject);

        return subjectRepository.save(targetSubject);
    }

    /**
     * 과목정보 수정시 수정해야할 데이터를 세팅한다.
     * 과목 정보를 우선 조회하고, 수정해야할 데이터가 있는 필드에만 수정될 값을 할당한다.
     * @param subjectId 과목 아이디
     * @param subject 수정할 정보
     * @return 수정된 과목 정보
     */
    private Subject setModifyInfo(Long subjectId, Subject subject) {
        Subject existsSubject = subjectRepository.findById(subjectId);

        if (subject.getName() != null) {
            existsSubject.setName(subject.getName());
        }

        if (subject.getCredit() != null) {
            existsSubject.setCredit(subject.getCredit());
        }

        if (subject.getProfessorId() != null) {
            existsSubject.setProfessorId(subject.getProfessorId());
        }
        return existsSubject;
    }

    /**
     * 과목 아이디로 과목 정보를 삭제한다.
     * @param subjetId 학생 아이디
     */
    public void deleteById(Long subjetId) {
        subjectRepository.delete(subjetId);
    }
}

