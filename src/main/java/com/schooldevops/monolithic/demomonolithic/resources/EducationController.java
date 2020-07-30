package com.schooldevops.monolithic.demomonolithic.resources;

import com.schooldevops.monolithic.demomonolithic.domains.Lecture;
import com.schooldevops.monolithic.demomonolithic.domains.Subject;
import com.schooldevops.monolithic.demomonolithic.services.LectureService;
import com.schooldevops.monolithic.demomonolithic.services.SubjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @RequestMapping을 통해서 엔드포인트를 /education 로 시작하게 설정한다.
 * @RestController 은 Spring Framework 으로 REST API 를 만들때, 응답을 View 가 아닌 @ResponseBody로 반환하도록 해준다.
 */
@RequestMapping("/education")
@RestController
public class EducationController {

    private final SubjectService subjectService;
    private final LectureService lectureService;

    public EducationController(SubjectService subjectService, LectureService lectureService) {
        this.subjectService = subjectService;
        this.lectureService = lectureService;
    }

    /**
     * 과목 등록
     * @RequestBody 를 이용하여, 클라이언트에서 전달된 JSON이 Object 로 매핑되도록 해준다.
     * @param student 프런트엔드로 부터 전달된 학생 정보 (JSON 을 Object 로 자동 매핑된다.)
     * @return 등록된후 ID가 할당된 과목 정보를 반환한다.
     */
    @PostMapping("/subjects")
    public Subject applySubject(@RequestBody Subject student) {
        return subjectService.applySubject(student);
    }

    /**
     * 과목 정보 수정
     * @RequestBody 를 이용하여, 클라이언트에서 전달된 JSON이 Object 로 매핑되도록 해준다.
     * @param id 수정을 원하는 과목 아이디
     * @param subject 수정을 되어야할 정보를 담고 있는 과목 정보
     * @return 수정된 버젼의 과목 정보가 반환된다.
     */
    @PutMapping("/subjects/{id}")
    public Subject modifySubject(@PathVariable("id") Long id, @RequestBody Subject subject) {
        return subjectService.modifySubject(id, subject);
    }

    /**
     * 전체 과목 목록을 반환한다.
     * @return 등록된 모든 과목 정보를 반환한다. (실제 프로젝트에서는 페이징을 적용해야한다.)
     */
    @GetMapping("/subjects")
    public List<Subject> findAllSubjects() {
        return subjectService.findAll();
    }

    /**
     * 과목 아이디로 과목 정보 조회
     * @param id 조회할 과목 아이디
     * @return 조회된 과목 정보를 반환한다.
     */
    @GetMapping("/subjects/{id}")
    public Subject findSubjectById(@PathVariable("id") Long id) {
        return subjectService.findById(id);
    }

    /**
     * 과목 아이디로 과목정보 제거 수행
     * @param id 삭제할 과목 아이디
     */
    @DeleteMapping("/subjects/{id}")
    public void deleteSubjectById(@PathVariable("id") Long id) {
        subjectService.deleteById(id);
    }

    /**
     * 교과 과목 등록
     * @param subjectId 과목 아이디
     * @param limitStudents 학생 제한수
     * @return 생성된 교과 과목
     */
    @PostMapping("/lectures/{subjectId}")
    public Lecture createLecture(@PathVariable("subjectId") Long subjectId, @RequestParam Integer limitStudents) {
        return lectureService.createLecture(subjectId, limitStudents);
    }

    /**
     * 교과 과목 정보를 수정한다.
     * @param lectureId 교과 과목 아이디
     * @param lecture  변경할 교과 과목 정보
     * @return 변경된 교과 과목
     */
    @PutMapping("/lectures/{lectureId}")
    public Lecture modifyLecture(@PathVariable("lectureId") Long lectureId, @RequestBody Lecture lecture) {
        return lectureService.modifyLecture(lectureId, lecture);
    }

    @PostMapping("/lectures/{lectureId}/attendedSubject/students/{studentId}")
    public Lecture applyAttendedSubject(@PathVariable("lectureId") Long lectureId, @PathVariable("studentId") Long studentId) {
        return lectureService.applyAttendedSubject(lectureId, studentId);
    }

    /**
     * 수강 아이디로 과목정보 제거 수행
     * @param lectureId 삭제 대상 교과 과목 아이디
     * @param attendedId 삭제할 수강 아이디
     */
    @DeleteMapping("/lectures/{lectureId}/attendedSubject/{attendedId}")
    public boolean deleteSubjectById(@PathVariable("lectureId") Long lectureId, @PathVariable("attendedId") Long attendedId) {
        return lectureService.removeAttendedSubject(lectureId, attendedId);
    }

    @DeleteMapping("/lectures/{lectureId}")
    public void deleteLectureId(@PathVariable("lectureId") Long lectureId) {
        lectureService.deleteLecture(lectureId);
    }

}
