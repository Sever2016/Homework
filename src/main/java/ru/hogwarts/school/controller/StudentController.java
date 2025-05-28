package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.*;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("students")
public class StudentController {
    private final StudentService studentService;
    private final AvatarService avatarService;

    public StudentController(StudentService studentService, AvatarService avatarService) {
        this.studentService = studentService;
        this.avatarService = avatarService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.editStudent(student));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable long id) {
        avatarService.deleteAvatar(id);
        return ResponseEntity.ok(studentService.deleteStudent(id));
    }

    @GetMapping("/filteredByAge/{age}")
    public ResponseEntity<List<Student>> filteredByAge(@PathVariable int age) {
        return ResponseEntity.ok(studentService.getStudentsByAge(age));
    }

    @GetMapping("/filteredByAgeBetween")
    public ResponseEntity<List<Student>> filteredByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
        return ResponseEntity.ok(studentService.getStudentsByAgeBetween(minAge, maxAge));
    }

    @GetMapping("/studentFaculty/{studentId}")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getStudentFaculty(studentId));
    }

    @GetMapping("/numberOfStudents")
    public ResponseEntity<NumberOfStudents> getNumberOfStudents() {
        return ResponseEntity.ok(studentService.getNumberOfStudents());
    }

    @GetMapping("/averageAgeOfStudents")
    public ResponseEntity<AverageAgeOfStudents> getAverageAgeOfStudents() {
        return ResponseEntity.ok(studentService.getAverageOfStudents());
    }

    @GetMapping("/lastFiveStudents")
    public ResponseEntity<List<LastFiveStudents>> getLastFiveStudents() {
        return ResponseEntity.ok(studentService.getLastFiveStudents());
    }
}