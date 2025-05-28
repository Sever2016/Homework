package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.AverageAgeOfStudents;
import ru.hogwarts.school.model.LastFiveStudents;
import ru.hogwarts.school.model.NumberOfStudents;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    @Query(value = "SELECT count(id) as number FROM student", nativeQuery = true)
    NumberOfStudents getNumberOfStudents();

    @Query(value = "SELECT avg(age) as averageAge FROM student", nativeQuery = true)
    AverageAgeOfStudents getAverageAgeOfStudents();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<LastFiveStudents> getLastFiveStudents();
}