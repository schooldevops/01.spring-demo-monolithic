package com.schooldevops.monolithic.demomonolithic.repositories;

import com.schooldevops.monolithic.demomonolithic.domains.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Repository
public class LectureRepository {
    /**
     * DB 를 사용하지 않기 때문에 수강등록 정보 아이디를 관리하기 위해서 AtomicLong 을 활용한다.
     * AtomicLong 을 사용하면 ThreadSafe 한 아이디를 할당받을 수 있다.
     */
    private AtomicLong lectureIds = new AtomicLong(3L);

    /**
     * 수강등록 정보 저장할 Map 을 생성한다.
     */
    private Map<Long, Lecture> lectureDB = new HashMap<>();

    /**
     * 수강 데이터를 초기화 한다.
     */
    public LectureRepository() {
        Professor professor01 = new Professor(1L, "Prof-KIDO", "Computer Science", LocalDateTime.now());
        Professor professor02 = new Professor(2L, "Madona", "Music", LocalDateTime.now());

        Subject subject01 = new Subject(1L, "Basic Computer Science", 1L, null, 3);
        Subject subject02 = new Subject(2L, "Machine Learning", 1L, null, 3);
        Subject subject03 = new Subject(3L, "Modeling", 2L, null, 3);

        Student studentKido = new Student(1L, "KIDO", 20, "ComputerScience", LocalDateTime.now());
        Student studentManDo = new Student(2L, "ManDo", 35, "Math", LocalDateTime.now());

        AttendedSubject attendedSubject01 = new AttendedSubject(1L, 1L, studentKido, "None", "APPLY");
        AttendedSubject attendedSubject02 = new AttendedSubject(2L, 1L, studentManDo, "None", "APPLY");
        AttendedSubject attendedSubject03 = new AttendedSubject(3L, 2L, studentKido, "A+", "DONE");

        lectureDB.put(1L, new Lecture(1L, professor01, subject01, List.of(attendedSubject01, attendedSubject02), 10, "APPLY" ));
        lectureDB.put(2L, new Lecture(2L, professor01, subject02, List.of(attendedSubject03), 10, "APPLY" ));
        lectureDB.put(3L, new Lecture(3L, professor02, subject03, List.of(attendedSubject02, attendedSubject03), 5, "DONE"));
    }

    /**
     * 수강 아이디로 수강 정보를 조회한다.
     * @param lectureId 조회할 수강 아이디
     * @return 수강 정보
     */
    public Lecture findById(Long lectureId) {
        return lectureDB.get(lectureId);
    }

    /**
     * 수강 정보를 저장하거나 수정한다.
     * @param lecture 저장 혹은 수정할 수강 정보
     * @return 저장이나 수정된 수강 정보
     */
    public Lecture save(Lecture lecture) {
        if (isExistsLecture(lecture)) {
            lectureDB.put(lecture.getId(), lecture);
        }
        else {
            long id = lectureIds.addAndGet(1);
            lecture.setId(id);
            lectureDB.put(id, lecture);
        }
        return lecture;
    }

    /**
     * 수강 정보가 존재하는지 검사한다.
     * 아이디가 존재하는지만 검사한다.
     * @param lecture 수강 정보
     * @return 수강정보 아이디가 존재한다면 true
     */
    private boolean isExistsLecture(Lecture lecture) {
        return lecture != null && lecture.getId() != null && lecture.getId() != 0L;
    }

    /**
     * 수강 아이디로 수강 정보를 삭제한다.
     * @param lectureId 삭제할 수강 아이디
     */
    public void delete(Long lectureId) {
        lectureDB.remove(lectureId);
    }

}
