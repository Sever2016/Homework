package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void gettingFacultyById() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Абрикосцы");
        faculty.setColor("Жёлтый");
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/faculties/6", Faculty.class))
                .isNotNull()
                .isEqualTo(faculty);
    }

    @Test
    void creatingFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Синицы");
        faculty.setId(3L);
        faculty.setColor("Синий");
        Assertions
                .assertThat(this.testRestTemplate.postForObject("http://localhost:" + port + "/faculties", faculty, Faculty.class))
                .isNotNull()
                .isEqualTo(faculty);
    }

    @Test
    void editingFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Зелень");
        faculty.setId(7L);
        faculty.setColor("Зелёный");
        this.testRestTemplate.put("http://localhost:" + port + "/faculties", faculty);
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/faculties/7", Faculty.class))
                .isNotNull()
                .isEqualTo(faculty);
    }

    @Test
    void deletingFacultyById() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Зелень");
        faculty.setId(2L);
        faculty.setColor("Зелёный");
        this.testRestTemplate.delete("http://localhost:" + port + "/faculties/2");
        Assertions
                .assertThat(this.testRestTemplate.exchange("http://localhost:" + port + "/faculties/2", HttpMethod.DELETE, null, Faculty.class))
                .isEqualTo(faculty);
    }

    @Test
    void filteringFacultiesByColor() throws Exception {
        Faculty faculty1 = new Faculty(1L, "Синицы", "Синий");
        Faculty faculty2 = new Faculty(4L, "Синяки", "Синий");
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/faculties/filteredByColor/Синий", Faculty[].class))
                .isNotNull()
                .contains(faculty1)
                .contains(faculty2)
                .hasSize(2);
    }

    @Test
    void filteringByColorOrName() throws Exception {
        Faculty faculty1 = new Faculty(3L, "Красавцы", "Красный");
        Faculty faculty2 = new Faculty(1L, "Синицы", "Синий");
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/faculties/filteredByColorOrName?name=Синицы&color=Красный", Faculty[].class))
                .isNotNull()
                .contains(faculty1)
                .contains(faculty2)
                .hasSize(2);
    }

    @Test
    void gettingStudentsFromFacultyByFacultyId() {
        Student student = new Student(2L, "Иннокентий", 18);
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/faculties/studentsFromFaculty/1", Student[].class))
                .isNotNull()
                .contains(student)
                .hasSize(1);
    }
}