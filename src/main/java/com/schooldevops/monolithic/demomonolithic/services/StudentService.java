package com.schooldevops.monolithic.demomonolithic.services;

import com.schooldevops.monolithic.demomonolithic.domains.Student;
import com.schooldevops.monolithic.demomonolithic.repositories.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Slf4j 는 Lombok 과 연동된 로거이다. Lombok 을 활용하면 편리하게 로깅을 남길 수 있다.
 * @Service 는 Spring Framework 의 Stereotype 으로 서비스 레이어에 대한 Component 라고 알려준다.
 *          이렇게 되면 Spring Framework 가 관리하는 Bean 으로 등록된다.
 */
@Slf4j
@Service
public class StudentService {

    /**
     * StudentRepository 를 생성자를 통한 의존성 주입을 수행하고 있다.
     */
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * 학생 아이디로 학생 정보 조회
     * @param studentId 학생 아이디
     * @return 조회된 학생 정보
     */
    public Student findById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    /**
     * 전체 학생 목록을 반환한다.
     * @return 전체 학생 목록
     */
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    /**
     * 전공 과목에 해당하는 학생 목록 조회
     * @param subjectName 전공 이름
     * @return 학생 목록
     */
    public List<Student> findByMajor(String subjectName) {
        log.info(String.format("Service Call by Subject %s", subjectName));
        return studentRepository.findByMajor(subjectName);
    }

    /**
     * 학생 등록을 수행하는 서비스이다.
     * 서비스에서는 이렇게 비즈니스 로직을 수행할 수 있다. 정보의 존재여부, 아이디 존재여부 등 검사.
     * @param student 등록할 학생 정보
     * @return 등록된 학생 정보, 등록이 되면 아이디와 등록일이 함께 저장된다.
     */
    public Student joinStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student can not be null, when you join in.");
        }
        if (student.getId() != null) {
            throw new IllegalArgumentException("Student id must be null, when you join in.");
        }

        student.setEntranceAt(LocalDateTime.now());
        return studentRepository.save(student);
    }

    /**
     * 학생 정보를 수정한다.
     * @param studentId 수정할 학생 아이디
     * @param student 수정할 데이터
     * @return 수정된 학생 정보
     */
    public Student modifyStudent(Long studentId, Student student) {
        if (studentId == null || studentId == 0L) {
            throw new IllegalArgumentException("Student id can not be null, when you modify student info.");
        }

        Student targetStudent = setModifyInfo(studentId, student);

        return studentRepository.save(targetStudent);
    }

    /**
     * 학생정보 수정시 수정해야할 데이터를 세팅한다.
     * 학생 정보를 우선 조회하고, 수정해야할 데이터가 있는 필드에만 수정될 값을 할당한다.
     * @param studentId 학생 아이디
     * @param student 수정할 정보
     * @return 수정된 학생 정보
     */
    private Student setModifyInfo(Long studentId, Student student) {
        Student existsStudent = studentRepository.findById(studentId);
        if (student.getMajor() != null) {
            existsStudent.setMajor(student.getMajor());
        }

        if (student.getName() != null) {
            existsStudent.setName(student.getName());
        }

        return existsStudent;
    }

    /**
     * 학생 아이디로 학생 정보를 삭제한다.
     * @param studentId 학생 아이디
     */
    public void deleteById(Long studentId) {
        studentRepository.delete(studentId);
    }
}
