package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Faculty faculty1 = new Faculty();
    private Faculty faculty2 = new Faculty();
    private Faculty faculty3 = new Faculty();

    @BeforeEach
    void clear() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
        faculty1.setName("Синицы");
        faculty1.setColor("Синий");
        faculty2.setName("Синяки");
        faculty2.setColor("Синий");
        faculty3.setName("Абрикосцы");
        faculty3.setColor("Жёлтый");
        facultyRepository.save(faculty1);
        facultyRepository.save(faculty2);
        facultyRepository.save(faculty3);
    }

    @Test
    void gettingFacultyById() throws Exception {
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/faculties/" + faculty1.getId().get(), Faculty.class))
                .isNotNull()
                .isEqualTo(faculty1);
    }

    @Test
    void creatingFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Травники");
        faculty.setColor("Зелёный");
        Assertions
                .assertThat(this.testRestTemplate.postForObject("http://localhost:" + port + "/faculties", faculty, Faculty.class))
                .isNotNull()
                .isEqualTo(faculty);
    }

    @Test
    void editingFaculty() throws Exception {
        faculty1.setName("Зелень");
        faculty1.setColor("Зелёный");
        this.testRestTemplate.put("http://localhost:" + port + "/faculties", faculty1);
        Assertions
                .assertThat(facultyRepository.findById(faculty1.getId().get()).get())
                .isNotNull()
                .isEqualTo(faculty1);
    }

    @Test
    void deletingFacultyById() throws Exception {
        this.testRestTemplate.delete("http://localhost:" + port + "/faculties/" + faculty1.getId().get());
        Assertions
                .assertThat(facultyRepository.findAll())
                .hasSize(2)
                .doesNotContain(faculty1);
    }

    @Test
    void filteringFacultiesByColor() throws Exception {
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/faculties/filteredByColor/" + faculty1.getColor(), Faculty[].class))
                .isNotNull()
                .contains(faculty1)
                .contains(faculty2)
                .hasSize(2);
    }

    @Test
    void filteringByColorOrName() throws Exception {
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/faculties/filteredByColorOrName?name=Синицы&color=Жёлтый", Faculty[].class))
                .isNotNull()
                .contains(faculty1)
                .contains(faculty3)
                .hasSize(2);
    }

    @Test
    void gettingStudentsFromFacultyByFacultyId() {
        Student student1 = new Student();
        student1.setName("Иннокентий");
        student1.setAge(24);
        student1.setFaculty(faculty1);
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setName("Вася");
        student2.setAge(25);
        student2.setFaculty(faculty1);
        studentRepository.save(student2);

        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/faculties/studentsFromFaculty/" + faculty1.getId().get(), Student[].class))
                .isNotNull()
                .hasSize(2)
                .contains(student1, student2);
    }
}