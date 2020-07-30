package com.schooldevops.monolithic.demomonolithic.repositories;

import com.schooldevops.monolithic.demomonolithic.domains.Subject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class SubjectRepository {

    /**
     * DB 를 사용하지 않기 때문에 과목 아이디를 관리하기 위해서 AtomicLong 을 활용한다.
     * AtomicLong 을 사용하면 ThreadSafe 한 아이디를 할당받을 수 있다.
     */
    private AtomicLong subjectId = new AtomicLong(3L);

    /**
     * 과목정보 저장할 Map 을 생성한다.
     */
    private Map<Long, Subject> subjectDB = new HashMap<>();

    /**
     * 과목 데이터를 초기화 한다.
     */
    public SubjectRepository() {
        subjectDB.put(1L, new Subject(1L, "Basic Computer Science", 1L, null, 3));
        subjectDB.put(2L, new Subject(2L, "Machine Learning", 1L, null, 3));
        subjectDB.put(3L, new Subject(3L, "Modeling", 2L, null, 3));

    }

    /**
     * 과목 아이디로 학생 정보를 조회한다.
     * @param subjectId 조회할 학생 아이디
     * @return 학생 정보
     */
    public Subject findById(Long subjectId) {
        return subjectDB.get(subjectId);
    }

    /**
     * 전체 과목 목록을 반환한다.
     * Java Stream 을 이용하여 아이디의 역순으로 조회하고 있다.
     * @return 전체 과목 목록
     */
    public List<Subject> findAll() {
        return subjectDB.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByKey())).map(item -> item.getValue()).collect(Collectors.toList());
    }

    /**
     * 과목 정보를 저장하거나 수정한다.
     * @param subject 저장 혹은 수정할 학생 정보
     * @return 저장이나 수정된 학생 정보
     */
    public Subject save(Subject subject) {
        if (isExistSubject(subject)) {
            subjectDB.put(subject.getId(), subject);
        }
        else {
            long id = subjectId.addAndGet(1);
            subject.setId(id);
            subjectDB.put(id, subject);
        }
        return subject;
    }

    /**
     * 과목 정보가 존재하는지 검사한다.
     * 아이디가 존재하는지만 검사한다.
     * @param subject 과목 정보
     * @return 과목 아이디가 존재한다면 true
     */
    private boolean isExistSubject(Subject subject) {
        return subject != null && subject.getId() != null && subject.getId() != 0L;
    }

    /**
     * 과목 아이디로 과목 정보를 삭제한다.
     * @param subjectId 삭제할 과목 아이디
     */
    public void delete(Long subjectId) {
        subjectDB.remove(subjectId);
    }

}
