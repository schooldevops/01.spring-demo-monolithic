package com.schooldevops.monolithic.demomonolithic.repositories;

import com.schooldevops.monolithic.demomonolithic.domains.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @Slf4j 는 Lombok 과 연동된 로거이다. Lombok 을 활용하면 편리하게 로깅을 남길 수 있다.
 * @Repository 는 Spring Framework 의 Stereotype 으로 리포지토리 레이어에 대한 Component 라고 알려준다.
 *          이렇게 되면 Spring Framework 가 관리하는 Bean 으로 등록된다.
 */
@Slf4j
@Repository
public class StudentRepository {

    /**
     * DB 를 사용하지 않기 때문에 학생 아이디를 관리하기 위해서 AtomicLong 을 활용한다.
     * AtomicLong 을 사용하면 ThreadSafe 한 아이디를 할당받을 수 있다.
     */
    private AtomicLong studentID = new AtomicLong(3L);

    /**
     * 학생 정보를 저장할 Map 을 생성한다.
     */
    private Map<Long, Student> studentDB = new HashMap<>();

    /**
     * 학생 데이터를 초기화 한다.
     */
    public StudentRepository() {
        studentDB.put(1L, new Student(1L, "KIDO", 20, "ComputerScience", LocalDateTime.now()));
        studentDB.put(2L, new Student(2L, "ManDo", 35, "Math", LocalDateTime.now()));
        studentDB.put(3L, new Student(3L, "Jobs", 40, "Job", LocalDateTime.now()));

    }

    /**
     * 학생 아이디로 학생 정보를 조회한다.
     * @param studentId 조회할 학생 아이디
     * @return 학생 정보
     */
    public Student findById(Long studentId) {
        return studentDB.get(studentId);
    }

    /**
     * 전체 학생 목록을 반환한다.
     * Java Stream 을 이용하여 아이디의 역순으로 조회하고 있다.
     * @return 전체 학생 목록
     */
    public List<Student> findAll() {
        return studentDB.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByKey())).map(item -> item.getValue()).collect(Collectors.toList());
    }

    /**
     * 전공에 해당하는 학생 목록을 반환한다.
     * Java Stream 을 활용하여 전공명이 동일한 학생만 필터링 하고 있다.
     * @param subjectName 전공과목 명
     * @return 전공에 해당하는 학생 목록
     */
    public List<Student> findByMajor(String subjectName) {

        log.info(String.format("Repository Call by Subject %s", subjectName));
        return studentDB.entrySet().stream().filter(item -> item.getValue().getMajor().equals(subjectName)).map(item -> item.getValue()).collect(Collectors.toList());
    }

    /**
     * 학생 정보를 저장하거나 수정한다.
     * @param student 저장 혹은 수정할 학생 정보
     * @return 저장이나 수정된 학생 정보
     */
    public Student save(Student student) {
        if (isExistStudent(student)) {
            studentDB.put(student.getId(), student);
        }
        else {
            long id = studentID.addAndGet(1);
            student.setId(id);
            studentDB.put(id, student);
        }
        return student;
    }

    /**
     * 학생 정보가 존재하는지 검사한다.
     * 아이디가 존재하는지만 검사한다.
     * @param student 학생 정보
     * @return 학생 아이디가 존재한다면 true
     */
    private boolean isExistStudent(Student student) {
        return student != null && student.getId() != null && student.getId() != 0L;
    }

    /**
     * 학생 아이디로 학생 정보를 삭제한다.
     * @param studentId 삭제할 학생 아이디
     */
    public void delete(Long studentId) {
        studentDB.remove(studentId);
    }
}