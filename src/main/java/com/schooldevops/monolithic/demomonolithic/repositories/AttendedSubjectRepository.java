package com.schooldevops.monolithic.demomonolithic.repositories;

import com.schooldevops.monolithic.demomonolithic.domains.AttendedSubject;
import com.schooldevops.monolithic.demomonolithic.domains.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Repository
public class AttendedSubjectRepository {
    /**
     * DB 를 사용하지 않기 때문에 수강등록 정보 아이디를 관리하기 위해서 AtomicLong 을 활용한다.
     * AtomicLong 을 사용하면 ThreadSafe 한 아이디를 할당받을 수 있다.
     */
    private AtomicLong attendedSubjectIds = new AtomicLong(3L);

    /**
     * 수강등록 정보 저장할 Map 을 생성한다.
     */
    private Map<Long, AttendedSubject> attendedSubjectDB = new HashMap<>();

    /**
     * 수강 데이터를 초기화 한다.
     */
    public AttendedSubjectRepository() {
        Student studentKido = new Student(1L, "KIDO", 20, "ComputerScience", LocalDateTime.now());
        Student studentManDo = new Student(2L, "ManDo", 35, "Math", LocalDateTime.now());
        attendedSubjectDB.put(1L, new AttendedSubject(1L, 1L, studentKido, "None", "APPLY"));
        attendedSubjectDB.put(2L, new AttendedSubject(2L, 1L, studentManDo, "None", "APPLY" ));
        attendedSubjectDB.put(3L, new AttendedSubject(3L, 2L, studentKido, "A+", "DONE"));
    }

    /**
     * 수강 아이디로 수강 정보를 조회한다.
     * @param attendedSubjectId 조회할 학생 아이디
     * @return 학생 정보
     */
    public AttendedSubject findById(Long attendedSubjectId) {
        return attendedSubjectDB.get(attendedSubjectId);
    }

    /**
     * 수강 정보를 저장하거나 수정한다.
     * @param attendedSubject 저장 혹은 수정할 수강 정보
     * @return 저장이나 수정된 수강 정보
     */
    public AttendedSubject save(AttendedSubject attendedSubject) {
        if (isExistAttendedSubject(attendedSubject)) {
            attendedSubjectDB.put(attendedSubject.getId(), attendedSubject);
        }
        else {
            long id = attendedSubjectIds.addAndGet(1);
            attendedSubject.setId(id);
            attendedSubjectDB.put(id, attendedSubject);
        }
        return attendedSubject;
    }

    /**
     * 수강 정보가 존재하는지 검사한다.
     * 아이디가 존재하는지만 검사한다.
     * @param attendedSubject 과목 정보
     * @return 수강정보 아이디가 존재한다면 true
     */
    private boolean isExistAttendedSubject(AttendedSubject attendedSubject) {
        return attendedSubject != null && attendedSubject.getId() != null && attendedSubject.getId() != 0L;
    }

    /**
     * 수강 아이디로 수강 정보를 삭제한다.
     * @param attendedSubjectId 삭제할 수강 아이디
     */
    public void delete(Long attendedSubjectId) {
        attendedSubjectDB.remove(attendedSubjectId);
    }

}
