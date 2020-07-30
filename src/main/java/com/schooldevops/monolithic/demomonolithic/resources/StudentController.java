package com.schooldevops.monolithic.demomonolithic.resources;

import com.schooldevops.monolithic.demomonolithic.domains.Student;
import com.schooldevops.monolithic.demomonolithic.services.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @RequestMapping을 통해서 엔드포인트를 /student 로 시작하게 설정한다.
 * @RestController 은 Spring Framework 으로 REST API 를 만들때, 응답을 View 가 아닌 @ResponseBody로 반환하도록 해준다.
 */
@RequestMapping("/students")
@RestController
public class StudentController {

    /**
     * 학생 서비스를 Dependency Injection 한다.
     * 아래 내용과 같이 여기서는 생성자를 통한 DI 를 수행한다.
     * Spring이 BootUP 될때 의존성을 Spring Framework 가 주입해준다.
     */
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * 학생 등록
     * @RequestBody 를 이용하여, 클라이언트에서 전달된 JSON이 Object 로 매핑되도록 해준다.
     * @param student 프런트엔드로 부터 전달된 학생 정보 (JSON 을 Object 로 자동 매핑된다.)
     * @return 등록된후 ID가 할당된 학생 정보를 반환한다.
     */
    @PostMapping
    public Student joinStudent(@RequestBody Student student) {
        return studentService.joinStudent(student);
    }

    /**
     * 학생 정보 수정
     * @RequestBody 를 이용하여, 클라이언트에서 전달된 JSON이 Object 로 매핑되도록 해준다.
     * @param id 수정을 원하는 학생 아이디
     * @param student 수정을 되어야할 정보를 담고 있는 학생 정보
     * @return 수정된 버젼의 학생 정보가 반환된다.
     */
    @PutMapping("/{id}")
    public Student modifyStudent(@PathVariable("id") Long id, @RequestBody Student student) {
        return studentService.modifyStudent(id, student);
    }

    /**
     * 전체 학생 목록을 반환한다.
     * @return 등록된 모든 학생 정보를 반환한다. (실제 프로젝트에서는 페이징을 적용해야한다.)
     */
    @GetMapping
    public List<Student> findAllStudents() {
        return studentService.findAll();
    }

    /**
     * 학생 아이디로 학생 정보 조회
     * @param id 조회할 학생 아이디
     * @return 조회된 학생 정보를 반환한다.
     */
    @GetMapping("/{id}")
    public Student findById(@PathVariable("id") Long id) {
        return studentService.findById(id);
    }

    /**
     * 전공과목으로 학생 목록 조회하기, 전공 과목에 해당하는 모든 학생 목록를 반환한다.
     * @param subject 전공과목
     * @return 전공과목이 동일할 학생 정보
     */
    @GetMapping("/major/{subject}")
    public List<Student> findByMajor(@PathVariable("subject") String subject) {
        return studentService.findByMajor(subject);
    }

    /**
     * 학생 아이디로 학생 정보 제거 수행
     * @param id 삭제할 학생 아이디
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        studentService.deleteById(id);
    }
}
