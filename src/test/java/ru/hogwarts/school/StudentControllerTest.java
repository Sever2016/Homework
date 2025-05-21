package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void gettingStudentById() throws Exception {
        Student student = new Student();
        student.setName("Вася");
        student.setAge(20);
        student.setId(3L);
        student.setFaculty(null);
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/students/3", Student.class))
                .isNotNull()
                .isEqualTo(student);
    }

    @Test
    void creatingStudent() throws Exception {
        Student student = new Student();
        student.setName("Вася");
        student.setAge(20);
        student.setId(3L);
        Assertions
                .assertThat(this.testRestTemplate.postForObject("http://localhost:" + port + "/students", student, Student.class))
                .isNotNull()
                .isEqualTo(student);
    }

    @Test
    void editingStudent() throws Exception {
        Student student = new Student();
        student.setName("Вася");
        student.setAge(27);
        student.setId(4L);
        this.testRestTemplate.put("http://localhost:" + port + "/students", student);
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/students/4", Student.class))
                .isNotNull()
                .isEqualTo(student);
    }

    @Test
    void deletingStudentById() throws Exception {
        Student student = new Student();
        student.setName("Вася");
        student.setId(4L);
        student.setAge(27);
        this.testRestTemplate.delete("http://localhost:" + port + "/student/4");
        Assertions
                .assertThat(this.testRestTemplate.exchange("http://localhost:" + port + "/student/4", HttpMethod.DELETE, null, Student.class))
                .isEqualTo(student);
    }

    @Test
    void filteringStudentsByAge() throws Exception {
        Student student1 = new Student(8L, "Вика", 22);
        Student student2 = new Student(9L, "Дима", 22);
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/students/filteredByAge/22", Student[].class))
                .isNotNull()
                .contains(student1)
                .contains(student2)
                .hasSize(2);
    }

    @Test
    void filteringByAgeBetween() throws Exception {
        Student student1 = new Student(8L, "Вика", 22);
        Student student2 = new Student(9L, "Дима", 22);
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/students/filteredByAgeBetween?minAge=20&maxAge=24", Student[].class))
                .isNotNull()
                .contains(student1)
                .contains(student2)
                .hasSize(3);
    }

    @Test
    void gettingFacultyByStudentId() {
        Faculty faculty = new Faculty(6L, "Абрикосцы", "Жёлтый");

        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/students/studentFaculty/2", Faculty.class))
                .isNotNull()
                .isEqualTo(faculty);
    }
}