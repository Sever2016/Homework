package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private Student student1 = new Student();
    private Student student2 = new Student();
    private Student student3 = new Student();

    @BeforeEach
    void clear() {
        studentRepository.deleteAll();
        student1.setName("Вика");
        student1.setAge(22);
        student2.setName("Дима");
        student2.setAge(20);
        student3.setName("Вася");
        student3.setAge(22);
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
    }

    @Test
    void gettingStudentById() throws Exception {
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/students/" + student1.getId().get(), Student.class))
                .isNotNull()
                .isEqualTo(student1);
    }

    @Test
    void creatingStudent() throws Exception {
        Student student = new Student();
        student.setName("Вася");
        student.setAge(20);
        Assertions
                .assertThat(this.testRestTemplate.postForObject("http://localhost:" + port + "/students", student, Student.class))
                .isNotNull()
                .isEqualTo(student);
    }

    @Test
    void editingStudent() throws Exception {
        student1.setAge(23);
        student1.setName("Василий");
        this.testRestTemplate.put("http://localhost:" + port + "/students", student1);
        Assertions
                .assertThat(studentRepository.findById(student1.getId().get()).get())
                .isNotNull()
                .isEqualTo(student1);
    }

    @Test
    void deletingStudentById() throws Exception {
        this.testRestTemplate.delete("http://localhost:" + port + "/students/" + student1.getId().get());
        Assertions
                .assertThat(studentRepository.findAll())
                .hasSize(2)
                .doesNotContain(student1);
    }

    @Test
    void filteringStudentsByAge() throws Exception {
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/students/filteredByAge/" + student1.getAge(), Student[].class))
                .isNotNull()
                .contains(student1)
                .contains(student3)
                .hasSize(2);
    }

    @Test
    void filteringByAgeBetween() throws Exception {
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/students/filteredByAgeBetween?minAge=21&maxAge=25", Student[].class))
                .isNotNull()
                .contains(student1)
                .contains(student3)
                .hasSize(2);
    }

    @Test
    void gettingFacultyByStudentId() {
        Faculty faculty = new Faculty();
        faculty.setColor("Жёлтый");
        faculty.setName("Абрикосцы");
        student1.setFaculty(faculty);

        facultyRepository.save(faculty);
        studentRepository.save(student1);
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/students/studentFaculty/" + student1.getId().get(), Faculty.class))
                .isNotNull()
                .isEqualTo(faculty);
    }
}