package com.schooldevops.monolithic.demomonolithic.services;

import com.schooldevops.monolithic.demomonolithic.domains.*;
import com.schooldevops.monolithic.demomonolithic.repositories.AttendedSubjectRepository;
import com.schooldevops.monolithic.demomonolithic.repositories.LectureRepository;
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
public class LectureService {

    private final SubjectService subjectService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final LectureRepository lectureRepository;
    private final AttendedSubjectRepository attendedSubjectRepository;

    public LectureService(SubjectService subjectService, ProfessorService professorService, StudentService studentService, LectureRepository lectureRepository, AttendedSubjectRepository attendedSubjectRepository) {
        this.subjectService = subjectService;
        this.professorService = professorService;
        this.studentService = studentService;
        this.lectureRepository = lectureRepository;
        this.attendedSubjectRepository = attendedSubjectRepository;
    }

    /**
     * 교과 과정을 생성한다.
     * @param subjectId    과목 아이디
     * @param limitStudent 교과 과정의 제한 인원
     * @return 생성된 교과과정
     */
    public Lecture createLecture(Long subjectId, Integer limitStudent) {

        Lecture lecture = new Lecture();
        Subject subjectInfo = getSubjectInfo(subjectId);

        lecture.setSubject(subjectInfo);
        lecture.setProfessor(getProfessorById(subjectInfo.getProfessorId()));
        lecture.setAttendedSubjects(List.of());
        lecture.setLimitStudents(limitStudent);
        lecture.setState("APPLY");

        return lectureRepository.save(lecture);
    }

    /**
     * Lecture 의 상태를 수정한다.
     * @param lectureId Lecture 상태를 수정할 id
     * @param lecture 수정 정보
     * @return 수정된 Lecture
     */
    public Lecture modifyLecture(Long lectureId, Lecture lecture) {
        Lecture existsLecture = lectureRepository.findById(lectureId);
        if (existsLecture == null) {
            throw new RuntimeException("Resource Not Found");
        }
        if (lecture.getLimitStudents() != null) {
            existsLecture.setLimitStudents(lecture.getLimitStudents());
        }

        if (lecture.getState() != null) {
            existsLecture.setState(lecture.getState());
        }

        return lectureRepository.save(existsLecture);
    }

    /**
     * 교과 과목을 삭제한다.
     * @param lectureId 삭제할 교과 과목 아이디
     */
    public void deleteLecture(Long lectureId) {
        Lecture existsLecture = lectureRepository.findById(lectureId);
        if (existsLecture == null) {
            throw new RuntimeException("Resource Not Found");
        }

        if (existsLecture.getAttendedSubjects() != null && existsLecture.getAttendedSubjects().size() > 0) {
            throw new RuntimeException("Not delete lecture because already lecture has attendedSubjects.");
        }

        lectureRepository.delete(lectureId);
    }

    public Lecture applyAttendedSubject(Long lectureId, Long studentId) {
        Student student = studentService.findById(studentId);
        if (student == null) {
            throw new RuntimeException("Resource Not Found");
        }

        Lecture existsLecture = lectureRepository.findById(lectureId);
        if (existsLecture == null) {
            throw new RuntimeException("Resource Not Found");
        }

        AttendedSubject attendedSubject = new AttendedSubject();
        attendedSubject.setSubjectId(existsLecture.getSubject().getId());
        attendedSubject.setStudent(student);
        attendedSubject.setGrade("None");
        attendedSubject.setState("APPLY");

        AttendedSubject savedAttendedSubject = attendedSubjectRepository.save(attendedSubject);

        return addAttendedSubject(lectureId, savedAttendedSubject);

    }

    public Lecture addAttendedSubject(Long lectureId, AttendedSubject attendedSubject) {
        Lecture existsLecture = lectureRepository.findById(lectureId);
        if (existsLecture == null) {
            throw new RuntimeException("Resource Not Found");
        }

        existsLecture.addAttendedSubject(attendedSubject);

        return lectureRepository.save(existsLecture);
    }

    public boolean removeAttendedSubject(Long lectureId, Long attendedId) {
        Lecture existsLecture = lectureRepository.findById(lectureId);
        if (existsLecture == null) {
            throw new RuntimeException("Resource Not Found");
        }

        List<AttendedSubject> attendedSubjects = existsLecture.getAttendedSubjects();
        if (attendedSubjects == null || attendedSubjects.size() == 0) return true;

        return attendedSubjects.removeIf(item -> item.getId() == attendedId);
    }

    /**
     * 교수 아이디로 교수 정보를 조회한다.
     * @param professorId 교수 아이디
     * @return  교수 정보
     */
    private Professor getProfessorById(Long professorId) throws RuntimeException {
        Professor professor = professorService.findById(professorId);
        if (professor == null) {
            throw new RuntimeException("Resource Not Found");
        }
        return professor;
    }

    /**
     * 과목 아이디로 과목 정보를 조회한다.
     * @param subjectId 과목 아이디
     * @return 과목 정보
     * @throws RuntimeException 과목을 찾지 못한경우 예외 발생
     */
    private Subject getSubjectInfo(Long subjectId) throws RuntimeException{
        Subject subject = subjectService.findById(subjectId);
        if (subject == null) {
            throw new RuntimeException("Resource Not Found");
        }
        return subject;
    }
}
